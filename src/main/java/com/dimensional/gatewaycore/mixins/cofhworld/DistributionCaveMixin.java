package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.numbers.INumberProvider;
import cofh.cofhworld.world.distribution.DistributionCave;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DistributionCave.class, remap = false)
public interface DistributionCaveMixin {
    @Accessor
    WorldGenerator getWorldGen();

    @Accessor
    INumberProvider getCount();

    @Accessor
    INumberProvider getGroundLevel();

    @Accessor
    boolean isCeiling();
}
