package com.dimensional.gatewaycore.tinker.traits;

import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class StackableEcological extends StackableTrait {

    private static final int avgTime = 40;  // in seconds

    public StackableEcological() {
        this(1);
    }

    public StackableEcological(int level) {
        super("gateway_ecological", TextFormatting.GREEN, level);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        // *20 because 20 ticks in a second
        if(!world.isRemote && entity instanceof EntityLivingBase && random.nextInt(20 * avgTime) < getLevel(tool)) {
            if(((EntityLivingBase) entity).getActiveItemStack() != tool) {
                ToolHelper.healTool(tool, 1, (EntityLivingBase) entity);
            }
        }
    }

}
