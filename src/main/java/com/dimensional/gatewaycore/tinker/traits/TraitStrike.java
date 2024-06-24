package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitStrike extends AbstractTrait {
    public TraitStrike() {
        super("strike", TextFormatting.WHITE);
    }

    @Override
    public boolean isCriticalHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target) {
        if (random.nextInt(4) == 0) {
            ToolHelper.damageTool(tool, 2, player);
            return true;
        }
        return super.isCriticalHit(tool, player, target);
    }
}
