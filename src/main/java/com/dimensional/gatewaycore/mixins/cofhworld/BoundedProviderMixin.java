package com.dimensional.gatewaycore.mixins.cofhworld;

import cofh.cofhworld.data.numbers.INumberProvider;
import cofh.cofhworld.data.numbers.operation.BoundedProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BoundedProvider.class, remap = false)
public interface BoundedProviderMixin {
    @Accessor
    INumberProvider getValue();
}
