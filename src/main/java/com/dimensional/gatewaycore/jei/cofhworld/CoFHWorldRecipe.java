package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.util.random.WeightedBlock;
import cofh.cofhworld.world.generator.WorldGen;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class CoFHWorldRecipe<T extends WorldGen> implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    public final T generator;
    private WeightedBlock[] blockOptions;
    private float totalWeight;

    public CoFHWorldRecipe(T gen) {
        generator = gen;
    }

    public void setBlockOptions(WeightedBlock[] options) {
        blockOptions = options;
        totalWeight = 0;
        for (WeightedBlock opt : blockOptions) totalWeight += opt.itemWeight;
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack stack, @Nonnull List<String> tooltip) {
        if (blockOptions.length > 1) {
            float weight = totalWeight / blockOptions[slotIndex].itemWeight;
            tooltip.add(String.format("%.2f", weight) + "% chance");
        }
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<ItemStack> outputs = new LinkedList<>();
        for (WeightedBlock opt : blockOptions) {
            ItemStack s = new ItemStack(opt.block, 1, opt.metadata);
            outputs.add(s);
        }
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }
}
