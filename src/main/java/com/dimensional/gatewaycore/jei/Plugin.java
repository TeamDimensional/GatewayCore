package com.dimensional.gatewaycore.jei;

import com.dimensional.gatewaycore.GatewayCore;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHWorldCategory;
import com.dimensional.gatewaycore.jei.cofhworld.CoFHWorldRegistrar;
import com.dimensional.gatewaycore.jei.immersive.*;
import com.dimensional.gatewaycore.jei.roots.FlowerGrowthCategory;
import com.dimensional.gatewaycore.jei.roots.RootsRegistrar;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;

@JEIPlugin
public class Plugin implements IModPlugin {
    public static final String FLOWER_GROWTH = "gateway:flower_growth";
    public static final String COFH_WORLD = "gateway:cofh_world";
    public static final String IE_MULTIBLOCK = "gateway:ie_multiblock";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();

        if (Loader.isModLoaded("roots")) {
            registry.addRecipeCategories(new FlowerGrowthCategory(helper));
        }
        if (Loader.isModLoaded("cofhworld")) {
            registry.addRecipeCategories(new CoFHWorldCategory(helper));
        }
        if (Loader.isModLoaded("immersiveengineering")) {
            registry.addRecipeCategories(new MultiblockRecipeCategory(helper));
        }
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {

        if (Loader.isModLoaded("roots")) {
            // Flower Growth Ritual
            GatewayCore.LOGGER.info("Loading Flower Growth recipes...");
            RootsRegistrar.register(registry);
        }

        if (Loader.isModLoaded("cofhworld")) {
            GatewayCore.LOGGER.info("Loading CoFH World recipes...");
            CoFHWorldRegistrar.register(registry);
        }

        if (Loader.isModLoaded("immersiveengineering")) {
            GatewayCore.LOGGER.info("Loading Immersive Engineering recipes...");
            ImmersiveEngineeringRegistrar.register(registry);
        }

        GatewayCore.LOGGER.info("JEI integration done");
    }
}
