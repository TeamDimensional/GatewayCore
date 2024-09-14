package com.dimensional.gatewaycore.mixins.nuclearcraft;

import nc.integration.groovyscript.GSBasicRecipeRegistry;
import nc.recipe.BasicRecipe;
import nc.recipe.BasicRecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = GSBasicRecipeRegistry.class, remap = false)
public abstract class GSBasicRecipeRegistryMixin extends VirtualizedRegistry<BasicRecipe> {
    @Shadow protected abstract BasicRecipeHandler getRecipeHandler();

    @Override
    public void afterScriptLoad() {
        getRecipeHandler().postInit();
    }
}
