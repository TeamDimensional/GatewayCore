package com.dimensional.gatewaycore.patchouli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dimensional.gatewaycore.jei.Plugin;
import com.dimensional.gatewaycore.jei.Plugin.RecipeWithWrapper;
import com.dimensional.gatewaycore.jei.RecipeLookupCriteria;
import com.dimensional.gatewaycore.jei.RecipeLookupCriteriaBuilder;
import com.dimensional.gatewaycore.render.ShaderManager;
import com.dimensional.gatewaycore.utils.IngredientRendererGetter;

import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.ingredients.GuiIngredient;
import mezz.jei.gui.ingredients.GuiIngredientGroup;
import mezz.jei.ingredients.Ingredients;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;

public class JEIRecipeRenderer {
    private String category;
    private List<String> inputs = new ArrayList<>();
    private List<String> outputs = new ArrayList<>();

    transient private RecipeWithWrapper<?> recipe;
    transient private SimpleRecipeLayout layout;
    transient private int recipeCount;
    transient private int yOffset;

    @SuppressWarnings("unchecked")
    public void load() {
        if (recipe == null) {
            RecipeLookupCriteriaBuilder rlcb = new RecipeLookupCriteriaBuilder(category);
            for (String in : inputs)
                rlcb.addInput(in);
            for (String out : outputs)
                rlcb.addOutput(out);
            RecipeLookupCriteria rlc = rlcb.build();
            List<RecipeWithWrapper<?>> recipesFound = Plugin.filterRecipes(rlc);
            recipeCount = recipesFound.size();

            if (recipeCount == 1) {
                recipe = recipesFound.get(0);
                layout = new SimpleRecipeLayout(recipe.category);
                IIngredients ingredients = new Ingredients();
                recipe.wrapper.getIngredients(ingredients);
                ((IRecipeCategory<IRecipeWrapper>) recipe.category).setRecipe(layout, recipe.wrapper, ingredients);
            }
        }
    }

    public int getHeight() {
        return recipe == null ? 36 : recipe.category.getBackground().getHeight();
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    @SuppressWarnings("unchecked")
    public void render(BookPage page, int mouseX, int mouseY) {
        if (recipe == null) {
            // Performance does not matter bc this will not be in production
            page.parent.drawCenteredStringNoShadow("Error!", GuiBook.PAGE_WIDTH / 2, yOffset, page.book.headerColor);
            BookTextRenderer textRender = new BookTextRenderer(page.parent,
                    "Expected to find 1 recipe, but found " + recipeCount, 0, 16 + yOffset);
            textRender.render(mouseX, mouseY);
            return;
        }

        GlStateManager.pushAttrib();
        ShaderManager.renderWith(ShaderManager.noJeiBackgroundColor, () -> {
            recipe.category.getBackground().draw(page.mc, 0, yOffset);
        });
        for (GuiIngredientGroup<?> group : layout.getGroups()) {
            for (Map.Entry<Integer, ?> ingPair : group.getGuiIngredients().entrySet()) {
                // JEI messes up Blend and Depth states while rendering items, which make
                // Patchouli rendering look weird, so we fix their mess

                GuiIngredient<Object> ing = (GuiIngredient<Object>) ingPair.getValue();
                int slotNum = ingPair.getKey();

                boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
                ing.draw(page.mc, 0, yOffset);
                if (blend)
                    GlStateManager.enableBlend();

                if (ing.isMouseOver(page.parent.bookLeft, yOffset + page.parent.bookTop, mouseX, mouseY)) {
                    IngredientRendererGetter<Object> helper = (IngredientRendererGetter<Object>) ing;
                    Object stack = ing.getDisplayedIngredient();
                    ITooltipFlag.TooltipFlags tooltipFlag = page.mc.gameSettings.advancedItemTooltips
                            ? TooltipFlags.ADVANCED
                            : TooltipFlags.NORMAL;
                    List<String> tooltip = helper.gatewaycore$getRenderer().getTooltip(page.mc, stack, tooltipFlag);
                    ITooltipCallback<Object> tooltipCallback = helper.gatewaycore$getTooltipCallback();
                    if (tooltipCallback != null) {
                        tooltipCallback.onTooltip(slotNum, ing.isInput(), stack, tooltip);
                    }
                    page.parent.setTooltip(tooltip);
                }
            }
        }
        GlStateManager.popAttrib();
    }
}
