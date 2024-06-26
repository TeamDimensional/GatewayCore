package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.biome.BiomeInfoSet;
import cofh.cofhworld.world.IConfigurableFeatureGenerator;
import cofh.cofhworld.world.distribution.Distribution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = Distribution.class, remap = false)
public interface DistributionMixin {
    @Accessor
    int getRarity();

    @Accessor
    boolean getWithVillage();

    @Accessor
    IConfigurableFeatureGenerator.GenRestriction getBiomeRestriction();

    @Accessor
    IConfigurableFeatureGenerator.GenRestriction getDimensionRestriction();

    @Accessor
    BiomeInfoSet getBiomes();

    @Accessor
    Set<Integer> getDimensions();
}
