package com.dimensional.gatewaycore.mixins.mmce;

import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileMultiblockMachineController.class, remap = false)
public class TileMultiblockMachineControllerMixin {

    @Redirect(method = "checkStartResult", at = @At(value = "INVOKE", target = "Lhellfirepvp/modularmachinery/common/crafting/helper/RecipeCraftingContext$CraftingCheckResult;addError(Ljava/lang/String;)V"))
    private void markRecipeInvalid(RecipeCraftingContext.CraftingCheckResult instance, String s) {
        // This fires when the recipe fails its own post-check
        // If we're in post-check, we should display its error message instead of the generic one
        // This is managed by validity attribute, higher validity = higher priority, 1.0 is correct because we have already checked items and such.
        instance.addError(s);
        instance.validity = 1.0f;
    }
}
