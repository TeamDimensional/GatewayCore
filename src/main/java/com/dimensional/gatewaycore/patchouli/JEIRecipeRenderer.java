package com.dimensional.gatewaycore.patchouli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dimensional.gatewaycore.jei.Plugin;
import com.dimensional.gatewaycore.jei.Plugin.RecipeWithWrapper;
import com.dimensional.gatewaycore.jei.RecipeLookupCriteria;
import com.dimensional.gatewaycore.render.ShaderManager;
import com.dimensional.gatewaycore.utils.GenericIngredient;
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
    private List<GenericIngredient<?>> inputs = new ArrayList<>();
    private List<GenericIngredient<?>> outputs = new ArrayList<>();
    private boolean removeBackground = true;

    transient private RecipeWithWrapper<?> recipe;
    transient private SimpleRecipeLayout layout;
    transient private int recipeCount;
    transient private int yOffset;
    transient private float scale;
    transient private int recipeWidth;
    transient private int recipeHeight;

    private static final int SCREEN_WIDTH = 120;

    @SuppressWarnings("unchecked")
    public void load() {
        if (recipe == null) {
            RecipeLookupCriteria rlc = new RecipeLookupCriteria(category);
            for (GenericIngredient<?> in : inputs)
                rlc.addInput(in);
            for (GenericIngredient<?> out : outputs)
                rlc.addOutput(out);
            List<RecipeWithWrapper<?>> recipesFound = Plugin.filterRecipes(rlc);
            recipeCount = recipesFound.size();

            if (recipeCount == 1) {
                recipe = recipesFound.get(0);
                layout = new SimpleRecipeLayout(recipe.category);
                IIngredients ingredients = new Ingredients();
                recipe.wrapper.getIngredients(ingredients);
                ((IRecipeCategory<IRecipeWrapper>) recipe.category).setRecipe(layout, recipe.wrapper, ingredients);
                scale = toThreshold(((float) SCREEN_WIDTH) / recipe.category.getBackground().getWidth());
                recipeWidth = recipe.category.getBackground().getWidth();
                recipeHeight = recipe.category.getBackground().getHeight();
            }
        }
    }

    public int getHeight() {
        return recipe == null ? 36 : recipe.category.getBackground().getHeight();
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    private static float toThreshold(float x) {
        if (x >= 1)
            return 1;
        int eights = (int) (x * 8);
        if (eights < 3)
            return x;
        return (float) eights / 8;
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

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, yOffset, 0);
        GlStateManager.scale(scale, scale, scale);
        Runnable drawBg = () -> {
            recipe.category.getBackground().draw(page.mc, 0, 0);
        };
        if (removeBackground) {
            ShaderManager.renderWith(ShaderManager.noJeiBackgroundColor, drawBg);
        } else {
            drawBg.run();
        }

        int actualMouseX = (int) (mouseX / scale),
                actualMouseY = (int) (mouseY / scale),
                actualLeft = (int) (page.parent.bookLeft / scale),
                actualTop = (int) ((yOffset + page.parent.bookTop) / scale),
                recipeMouseY = actualMouseY - actualTop,
                recipeMouseX = actualMouseX - actualLeft;
        for (GuiIngredientGroup<?> group : layout.getGroups()) {
            for (Map.Entry<Integer, ?> ingPair : group.getGuiIngredients().entrySet()) {
                // JEI messes up Blend and Depth states while rendering items, which make
                // Patchouli rendering look weird, so we fix their mess

                GuiIngredient<Object> ing = (GuiIngredient<Object>) ingPair.getValue();
                int slotNum = ingPair.getKey();

                GlStateManager.enableBlend();
                ing.draw(page.mc, 0, 0);

                if (ing.isMouseOver(actualLeft, actualTop, actualMouseX, actualMouseY)) {
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
                    // NOTE: JEI's API requires that tooltip lists are non-null.
                    // However, Thaumic JEI violates this principle! So we need to clean up their
                    // mess.
                    List<String> tooltips = recipe.wrapper.getTooltipStrings(recipeMouseX, recipeMouseY);
                    if (tooltips != null)
                        tooltip.addAll(tooltips);
                    tooltips = recipe.category.getTooltipStrings(recipeMouseX, recipeMouseY);
                    if (tooltips != null)
                        tooltip.addAll(tooltips);
                    page.parent.setTooltip(tooltip);
                }
            }
        }

        recipe.wrapper.drawInfo(page.mc, recipeWidth, recipeHeight, mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);

        GlStateManager.enableAlpha();
        recipe.category.drawExtras(page.mc);
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        if (blend)
            GlStateManager.enableBlend();
    }
}
