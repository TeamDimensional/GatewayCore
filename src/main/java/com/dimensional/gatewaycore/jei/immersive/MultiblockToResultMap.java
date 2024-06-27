package com.dimensional.gatewaycore.jei.immersive;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.*;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDevices;
import flaxbeard.immersivepetroleum.common.blocks.multiblocks.*;
import flaxbeard.immersivepetroleum.common.IPContent;
import flaxbeard.immersivepetroleum.common.blocks.metal.BlockTypes_IPMetalMultiblock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MultiblockToResultMap {

    private static final Map<Class<? extends MultiblockHandler.IMultiblock>, ItemStack> theMap = new HashMap<>();
    private static boolean initialized = false;

    public static void addPreview(Class<? extends MultiblockHandler.IMultiblock> multiblock, ItemStack preview) {
        theMap.put(multiblock, preview);
    }

    public static void init() {
        if (initialized) return;
        initialized = true;

        theMap.put(MultiblockAlloySmelter.class,
            new ItemStack(IEContent.blockStoneDevice, 1, BlockTypes_StoneDevices.ALLOY_SMELTER.getMeta()));
        theMap.put(MultiblockArcFurnace.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.ARC_FURNACE.getMeta()));
        theMap.put(MultiblockAssembler.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.ASSEMBLER.getMeta()));
        theMap.put(MultiblockAutoWorkbench.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.AUTO_WORKBENCH.getMeta()));
        theMap.put(MultiblockBlastFurnace.class,
            new ItemStack(IEContent.blockStoneDevice, 1, BlockTypes_StoneDevices.BLAST_FURNACE.getMeta()));
        theMap.put(MultiblockBlastFurnaceAdvanced.class,
            new ItemStack(IEContent.blockStoneDevice, 1, BlockTypes_StoneDevices.BLAST_FURNACE_ADVANCED.getMeta()));
        theMap.put(MultiblockBottlingMachine.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.BOTTLING_MACHINE.getMeta()));
        theMap.put(MultiblockBucketWheel.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.BUCKET_WHEEL.getMeta()));
        theMap.put(MultiblockCokeOven.class,
            new ItemStack(IEContent.blockStoneDevice, 1, BlockTypes_StoneDevices.COKE_OVEN.getMeta()));
        theMap.put(MultiblockCrusher.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.CRUSHER.getMeta()));
        theMap.put(MultiblockDieselGenerator.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.DIESEL_GENERATOR.getMeta()));
        theMap.put(MultiblockExcavator.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.EXCAVATOR.getMeta()));
        theMap.put(MultiblockFermenter.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.FERMENTER.getMeta()));
        theMap.put(MultiblockLightningrod.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.LIGHTNINGROD.getMeta()));
        theMap.put(MultiblockMetalPress.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.METAL_PRESS.getMeta()));
        theMap.put(MultiblockMixer.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.MIXER.getMeta()));
        theMap.put(MultiblockRefinery.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.REFINERY.getMeta()));
        theMap.put(MultiblockSheetmetalTank.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.TANK.getMeta()));
        theMap.put(MultiblockSilo.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.SILO.getMeta()));
        theMap.put(MultiblockSqueezer.class,
            new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.SQUEEZER.getMeta()));
    }

    public static @Nullable ItemStack getOutputStack(MultiblockHandler.IMultiblock mb) {
        Class<? extends MultiblockHandler.IMultiblock> clazz = mb.getClass();
        if (!theMap.containsKey(clazz)) return null;
        return theMap.get(clazz);
    }

}
