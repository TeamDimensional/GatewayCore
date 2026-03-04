package com.dimensional.gatewaycore.mixins.soot;

import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import soot.compat.groovyscript.AlchemicalMixer;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(value = AlchemicalMixer.RecipeBuilder.class, remap = false)
public class AlchemicalMixerMixin {
    @ModifyConstant(method = "validate(Lcom/cleanroommc/groovyscript/api/GroovyLog$Msg;)V", constant = @Constant(intValue = 16))
    public int replaceAmount(int wrong) {
        return 8000;
    }

    // Can't target the lambda without hacks, so we replace the whole lambda.
    @Redirect(method = "validate(Lcom/cleanroommc/groovyscript/api/GroovyLog$Msg;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;anyMatch(Ljava/util/function/Predicate;)Z"))
    public boolean replaceAnyMatch(Stream<FluidStack> instance, Predicate<FluidStack> predicate) {
        return instance.anyMatch(t -> t.amount > 8000);
    }
}
