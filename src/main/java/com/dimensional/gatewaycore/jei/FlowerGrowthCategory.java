package com.dimensional.gatewaycore.jei;

import com.dimensional.gatewaycore.Tags;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class FlowerGrowthCategory implements IRecipeCategory<FlowerGrowthRecipe> {

    private final IDrawable background;

    public FlowerGrowthCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
            new ResourceLocation("gatewaycore", "textures/gui/flower_growth.png"), 0, 0, 86, 22);
    }

    @Override
    public @Nonnull String getUid() {
        return Plugin.FLOWER_GROWTH;
    }

    @Override
    public @Nonnull String getTitle() {
        return I18n.format("container.gateway.flower_growth.name");
    }

    @Override
    public @Nonnull String getModName() {
        return Tags.MOD_NAME;
    }

    @Override
    public @Nonnull IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, @Nonnull FlowerGrowthRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.addTooltipCallback(recipe);

        group.init(0, true, 0, 2);
        group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        group.init(1, false, 68, 2);
        group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}
