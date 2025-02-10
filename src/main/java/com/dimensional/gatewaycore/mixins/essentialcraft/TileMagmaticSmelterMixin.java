package com.dimensional.gatewaycore.mixins.essentialcraft;

import com.dimensional.gatewaycore.GatewayConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import essentialcraft.common.tile.TileMagmaticSmelter;
import net.minecraftforge.fluids.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TileMagmaticSmelter.class, remap = false)
public class TileMagmaticSmelterMixin {

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @ModifyExpressionValue(method = "*", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
            target = "Lnet/minecraftforge/fluids/FluidRegistry;LAVA:Lnet/minecraftforge/fluids/Fluid;", remap = false))
    private Fluid changeFluid(Fluid original) {
        return FluidRegistry.getFluid(GatewayConfig.mods.magmaticSmelteryFluid);
    }

}
