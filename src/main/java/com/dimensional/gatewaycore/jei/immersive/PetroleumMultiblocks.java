package com.dimensional.gatewaycore.jei.immersive;

import flaxbeard.immersivepetroleum.common.IPContent;
import flaxbeard.immersivepetroleum.common.blocks.metal.BlockTypes_IPMetalMultiblock;
import flaxbeard.immersivepetroleum.common.blocks.multiblocks.MultiblockDistillationTower;
import flaxbeard.immersivepetroleum.common.blocks.multiblocks.MultiblockPumpjack;
import net.minecraft.item.ItemStack;

public class PetroleumMultiblocks {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;
        MultiblockToResultMap.addPreview(MultiblockPumpjack.class,
            new ItemStack(IPContent.blockMetalMultiblock, 1, BlockTypes_IPMetalMultiblock.PUMPJACK.getMeta()));
        MultiblockToResultMap.addPreview(MultiblockDistillationTower.class,
            new ItemStack(IPContent.blockMetalMultiblock, 1, BlockTypes_IPMetalMultiblock.DISTILLATION_TOWER.getMeta()));
    }
}
