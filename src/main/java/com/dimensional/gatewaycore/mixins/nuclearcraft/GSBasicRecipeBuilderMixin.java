package com.dimensional.gatewaycore.mixins.nuclearcraft;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import nc.integration.groovyscript.GSBasicRecipeBuilder;
import nc.recipe.BasicRecipe;
import nc.recipe.BasicRecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GSBasicRecipeBuilder.class, remap = false)
public abstract class GSBasicRecipeBuilderMixin extends AbstractRecipeBuilder<BasicRecipe> {
    @Shadow protected abstract BasicRecipeHandler getRecipeHandler();

    @Inject(method = "validate", at = @At("TAIL"))
    private void fixNulls(GroovyLog.Msg msg, CallbackInfo ci) {
        while (input.size() < getRecipeHandler().itemInputSize) input.add(null);
        while (output.size() < getRecipeHandler().itemOutputSize) output.add(null);
        while (fluidInput.size() < getRecipeHandler().fluidInputSize) fluidInput.add(null);
        while (fluidOutput.size() < getRecipeHandler().fluidOutputSize) fluidOutput.add(null);
    }
}
