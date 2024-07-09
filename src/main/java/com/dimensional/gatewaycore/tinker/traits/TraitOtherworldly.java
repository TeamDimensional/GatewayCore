package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitOtherworldly extends StackableTrait {

    private static final int avgTime = 16;  // in seconds

    public TraitOtherworldly() {
        this(1);
    }

    public TraitOtherworldly(int level) {
        super("otherworldly", TextFormatting.GREEN, level);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        // *20 because 20 ticks in a second
        if(!world.isRemote && entity instanceof EntityLivingBase && random.nextInt(20 * avgTime) < getLevel(tool)) {
            if(entity.dimension != 0) {  // we're not in overworld
                ToolHelper.healTool(tool, 1, (EntityLivingBase) entity);
            }
        }
    }

}
