package com.dimensional.gatewaycore.jei.immersive;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.dimensional.gatewaycore.jei.DrawableScaled;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class MultiblockRecipe implements IRecipeWrapper {
    private final MultiblockHandler.IMultiblock multiblock;
    private final @Nullable ItemStack output;
    private final @Nullable IDrawable drawable;

    public MultiblockRecipe(MultiblockHandler.IMultiblock m, IGuiHelper h) {
        multiblock = m;
        output = MultiblockToResultMap.getOutputStack(multiblock);
        if (output != null) {
            IDrawable stackRenderer = h.createDrawableIngredient(output);
            drawable = new DrawableScaled(stackRenderer, 5.5f);
        } else {
            drawable = null;
        }
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (drawable != null) {
            drawable.draw(minecraft, 6, 6);
        }
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<List<ItemStack>> inputs = new LinkedList<>();
        IngredientStack[] istacks = multiblock.getTotalMaterials();
        for (IngredientStack it : istacks) {
            inputs.add(it.getSizedStackList());
        }

        if (output != null) {
            List<ItemStack> outputs = new LinkedList<>();
            outputs.add(output);
            ingredients.setOutputs(VanillaTypes.ITEM, outputs);
        }

        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
    }
}
