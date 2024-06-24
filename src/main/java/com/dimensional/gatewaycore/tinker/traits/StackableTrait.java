package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public abstract class StackableTrait extends AbstractTrait {

    public final int level;
    public final String name;

    public StackableTrait(String identifier, TextFormatting formatting) {
        this(identifier, Util.enumChatFormattingToColor(formatting), 1);
    }

    public StackableTrait(String identifier, int color) {
        this(identifier, color, 1);
    }

    public StackableTrait(String identifier, TextFormatting formatting, int level) {
        this(identifier, Util.enumChatFormattingToColor(formatting), level);
    }

    public StackableTrait(String identifier, int color, int level) {
        super(level == 1 ? identifier : identifier + level, color);
        this.level = level;
        name = identifier;
        // if (level > 1) TinkerRegistry.registerModifierAlias(getTraitBase(), identifier + level);
    }

    @Override
    public String getModifierIdentifier() {
        return name;
    }

    @Override
    public String getLocalizedName() {
        String locName = Util.translate("modifier.%s.name", name);
        if (level > 1) locName = locName + " " + TinkerUtil.getRomanNumeral(level);
        return locName;
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translate("modifier.%s.desc", name);
    }

    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        NBTTagList tagList = TagUtil.getModifiersTagList(rootCompound);
        int index = TinkerUtil.getIndexInCompoundList(tagList, this.getModifierIdentifier());
        NBTTagCompound tag = new NBTTagCompound();
        if (index > -1) {
            tag = tagList.getCompoundTagAt(index);
        } else {
            index = tagList.tagCount();
            tagList.appendTag(tag);
        }

        ModifierNBT data = ModifierNBT.readTag(tag);
        // level initializes at 1
        if (!tag.getBoolean(name)) {
            data.level = 0;
            tag.setBoolean(name, true);
        }
        data.level += level;
        data.write(tag);
        tagList.set(index, tag);
        TagUtil.setModifiersTagList(rootCompound, tagList);
    }

    protected int getLevel(ItemStack tool) {
        NBTTagCompound modifierNBT = TinkerUtil.getModifierTag(tool, "gateway_crude");
        ModifierNBT data = new ModifierNBT(modifierNBT);
        return data.level;
    }

}
