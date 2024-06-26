package com.dimensional.gatewaycore.jei;

import cofh.cofhworld.world.IFeatureGenerator;
import com.dimensional.gatewaycore.GatewayCore;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHDistributionResolver;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHWorldCategory;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHWorldRecipe;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FlowerRecipe;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import cofh.cofhworld.init.WorldHandler;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

@JEIPlugin
public class Plugin implements IModPlugin {
    public static final String FLOWER_GROWTH = "gateway:flower_growth";
    public static final String COFH_WORLD = "gateway:cofh_world";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();

        if (Loader.isModLoaded("roots")) {
            registry.addRecipeCategories(new FlowerGrowthCategory(helper));
        }
        if (Loader.isModLoaded("cofhworld")) {
            registry.addRecipeCategories(new CoFHWorldCategory(helper));
        }
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (Loader.isModLoaded("roots")) {
            // Flower Growth Ritual
            GatewayCore.LOGGER.info("Loading Flower Growth recipes...");
            int c = (int) ModRecipes.getFlowerRecipes().values().stream().filter(r -> r.getAllowedSoils().isEmpty()).count();
            registry.addRecipes(ModRecipes.getFlowerRecipes().values(), FLOWER_GROWTH);
            registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_flower_growth), FLOWER_GROWTH);
            registry.handleRecipes(FlowerRecipe.class, r -> new FlowerGrowthRecipe(r, c), FLOWER_GROWTH);
        }

        if (Loader.isModLoaded("cofhworld")) {
            CoFHDistributionResolver.init();
            GatewayCore.LOGGER.info("Loading CoFH World recipes...");

            List<CoFHWorldRecipe<?>> recipes = new LinkedList<>();
            for (IFeatureGenerator feat : WorldHandler.getFeatures()) {
                CoFHWorldRecipe<?> recipe = CoFHDistributionResolver.makeRecipe(feat);
                if (recipe != null) {
                    recipes.add(recipe);
                }
            }

            registry.addRecipes(recipes, COFH_WORLD);
            registry.handleRecipes(CoFHWorldRecipe.class, r -> r, COFH_WORLD);
        }
        GatewayCore.LOGGER.info("JEI integration done");
    }
}
