package com.dimensional.gatewaycore.jei.roots;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FlowerRecipe;
import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;

import com.dimensional.gatewaycore.jei.Plugin;

public class RootsRegistrar {

    public static void register(IModRegistry registry) {

        int c = (int) ModRecipes.getFlowerRecipes().values().stream().filter(r -> r.getAllowedSoils().isEmpty()).count();
        registry.addRecipes(ModRecipes.getFlowerRecipes().values(), Plugin.FLOWER_GROWTH);
        registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_flower_growth), Plugin.FLOWER_GROWTH);
        registry.handleRecipes(FlowerRecipe.class, r -> new FlowerGrowthRecipe(r, c), Plugin.FLOWER_GROWTH);
    }

}
