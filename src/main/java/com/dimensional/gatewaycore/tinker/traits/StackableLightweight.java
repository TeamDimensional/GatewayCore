package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;

public class StackableLightweight extends StackableTrait {

    public StackableLightweight() {
        this(1);
    }

    public StackableLightweight(int level) {
        super("gateway_lightweight", 0x00ff00, level);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);

        ToolNBT data = TagUtil.getToolStats(rootCompound);
        data.attackSpeedMultiplier = Math.max(data.attackSpeedMultiplier, 1.0f / (1.0f - 0.12f * getLevel(rootCompound)));
        TagUtil.setToolTag(rootCompound, data.get());
    }
}
