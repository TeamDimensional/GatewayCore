package com.dimensional.gatewaycore.jei.ec4;

import com.dimensional.gatewaycore.GatewayConfig;
import essentialcraft.api.OreSmeltingRecipe;
import essentialcraft.common.item.ItemsCore;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagmaticSmelteryRecipe implements IRecipeWrapper, ITooltipCallback<FluidStack> {
    private final OreSmeltingRecipe recipe;
    private final boolean forFurnace;

    public MagmaticSmelteryRecipe(OreSmeltingRecipe r, boolean f) {
        recipe = r;
        forFurnace = f;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ItemStack alloyItem = new ItemStack(ItemsCore.magicalAlloy);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("ore", recipe.oreName);
        alloyItem.setTagCompound(nbt);
        alloyItem.setCount(recipe.dropAmount);

        List<List<ItemStack>> inputs = new ArrayList<>();
        List<List<ItemStack>> outputs = new ArrayList<>();
        // output of getOres may not be modified, so we copy it
        List<ItemStack> inputOres = OreDictionary.getOres(recipe.oreName);
        List<ItemStack> outputIngotsConst = OreDictionary.getOres(recipe.outputName);
        List<ItemStack> outputIngots = new ArrayList<>();
        assert !outputIngotsConst.isEmpty();
        ItemStack copy = outputIngotsConst.get(0).copy();
        copy.setCount(recipe.dropAmount * 2 * (forFurnace ? 1 : 2));
        outputIngots.add(copy);

        List<ItemStack> alloy = Collections.singletonList(alloyItem);
        if (forFurnace) {
            inputs.add(inputOres);
            inputs.add(alloy);
            outputs.add(alloy);
            outputs.add(outputIngots);
        } else {
            alloyItem.setCount(recipe.dropAmount * 2);
            inputs.add(inputOres);
            inputs.add(alloy);
            outputs.add(alloy);
            outputs.add(outputIngots);
            outputs.add(Collections.singletonList(new ItemStack(ItemsCore.magicalSlag)));

            // Fluid
            Fluid fluid = FluidRegistry.getFluid(GatewayConfig.mods.magmaticSmelteryFluid);
            FluidStack magmaticSolvent = new FluidStack(fluid, 1000);
            ingredients.setInputs(VanillaTypes.FLUID, Collections.singletonList(magmaticSolvent));
        }

        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void onTooltip(int i, boolean b, @Nonnull FluidStack fluidStack, @Nonnull List<String> list) {
        // The only Fluidstack we have is the magmatic solvent
        list.add("Average amount per ore: 40 mb");
    }
}
