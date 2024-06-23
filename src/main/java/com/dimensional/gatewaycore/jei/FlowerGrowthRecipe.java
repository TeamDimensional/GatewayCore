package com.dimensional.gatewaycore.jei;

import epicsquid.roots.recipe.FlowerRecipe;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlowerGrowthRecipe implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    private final FlowerRecipe recipe;
    private final int count;

    public FlowerGrowthRecipe(FlowerRecipe r, int c) {
        recipe = r;
        count = c;
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        if (!input && recipe.getAllowedSoils().isEmpty()) {
            float weight = ((float) 100) / count;
            tooltip.add(String.format("%.2f", weight) + "% chance");
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();
        if (recipe.getAllowedSoils().isEmpty()) {
            assert Blocks.DIRT != null;
            inputs.add(new ItemStack(Blocks.DIRT));
            assert Blocks.GRASS != null;
            inputs.add(new ItemStack(Blocks.GRASS));
        } else {
            for (Ingredient it : recipe.getAllowedSoils()) {
                Collections.addAll(inputs, it.getMatchingStacks());
            }
        }

        List<List<ItemStack>> nestedInputs = new ArrayList<>();
        nestedInputs.add(inputs);

        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(recipe.getStack());
        ingredients.setInputLists(VanillaTypes.ITEM, nestedInputs);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }
}
