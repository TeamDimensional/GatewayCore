package com.dimensional.gatewaycore.tinker.traits;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EntityLivingBase;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.Util;

import java.util.List;

public class StackableCrude extends StackableTrait {

    private static final float damageMult = 0.05f;

    public StackableCrude() {
        this(1);
    }

    public StackableCrude(int level) {
        super("gateway_crude", 0x424242, level);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        boolean hasArmor = target.getTotalArmorValue() > 0;
        if(!hasArmor) newDamage += damage * getLevel(tool) * damageMult;
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        float bonus = damageMult * getLevel(tool);

        return ImmutableList.of(Util.translateFormatted(loc, Util.dfPercent.format(bonus)));
    }

}
