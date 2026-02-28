package com.dimensional.gatewaycore.mixins.vanilla;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dimensional.gatewaycore.utils.ItemToolModifier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

@Mixin(value = ItemTool.class, remap = true)
public abstract class ItemToolMixin extends Item implements ItemToolModifier {
    @Shadow(remap = false)
    private String toolClass;

    @Override
    public void gatewaycore$setToolType(String toolType) {
        toolClass = toolType;
    }
}
