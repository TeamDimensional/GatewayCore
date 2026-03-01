package com.dimensional.gatewaycore.utils;

import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredientRenderer;

public interface IngredientRendererGetter<T> {
    public IIngredientRenderer<T> gatewaycore$getRenderer();

    public ITooltipCallback<T> gatewaycore$getTooltipCallback();
}
