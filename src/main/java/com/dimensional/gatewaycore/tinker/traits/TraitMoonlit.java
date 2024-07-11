package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import com.shiroroku.theaurorian.AurorianConfig;

public class TraitMoonlit extends AbstractTrait {
    public TraitMoonlit() {
        super("moonlit", 0xd188d1);
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        if (entity.dimension == AurorianConfig.Config_AurorianDimID || !entity.world.isDaytime()) {
            if (random.nextInt(5) == 0) {
                newDamage = 0;
            }
        }
        return newDamage;
    }

}
