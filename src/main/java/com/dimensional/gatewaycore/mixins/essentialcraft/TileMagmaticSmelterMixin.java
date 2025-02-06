package com.dimensional.gatewaycore.mixins.essentialcraft;

import com.dimensional.gatewaycore.GatewayConfig;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import essentialcraft.common.tile.TileMagmaticSmelter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TileMagmaticSmelter.class, remap = false)
public class TileMagmaticSmelterMixin {
    @Shadow public FluidTank lavaTank;

    @Definition(id = "getFluid", method = "Lnet/minecraftforge/fluids/FluidStack;getFluid()Lnet/minecraftforge/fluids/Fluid;", remap = false)
    @Definition(id = "LAVA", field = "Lnet/minecraftforge/fluids/FluidRegistry;LAVA:Lnet/minecraftforge/fluids/Fluid;", remap = false)
    @Expression("?.getFluid() == LAVA")
    @ModifyExpressionValue(method = "update", at = @At("MIXINEXTRAS:EXPRESSION"), remap = true)
    private boolean changeFluid(boolean original) {
        if (lavaTank.getFluid() == null) return false;
        return lavaTank.getFluid().getFluid().getName().equals(GatewayConfig.mods.magmaticSmelteryFluid);
    }

    @Definition(id = "getFluid", method = "Lnet/minecraftforge/fluids/FluidStack;getFluid()Lnet/minecraftforge/fluids/Fluid;", remap = false)
    @Definition(id = "LAVA", field = "Lnet/minecraftforge/fluids/FluidRegistry;LAVA:Lnet/minecraftforge/fluids/Fluid;", remap = false)
    @Expression("?.getFluid() == LAVA")
    @ModifyExpressionValue(method = "isItemValidForSlot", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    private boolean isFluidValid(boolean original, @Local(argsOnly = true) ItemStack stack) {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        if (fluid == null) return false;
        return fluid.getFluid().getName().equals(GatewayConfig.mods.magmaticSmelteryFluid);
    }
}
