package com.dimensional.gatewaycore.jei.ec4;

import com.dimensional.gatewaycore.Tags;
import com.dimensional.gatewaycore.jei.Plugin;
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

public class MagicalFurnaceCategory implements IRecipeCategory<MagmaticSmelteryRecipe> {

    private final IDrawable background;

    public MagicalFurnaceCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
                new ResourceLocation("gatewaycore", "textures/gui/magical_furnace.png"), 0, 0, 106, 22);
    }

    @Override
    public @Nonnull String getUid() {
        return Plugin.MAGMATIC_FURNACE;
    }

    @Override
    public @Nonnull String getTitle() {
        return I18n.format("container.gateway.magmatic_furnace.name");
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

        group.init(0, true, 0, 2);
        group.init(1, false, 44, 2);
        group.init(2, false, 88, 2);
        group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        group.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
    }
}
