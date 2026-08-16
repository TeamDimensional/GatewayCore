package com.dimensional.gatewaycore.mmce;

import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class MMCEUtils {

    @FunctionalInterface
    public interface IMultiblockRenderFunction {
        void render(TileMultiblockMachineController controller, double x, double y, double z);
    }

    public static final Map<String, IMultiblockRenderFunction> renderFunctions = new HashMap<>();

    public static void registerRenderer(String name, IMultiblockRenderFunction func) {
        renderFunctions.put(name, func);
    }

    // Class for use in Crafttweaker scripts.
    public static RecipeCraftingContext getContext(RecipeEvent e) {
        return e.getContext();
    }

}
