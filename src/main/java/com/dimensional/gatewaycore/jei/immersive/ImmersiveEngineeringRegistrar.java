package com.dimensional.gatewaycore.jei.immersive;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.IEContent;
import com.dimensional.gatewaycore.jei.Plugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.LinkedList;
import java.util.List;

public class ImmersiveEngineeringRegistrar {

    public static void register(IModRegistry registry) {

        MultiblockToResultMap.init();
        if (Loader.isModLoaded("immersivepetroleum")) {
            PetroleumMultiblocks.init();
        }

        IGuiHelper h = registry.getJeiHelpers().getGuiHelper();
        List<MultiblockRecipe> recipes = new LinkedList<>();
        for (MultiblockHandler.IMultiblock mb : MultiblockHandler.getMultiblocks()) {
            ItemStack it = MultiblockToResultMap.getOutputStack(mb);
            if (it != null) {
                recipes.add(new MultiblockRecipe(mb, h));
            }
        }

        registry.addRecipes(recipes, Plugin.IE_MULTIBLOCK);
        registry.handleRecipes(MultiblockRecipe.class, r -> r, Plugin.IE_MULTIBLOCK);
        registry.addRecipeCatalyst(new ItemStack(IEContent.itemTool), Plugin.IE_MULTIBLOCK);
    }

}
