package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.numbers.INumberProvider;
import cofh.cofhworld.util.random.WeightedBlock;
import cofh.cofhworld.world.generator.WorldGenMinableCluster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = WorldGenMinableCluster.class, remap = false)
public interface WorldGenMinableClusterMixin {
    @Accessor
    WeightedBlock[] getGenBlock();

    @Accessor
    List<WeightedBlock> getCluster();

    @Accessor
    INumberProvider getGenClusterSize();
}
