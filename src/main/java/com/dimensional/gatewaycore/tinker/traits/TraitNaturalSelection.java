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

import java.util.List;

public class TraitNaturalSelection extends AbstractTrait {
    private final static int level1 = 200;
    private final static int level2 = 500;

    private final String tagName = "gateway:NaturalSelectionExp";

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
        addExperience(tool, Math.max((int) (damageDealt / 2.5f), 1));
    }

    private static int getModifierCount(int xp) {
        if (xp < level1) return 0;
        if (xp < level2) return 1;
        return 2;
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        int exp = rootCompound.getInteger(tagName);
        int mods = getModifierCount(exp);
        NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
        toolTag.setInteger(Tags.FREE_MODIFIERS, toolTag.getInteger(Tags.FREE_MODIFIERS) + mods);
    }

    private void addExperience(ItemStack tool, int expBonus) {
        NBTTagCompound tag = TagUtil.getTagSafe(tool);
        int oldXp = tag.getInteger(tagName);
        int newXp = Math.min(oldXp + expBonus, level2);
        tag.setInteger(tagName, newXp);

        int addedMods = getModifierCount(newXp) - getModifierCount(oldXp);
        if (addedMods > 0) {
            NBTTagCompound toolTag = TagUtil.getToolTag(tool.getTagCompound());
            int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + addedMods;
            toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
            TagUtil.setToolTag(tool.getTagCompound(), toolTag);
        }
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        NBTTagCompound tag = TagUtil.getTagSafe(tool);
        int experience = tag.getInteger(tagName);
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
