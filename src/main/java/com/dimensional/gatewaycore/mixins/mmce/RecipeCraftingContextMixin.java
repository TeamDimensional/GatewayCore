package com.dimensional.gatewaycore.mixins.mmce;

import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.crafting.helper.RequirementComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(value = RecipeCraftingContext.class, remap = false)
public interface RecipeCraftingContextMixin {
    @Accessor
    Map<Long, List<ProcessingComponent<?>>> getTypeComponents();
}
