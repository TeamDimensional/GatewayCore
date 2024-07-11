package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.init.WorldHandler;
import cofh.cofhworld.world.IFeatureGenerator;
import com.dimensional.gatewaycore.jei.Plugin;
import mezz.jei.api.IModRegistry;

import java.util.LinkedList;
import java.util.List;

public class CoFHWorldRegistrar {

    public static void register(IModRegistry registry) {
        CoFHDistributionResolver.init();

        List<CoFHWorldRecipe<?>> recipes = new LinkedList<>();
        for (IFeatureGenerator feat : WorldHandler.getFeatures()) {
            CoFHWorldRecipe<?> recipe = CoFHDistributionResolver.makeRecipe(feat);
            if (recipe != null) {
                recipes.add(recipe);
            }
        }

        registry.addRecipes(recipes, Plugin.COFH_WORLD);
        registry.handleRecipes(CoFHWorldRecipe.class, r -> r, Plugin.COFH_WORLD);
    }

}
