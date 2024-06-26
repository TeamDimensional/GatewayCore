package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.numbers.INumberProvider;
import cofh.cofhworld.world.distribution.DistributionUnderfluid;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DistributionUnderfluid.class, remap = false)
public interface DistributionUnderfluidMixin {
    @Accessor
    WorldGenerator getWorldGen();

    @Accessor
    INumberProvider getCount();

    @Accessor
    String[] getFluidList();
}
