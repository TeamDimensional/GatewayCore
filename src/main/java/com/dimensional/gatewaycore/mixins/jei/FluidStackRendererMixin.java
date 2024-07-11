package com.dimensional.gatewaycore.mixins.jei;

import com.dimensional.gatewaycore.events.TooltipEvents;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = FluidStackRenderer.class, remap = false)
public class FluidStackRendererMixin {

    @Inject(
        method = "getTooltip(Lnet/minecraft/client/Minecraft;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List;",
        at = @At("RETURN")
    )
    public void onGetTooltip(Minecraft minecraft, FluidStack fluidStack, ITooltipFlag tooltipFlag, CallbackInfoReturnable<List<String>> cir) {
        cir.getReturnValue().addAll(TooltipEvents.getTooltips(fluidStack, minecraft.player));
    }

}
