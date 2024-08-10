package com.dimensional.gatewaycore.mixins.tconstruct;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.GatewayCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

@Mixin(value = TinkerSmeltery.class, remap = false)
public class TinkerSmelteryMixin {
    @Inject(method = "registerRecipeOredictMelting", at = @At("HEAD"), cancellable = true)
    private static void onRegisterRecipeOredictMelting(CallbackInfo ci) {
        if (GatewayConfig.tConstruct.disableRecipeScan) {
            GatewayCore.LOGGER.info("Stopped Tinker's Construct from adding a bazillion recipes. Yay!");
            ci.cancel();
        }
    }
}
