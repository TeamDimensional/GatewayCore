package com.dimensional.gatewaycore.events;

import java.util.List;

import com.dimensional.gatewaycore.utils.ItemToolModifier;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ToolUtils {
    private static final List<String> TOOL_TYPES = ImmutableList.of("pickaxe", "axe", "shovel", "hoe", "sword");

    public static void deleteToolType(ItemStack stack) {
        deleteToolType(stack.getItem());
    }

    public static void deleteToolType(Item item) {
        for (String it : TOOL_TYPES)
            item.setHarvestLevel(it, -1);
        ItemToolModifier mod = (ItemToolModifier) item;
        mod.gatewaycore$setToolType(null);
    }

    @SubscribeEvent
    public static void canHarvest(PlayerEvent.HarvestCheck e) {
        EntityPlayer player = e.getEntityPlayer();
        ItemStack tool = player.inventory.getCurrentItem();
        if (tool.getItem() instanceof ItemTool) {
            ItemTool mod = (ItemTool) tool.getItem();
            if (mod.getToolClasses(tool).isEmpty()) {
                e.setCanHarvest(false);
            }
        }
    }
}
