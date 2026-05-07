package com.dimensional.gatewaycore.mixins.witchery;

import com.dimensional.gatewaycore.GatewayConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.msrandom.witchery.integration.jei.JeiWitcheryPlugin$Companion", remap = false)
public class JeiWitcheryPluginMixin {
    @Inject(method = "reload()V", at = @At("HEAD"), cancellable = true)
    private void noReload(CallbackInfo ci) {
        if (GatewayConfig.mods.noWitcheryJeiReload) {
            ci.cancel();
        }
    }
}
