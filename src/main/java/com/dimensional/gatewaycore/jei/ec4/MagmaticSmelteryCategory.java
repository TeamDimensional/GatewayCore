package com.dimensional.gatewaycore.jei.ec4;

import com.dimensional.gatewaycore.Tags;
import com.dimensional.gatewaycore.jei.Plugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MagmaticSmelteryCategory implements IRecipeCategory<MagmaticSmelteryRecipe> {

    private final IDrawable background;

    public MagmaticSmelteryCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
            new ResourceLocation("gatewaycore", "textures/gui/magmatic_smeltery.png"), 0, 0, 106, 55);
    }

    @Override
    public @Nonnull String getUid() {
        return Plugin.MAGMATIC_SMELTERY;
    }

    @Override
    public @Nonnull String getTitle() {
        return I18n.format("container.gateway.magmatic_smeltery.name");
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
    public void setRecipe(IRecipeLayout layout, @Nonnull MagmaticSmelteryRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = layout.getItemStacks();

        group.init(0, true, 0, 23);
        group.init(1, false, 44, 12);
        group.init(2, false, 88, 12);
        group.init(3, false, 44, 34);
        group.init(4, false, 88, 34);
        ItemStack slag2 = ingredients.getOutputs(VanillaTypes.ITEM).get(2).get(0).copy();
        slag2.setCount(ingredients.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount());
        group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        group.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
        group.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(2));
        group.set(4, slag2);

        IGuiFluidStackGroup groupF = layout.getFluidStacks();
        groupF.addTooltipCallback(recipe);
        groupF.init(0, true, 1, 3);
        groupF.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
    }
}
