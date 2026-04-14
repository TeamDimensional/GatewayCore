package com.dimensional.gatewaycore.mixins.nuclearcraft;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import org.spongepowered.asm.mixin.Mixin;
import nc.integration.groovyscript.GSContainer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import nc.integration.groovyscript.GSBasicRecipeRegistryImpl.*;

@Mixin(value = GSContainer.class, remap = false)
public abstract class GSContainerMixin extends GroovyPropertyContainer {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addMoreProperties(CallbackInfo ci) {
        addProperty(new GSMultiblockInfiltratorRecipeRegistry("multiblock_infiltrator"));
    }
}
