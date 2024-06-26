package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.world.distribution.DistributionGaussian;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DistributionGaussian.class, remap = false)
public interface DistributionGaussianMixin {
    @Accessor
    WorldGenerator getWorldGen();
}
