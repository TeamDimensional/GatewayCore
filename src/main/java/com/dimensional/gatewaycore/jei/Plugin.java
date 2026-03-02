package com.dimensional.gatewaycore.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.dimensional.gatewaycore.GatewayCore;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHWorldCategory;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHWorldRegistrar;
import com.dimensional.gatewaycore.jei.ec4.EC4Registrar;
import com.dimensional.gatewaycore.jei.ec4.MagicalFurnaceCategory;
import com.dimensional.gatewaycore.jei.ec4.MagmaticSmelteryCategory;
import com.dimensional.gatewaycore.jei.roots.FlowerGrowthCategory;
import com.dimensional.gatewaycore.jei.roots.RootsRegistrar;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.ingredients.Ingredients;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class Plugin implements IModPlugin {
    public static class RecipeWithWrapper<T extends IRecipeWrapper> {
        public final IRecipeCategory<T> category;
        public final T wrapper;

        public RecipeWithWrapper(IRecipeCategory<T> category, T wrapper) {
            this.category = category;
            this.wrapper = wrapper;
        }
    }

    public static final String FLOWER_GROWTH = "gateway:flower_growth";
    public static final String COFH_WORLD = "gateway:cofh_world";
    public static final String MAGMATIC_FURNACE = "gateway:magmatic_furnace";
    public static final String MAGMATIC_SMELTERY = "gateway:magmatic_smeltery";

    private static IJeiRuntime runtime;
    private static IModRegistry modRegistry;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public static List<RecipeWithWrapper<?>> filterRecipes(RecipeLookupCriteria rlc) {
        List<RecipeWithWrapper<?>> recipes = new ArrayList<>();
        IRecipeCategory<?> cat = getCategory(rlc.getCategory());
        if (cat == null) {
            GatewayCore.LOGGER.warn("Unknown recipe category: {}", rlc.getCategory());
            return recipes;
        }
        for (IRecipeWrapper wrapper : getRecipeRegistry().getRecipeWrappers(cat)) {
            IIngredients ingredients = new Ingredients();
            wrapper.getIngredients(ingredients);
            try {
                if (rlc.matches(ingredients)) {
                    @SuppressWarnings("unchecked")
                    RecipeWithWrapper<?> pair = new RecipeWithWrapper<>((IRecipeCategory<IRecipeWrapper>) cat, wrapper);
                    recipes.add(pair);
                }
            } catch (NullPointerException e) {
                GatewayCore.LOGGER.error(
                        "While evaluating a recipe in '{}', got an NPE. This is a bug in the mod adding that category, not in JEI or Patchouli.",
                        rlc.getCategory());
                e.printStackTrace();
            }
        }
        return recipes;
    }

    public static IRecipeRegistry getRecipeRegistry() {
        return runtime.getRecipeRegistry();
    }

    public static IRecipeCategory<?> getCategory(String catName) {
        return getRecipeRegistry().getRecipeCategory(catName);
    }

    public static IIngredientRegistry getIngredientRegistry() {
        return modRegistry.getIngredientRegistry();
    }

    public static <V> IIngredientHelper<V> getIngredientHelper(V ingredient) {
        return getIngredientRegistry().getIngredientHelper(ingredient);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();

        if (Loader.isModLoaded("roots")) {
            registry.addRecipeCategories(new FlowerGrowthCategory(helper));
        }
        if (Loader.isModLoaded("cofhworld")) {
            registry.addRecipeCategories(new CoFHWorldCategory(helper));
        }
        if (Loader.isModLoaded("essentialcraft")) {
            registry.addRecipeCategories(new MagmaticSmelteryCategory(helper));
            registry.addRecipeCategories(new MagicalFurnaceCategory(helper));
        }
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {

        modRegistry = registry;

        if (Loader.isModLoaded("roots")) {
            // Flower Growth Ritual
            GatewayCore.LOGGER.info("Loading Flower Growth recipes...");
            RootsRegistrar.register(registry);
        }

        if (Loader.isModLoaded("cofhworld")) {
            GatewayCore.LOGGER.info("Loading CoFH World recipes...");
            CoFHWorldRegistrar.register(registry);
        }

        if (Loader.isModLoaded("essentialcraft")) {
            GatewayCore.LOGGER.info("Loading Essentialcraft recipes...");
            EC4Registrar.register(registry);
        }

        GatewayCore.LOGGER.info("JEI integration done");
    }
}
