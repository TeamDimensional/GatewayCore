package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.biome.BiomeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BiomeInfo.class, remap = false)
public interface BiomeInfoMixin {
    @Accessor
    Object getData();

    @Accessor
    int getType();

    @Accessor
    boolean getWhitelist();
}
