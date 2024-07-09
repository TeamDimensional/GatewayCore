package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

// This file is a copy of TraitCheap with a better number value lol
public class TraitVeryCheap extends AbstractTrait {
    public TraitVeryCheap() {
        super("gateway_cheap", TextFormatting.DARK_GRAY);
    }

    @Override
    public int onToolHeal(ItemStack tool, int amount, int newAmount, EntityLivingBase entity) {
        // 20% bonus durability repaired!
        return newAmount + amount * 20 / 100;
    }
}
