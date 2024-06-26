package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.util.random.WeightedBlock;
import cofh.cofhworld.world.generator.WorldGen;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CoFHWorldRecipe<T extends WorldGen> implements IRecipeWrapper, ITooltipCallback {
    public final T generator;
    private WeightedBlock[] materialOptions;
    private WeightedBlock[] blockOptions;
    private float totalWeight;
    private final List<String> texts = new LinkedList<>();
    public final List<FluidStack> fluids = new ArrayList<>();

    public CoFHWorldRecipe(T gen) {
        generator = gen;
    }

    public void setBlockOptions(WeightedBlock[] options) {
        blockOptions = options;
        totalWeight = 0;
        for (WeightedBlock opt : blockOptions) totalWeight += opt.itemWeight;
    }

    public void setMaterialOptions(WeightedBlock[] options) {
        materialOptions = options;
    }

    public void addString(String s) {
        texts.add(s);
    }

    public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack stack, @Nonnull List<String> tooltip) {
        if (blockOptions.length > 1 && !input) {
            float weight = ((float) blockOptions[slotIndex - 6].itemWeight) / totalWeight * 100;
            tooltip.add(String.format("%.2f", weight) + "% chance");
        }
    }

    public void onTooltip(int slotIndex, boolean input, @Nonnull FluidStack stack, @Nonnull List<String> tooltip) {
        tooltip.add("Generates under the fluid");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onTooltip(int i, boolean b, @Nonnull Object o, @Nonnull List list) {
        if (o instanceof ItemStack) {
            onTooltip(i, b, (ItemStack) o, (List<String>) list);
        } else if (o instanceof FluidStack) {
            onTooltip(i, b, (FluidStack) o, (List<String>) list);
        }
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<ItemStack> inputs = new LinkedList<>();
        for (WeightedBlock opt : materialOptions) {
            ItemStack s = new ItemStack(opt.block, 1, opt.metadata);
            inputs.add(s);
        }
        ingredients.setInputs(VanillaTypes.ITEM, inputs);

        List<ItemStack> outputs = new LinkedList<>();
        for (WeightedBlock opt : blockOptions) {
            ItemStack s = new ItemStack(opt.block, 1, opt.metadata);
            outputs.add(s);
        }
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);

        List<List<FluidStack>> fluidStacks = new LinkedList<>();
        fluidStacks.add(fluids);
        ingredients.setInputLists(VanillaTypes.FLUID, fluidStacks);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int h = minecraft.fontRenderer.FONT_HEIGHT;
        int currentY = 61;
        for (String line : texts) {
            minecraft.fontRenderer.drawString(line, 2, currentY, Color.BLACK.getRGB());
            currentY += h;
        }
    }
}
