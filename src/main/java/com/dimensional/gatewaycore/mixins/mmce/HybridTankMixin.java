package com.dimensional.gatewaycore.mixins.mmce;

import com.dimensional.gatewaycore.GatewayConfig;
import hellfirepvp.modularmachinery.common.util.HybridTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = HybridTank.class, remap = false)
public class HybridTankMixin extends FluidTank {
    public HybridTankMixin(int capacity) {
        super(capacity);
    }

    @Redirect(method = "drainInternal(Lnet/minecraftforge/fluids/FluidStack;Z)Lnet/minecraftforge/fluids/FluidStack;", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidTank;drainInternal(Lnet/minecraftforge/fluids/FluidStack;Z)Lnet/minecraftforge/fluids/FluidStack;"))
    public FluidStack fixExtracting(FluidTank instance, FluidStack resource, boolean doDrain)
    {
        if (!GatewayConfig.mods.fixModularMachineryNBT) {
            return super.drain(resource, doDrain);
        }

        FluidStack inside = instance.getFluid();
        if (resource == null || inside == null || inside.getFluid() != resource.getFluid()) {
            return null;
        }
        if (resource.tag == null || resource.isFluidEqual(inside)) {
            return drainInternal(resource.amount, doDrain);
        } else {
            return null;
        }
    }
}
