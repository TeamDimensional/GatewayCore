package com.dimensional.gatewaycore.jei.cofhworld;

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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CoFHWorldCategory implements IRecipeCategory<CoFHWorldRecipe<?>> {

    private final IDrawable background;
    private final IDrawable icon;

    public CoFHWorldCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
            new ResourceLocation("gatewaycore", "textures/gui/cofh_world.png"), 0, 0, 128, 107);
        this.icon = helper.createDrawableIngredient(new ItemStack(Blocks.TALLGRASS, 1, 2));
    }

    @Override
    public @Nonnull String getUid() {
        return Plugin.COFH_WORLD;
    }

    @Override
    public @Nonnull String getTitle() {
        return I18n.format("container.gateway.cofh_world.name");
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
    @SuppressWarnings("unchecked")
    public void setRecipe(IRecipeLayout layout, @Nonnull CoFHWorldRecipe<?> recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.addTooltipCallback(recipe);
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        for (int i = 0; i < Math.min(6, inputs.size()); i++) {
            int x = 2 + 18 * (i % 2);
            int y = 2 + 18 * (i / 2);
            group.init(i, true, x, y);
            group.set(i, inputs.get(i));
        }

        for (int i = 0; i < Math.min(9, outputs.size()); i++) {
            int x = 72 + 18 * (i % 3);
            int y = 2 + 18 * (i / 3);
            group.init(i + 6, false, x, y);
            group.set(i + 6, outputs.get(i));
        }

        List<List<FluidStack>> fluidInputs = ingredients.getInputs(VanillaTypes.FLUID);
        if (!fluidInputs.isEmpty()) {
            IGuiFluidStackGroup fluids = layout.getFluidStacks();
            fluids.addTooltipCallback(recipe);
            fluids.init(0, true, 46, 3);
            fluids.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
        }
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }
}
