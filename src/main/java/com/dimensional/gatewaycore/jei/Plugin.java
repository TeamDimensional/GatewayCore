package com.dimensional.gatewaycore.jei;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FlowerRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class Plugin implements IModPlugin {
    public static final String FLOWER_GROWTH = "gateway:flower_growth";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new FlowerGrowth(helper));
    }

    @Override
    public void register(IModRegistry registry) {
        int c = (int) ModRecipes.getFlowerRecipes().values().stream().filter(r -> r.getAllowedSoils().isEmpty()).count();
        registry.addRecipes(ModRecipes.getFlowerRecipes().values(), FLOWER_GROWTH);
        registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_flower_growth), FLOWER_GROWTH);
        registry.handleRecipes(FlowerRecipe.class, r -> new FlowerGrowthRecipe(r, c), FLOWER_GROWTH);
    }
}
