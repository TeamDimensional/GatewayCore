package com.dimensional.gatewaycore.jei.ec4;

import essentialcraft.api.OreSmeltingRecipe;
import essentialcraft.common.block.BlocksCore;
import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;

import com.dimensional.gatewaycore.jei.Plugin;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.stream.Collectors;

public class EC4Registrar {

    private static OreSmeltingRecipe fixOutputName(OreSmeltingRecipe r) {
        String outputName = r.outputName.isEmpty() ? "ingot" + r.oreName.substring(3) : r.outputName;
        return new OreSmeltingRecipe(r.oreName, outputName, r.color, r.dropAmount);
    }

    private static Collection<OreSmeltingRecipe> generateRecipes() {
        return OreSmeltingRecipe.RECIPES.stream()
                .map(EC4Registrar::fixOutputName)
                .filter(r -> OreDictionary.doesOreNameExist(r.oreName) &&
                            OreDictionary.doesOreNameExist(r.outputName))
                .collect(Collectors.toList());
    }

    public static void register(IModRegistry registry) {

        registry.addRecipes(generateRecipes(), Plugin.MAGMATIC_SMELTERY);
        registry.addRecipes(generateRecipes(), Plugin.MAGMATIC_FURNACE);
        registry.addRecipeCatalyst(new ItemStack(BlocksCore.magmaticSmeltery), Plugin.MAGMATIC_SMELTERY);
        registry.addRecipeCatalyst(new ItemStack(BlocksCore.furnaceMagic), Plugin.MAGMATIC_FURNACE);
        registry.addRecipeCatalyst(new ItemStack(BlocksCore.furnaceMagic, 1, 4), Plugin.MAGMATIC_FURNACE);
        registry.addRecipeCatalyst(new ItemStack(BlocksCore.furnaceMagic, 1, 8), Plugin.MAGMATIC_FURNACE);
        registry.addRecipeCatalyst(new ItemStack(BlocksCore.furnaceMagic, 1, 12), Plugin.MAGMATIC_FURNACE);
        registry.handleRecipes(OreSmeltingRecipe.class, r -> new MagmaticSmelteryRecipe(r, false), Plugin.MAGMATIC_SMELTERY);
        registry.handleRecipes(OreSmeltingRecipe.class, r -> new MagmaticSmelteryRecipe(r, true), Plugin.MAGMATIC_FURNACE);
    }

}
