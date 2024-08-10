package com.dimensional.gatewaycore.mixins.calculator;

import com.dimensional.gatewaycore.GatewayConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sonar.calculator.mod.common.tileentity.generators.TileEntityConductorMast;

@Mixin(value = TileEntityConductorMast.class, remap = false)
public abstract class TileEntityConductorMastMixin {

    @Shadow public abstract int getTransmitters();

    @Shadow public int lastStations;

    @Shadow public int strikes;

    @Inject(method = "getStrikes", at = @At("HEAD"), cancellable = true)
    private void overrideStrikes(CallbackInfoReturnable<Integer> cir) {
        if (!GatewayConfig.mods.rebalanceConductorMast) return;
        int trans = getTransmitters();
        int batch1 = Math.min(16, trans);
        int batch2 = Math.min(16, Math.max(0, trans - 16));
        int batch3 = Math.max(0, trans - 32);
        cir.setReturnValue(1 + batch1 / 2 + batch2 / 4 + batch3 / 10);
    }

    // TODO: this only works partially and causes desyncs - cannot be fixed until Expression syntax is merged,
    //  so keeping it as is. CM is still probably mega op.
    /*
    @SuppressWarnings("deprecation")
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lsonar/core/network/sync/SyncTagType$INT;setObject(Ljava/lang/Object;)V", ordinal = 6))
    private void modifyPowerMultiplier(SyncTagType.INT instance, Object o, Operation<Void> original) {
        // Original value: (CalculatorConfig.CONDUCTOR_MAST_PER_TICK / 200 + this.lastStations * CalculatorConfig.WEATHER_STATION_PER_TICK / 200) * this.strikes * 4 * 200
        GatewayCore.LOGGER.info("power multiplier has been called, {}", o);
        if (!GatewayConfig.mods.rebalanceConductorMast) {
            original.call(instance, o);
            return;
        }

        int newValue = CalculatorConfig.CONDUCTOR_MAST_PER_TICK;
        int batch1 = Math.min(4, lastStations);
        int batch2 = Math.min(5, Math.max(0, lastStations - 4));
        int batch3 = Math.max(0, lastStations - 9);
        newValue += CalculatorConfig.WEATHER_STATION_PER_TICK * (batch1 * 2 + batch2 + batch3 / 4);
        GatewayCore.LOGGER.info("last stations: {}, power multiplier NEW: {}", lastStations, newValue);
        original.call(instance, (Object) (newValue * strikes * 4));
    }

     */

}
