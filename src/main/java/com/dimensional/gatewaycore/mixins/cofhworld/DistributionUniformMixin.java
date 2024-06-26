package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.numbers.INumberProvider;
import cofh.cofhworld.world.distribution.DistributionUniform;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DistributionUniform.class, remap = false)
public interface DistributionUniformMixin {
    @Accessor
    WorldGenerator getWorldGen();

    @Accessor
    INumberProvider getCount();

    @Accessor
    INumberProvider getMaxY();

    @Accessor
    INumberProvider getMinY();
}
