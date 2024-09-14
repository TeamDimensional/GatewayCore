package com.dimensional.gatewaycore.mixins.nuclearcraft;

import nc.recipe.BasicRecipeHandler;
import nc.recipe.RecipeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(value = BasicRecipeHandler.class, remap = false)
public class BasicRecipeHandlerMixin {
    @Shadow public List<Set<String>> validFluids;

    /**
     * @author Wizzerinus
     * @reason Fix the list instance getting replaced with a different list instance
     */
    @Overwrite
    protected void setValidFluids() {
        if (validFluids == null) {
            validFluids = new ArrayList<>();
        }
        validFluids.clear();
        validFluids.addAll(RecipeHelper.validFluids((BasicRecipeHandler) (Object) this));
    }
}
