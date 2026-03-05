package com.dimensional.gatewaycore.mixins.chisel;

import com.dimensional.gatewaycore.GatewayConfig;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.chisel.api.block.BlockCreator;
import team.chisel.common.block.BlockCarvable;

@Mixin(targets = "team/chisel/Features$ChiselBlockProvider", remap = false)
public class FeaturesMixin {

    @ModifyVariable(
            method = "<init>(Lteam/chisel/api/block/BlockCreator;Ljava/lang/Class;)V",
            at = @At("HEAD"))
    private static BlockCreator<?> makeProvider(BlockCreator<?> provider, @Local Class<?> clazz) {
        if (clazz == BlockCarvable.class && GatewayConfig.mods.noSpecialChiselProperties) {
            return BlockCarvable::new;
        } else {
            return provider;
        }
    }

}
