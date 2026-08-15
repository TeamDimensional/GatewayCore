package com.dimensional.gatewaycore.mmce;

import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;

@SuppressWarnings("unused")
public class MMCEUtils {
    // Class for use in Crafttweaker scripts.

    public static RecipeCraftingContext getContext(RecipeEvent e) {
        return e.getContext();
    }
}
