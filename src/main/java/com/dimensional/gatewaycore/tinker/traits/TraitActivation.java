package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitActivation extends AbstractTrait {
    private static final int effectDuration = 30;

    public TraitActivation() {
        super("activation", 0xcf83ee);
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        super.afterBlockBreak(tool, world, state, pos, player, wasEffective);
        if (!wasEffective) return;
        if (random.nextInt(100) < 2 && !player.isPotionActive(MobEffects.HASTE)) { // 1/50 chance
            ToolHelper.damageTool(tool, 10, player);
            player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 20 * effectDuration, 2));
        }
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        super.afterHit(tool, player, target, damageDealt, wasCritical, wasHit);
        if (!wasHit) return;
        if ((random.nextInt(100) < 8 || wasCritical) && !player.isPotionActive(MobEffects.STRENGTH)) { // 2/25 chance
            ToolHelper.damageTool(tool, 10, player);
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20 * effectDuration, 2));  // 30 seconds
        }
    }
}
