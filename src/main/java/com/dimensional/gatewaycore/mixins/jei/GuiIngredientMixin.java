package com.dimensional.gatewaycore.mixins.jei;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dimensional.gatewaycore.utils.IngredientRendererGetter;

import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.GuiIngredient;

@Mixin(value = GuiIngredient.class, remap = false)
public class GuiIngredientMixin implements IngredientRendererGetter<Object> {

    @Shadow
    IIngredientRenderer<Object> ingredientRenderer;

    @Shadow
    private ITooltipCallback<Object> tooltipCallback;

    @Override
    public IIngredientRenderer<Object> gatewaycore$getRenderer() {
        return ingredientRenderer;
    }

    @Override
    public ITooltipCallback<Object> gatewaycore$getTooltipCallback() {
        return tooltipCallback;
    }

}
