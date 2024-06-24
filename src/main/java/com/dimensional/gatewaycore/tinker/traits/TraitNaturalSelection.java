package com.dimensional.gatewaycore.tinker.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;

public class TraitNaturalSelection extends AbstractTrait {
    private final static int level1 = 200;
    private final static int level2 = 500;

    public TraitNaturalSelection() {
        super("naturalselection", 0x795a2b);
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        super.afterBlockBreak(tool, world, state, pos, player, wasEffective);
        addExperience(tool, 1);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        super.afterHit(tool, player, target, damageDealt, wasCritical, wasHit);
        addExperience(tool, (int) damageDealt);
    }

    private void addExperience(ItemStack tool, int expBonus) {
        NBTTagCompound tag = TinkerUtil.getModifierTag(tool, identifier);
        int oldXp = tag.getInteger("selection_xp");
        int newXp = Math.min(oldXp + expBonus, level2);
        boolean shouldGiveMod = (newXp == level2 && oldXp < level2) || (newXp >= level1 && oldXp < level1);
        tag.setInteger("selection_xp", newXp);
        if (shouldGiveMod) {
            NBTTagCompound toolTag = TagUtil.getToolTag(tool.getTagCompound());
            int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS)+1;
            toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
            TagUtil.setToolTag(tool.getTagCompound(), toolTag);
        }
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int experience = modifierTag.getInteger("selection_xp");
        int targetExperience = 0;
        if (experience < level1) {
            targetExperience = level1;
        } else if (experience < level2) {
            targetExperience = level2;
        }

        if (targetExperience == 0) {
            String loc = String.format(LOC_Extra + ".maxed", getModifierIdentifier());
            return ImmutableList.of(Util.translateFormatted(loc));
        }

        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, experience, targetExperience));
    }
}
