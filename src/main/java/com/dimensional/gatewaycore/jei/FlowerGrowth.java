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

public class FlowerGrowth implements IRecipeCategory<FlowerGrowthRecipe> {

    private final IDrawable background;

    public FlowerGrowth(IGuiHelper helper) {
        this.background = helper.createDrawable(
            new ResourceLocation("gatewaycore", "textures/gui/flower_growth.png"), 0, 0, 86, 22);
    }

    @Override
    public String getUid() {
        return Plugin.FLOWER_GROWTH;
    }

    @Override
    public String getTitle() {
        return I18n.format("container.gateway.flower_growth.name");
    }

    @Override
    public String getModName() {
        return Tags.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }


    @Override
    public void setRecipe(IRecipeLayout layout, FlowerGrowthRecipe recipe, IIngredients ingredients) {
        layout.getItemStacks().addTooltipCallback(recipe);

        IGuiItemStackGroup group = layout.getItemStacks();
        group.init(0, true, 0, 2);
        group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        group.init(1, false, 68, 2);
        group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}
