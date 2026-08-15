package com.dimensional.gatewaycore.mixins.mmce;

import com.dimensional.gatewaycore.GatewayConfig;
import github.kasuminova.mmce.common.util.MultiFluidTank;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MultiFluidTank.class, remap = false)
public class MultiFluidTankMixin {
    @Redirect(method = "drain(Lnet/minecraftforge/fluids/FluidStack;Z)Lnet/minecraftforge/fluids/FluidStack;", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidStack;isFluidEqual(Lnet/minecraftforge/fluids/FluidStack;)Z"))
    private boolean fixRecipeChecking(FluidStack instance, FluidStack fluidStack) {
        if (instance == null || fluidStack == null) {
            return false;
        }
        if (instance.isFluidEqual(fluidStack)) {
            return true;
        }
        return GatewayConfig.mods.fixModularMachineryNBT && instance.getFluid() == fluidStack.getFluid() && fluidStack.tag == null;
    }
}
