package com.dimensional.gatewaycore.events;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.GatewayCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.darkhax.gamestages.GameStageHelper;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber
public class TooltipEvents {

    private static final Map<Integer, String> tierNames = new HashMap<>();
    private static final TooltipStorage<ItemStack> stackStorage = new TooltipStorage<>(
        stack -> stack.getItem().getRegistryName() + "@" + stack.getMetadata(),
        stack -> stack.getItem().getRegistryName() + "@32767",
        stack -> stack.getItem().getCreatorModId(stack)
        );
    private static final TooltipStorage<FluidStack> fluidStorage = new TooltipStorage<>(FluidStack::getUnlocalizedName);

    public static void setTier(ItemStack item, int tier) {
        stackStorage.setTier(item, tier);
    }

    public static void setModTier(String mod, int tier) {
        stackStorage.setModTier(mod, tier);
        // Fluid storage doesn't use mod tiers
        // fluidStorage.setModTier(mod, tier);
    }

    public static void setUnlock(ItemStack item, int tier) {
        stackStorage.setUnlock(item, tier);
    }

    public static void setGated(ItemStack item) {
        stackStorage.setGated(item);
    }

    public static void setTier(FluidStack item, int tier) {
        fluidStorage.setTier(item, tier);
    }

    public static void setUnlock(FluidStack item, int tier) {
        fluidStorage.setUnlock(item, tier);
    }

    public static void setGated(FluidStack item) {
        fluidStorage.setGated(item);
    }

    public static void setTooltip(ItemStack item, String tooltip, TextFormatting tf) {
        stackStorage.setTooltip(item, tf.toString() + tooltip);
    }

    public static void setTooltip(ItemStack item, String tooltip) {
        stackStorage.setTooltip(item, TextFormatting.YELLOW.toString() + tooltip);
    }

    public static void setTooltip(FluidStack item, String tooltip, TextFormatting tf) {
        fluidStorage.setTooltip(item, tf.toString() + tooltip);
    }

    public static void setTooltip(FluidStack item, String tooltip) {
        fluidStorage.setTooltip(item, TextFormatting.YELLOW.toString() + tooltip);
    }

    public static void addPredicate(TooltipStorage.StackPredicate<ItemStack> p, String s) {
        addPredicate(UUID.randomUUID().toString(), p, s);
    }

    public static void addTierPredicate(TooltipStorage.StackPredicate<ItemStack> p, int s) {
        addTierPredicate(UUID.randomUUID().toString(), p, s);
    }

    public static void addPredicate(String n, TooltipStorage.StackPredicate<ItemStack> p, String s) {
        stackStorage.addPredicate(n, p, s);
    }

    public static void addTierPredicate(String n, TooltipStorage.StackPredicate<ItemStack> p, int s) {
        stackStorage.addTierPredicate(n, p, s);
    }

    public static void addFluidPredicate(TooltipStorage.StackPredicate<FluidStack> p, String s) {
        addFluidPredicate(UUID.randomUUID().toString(), p, s);
    }

    public static void addFluidTierPredicate(TooltipStorage.StackPredicate<FluidStack> p, int s) {
        addFluidTierPredicate(UUID.randomUUID().toString(), p, s);
    }

    public static void addFluidPredicate(String n, TooltipStorage.StackPredicate<FluidStack> p, String s) {
        fluidStorage.addPredicate(n, p, s);
    }

    public static void addFluidTierPredicate(String n, TooltipStorage.StackPredicate<FluidStack> p, int s) {
        fluidStorage.addTierPredicate(n, p, s);
    }

    public static void setTierName(int tier, String name) {
        tierNames.put(tier, name);
    }

    public static String getTierText(int tier) {
        if (tier < 0) return "Future content";
        if (tierNames.containsKey(tier)) return "Tier " + tier + " - " + tierNames.get(tier);
        return "Tier " + tier;
    }

    public static boolean hasTier(EntityPlayer player, int tier) {
        if (!Loader.isModLoaded("gamestages") || tier == 1) {
            return true;
        }
        return GameStageHelper.hasStage(player, "tier" + tier);
    }

    @Nullable
    private static FluidStack getFluidInStack(ItemStack stack) {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        if (fluid != null) return fluid;

        if (!(stack.getItem() instanceof ItemBlock)) return null;
        ItemBlock ib = (ItemBlock) stack.getItem();
        if (!(ib.getBlock() instanceof BlockFluidBase)) return null;
        BlockFluidBase bfb = (BlockFluidBase) ib.getBlock();
        return new FluidStack(bfb.getFluid(), 1000);
    }

    private static int getTier(ItemStack stack) {
        int tier = stackStorage.getTier(stack);
        FluidStack fluid = getFluidInStack(stack);
        if (fluid != null)
            tier = Math.max(tier, fluidStorage.getTier(fluid));
        return tier;
    }

    private static boolean isGated(ItemStack stack) {
        if (stackStorage.isGated(stack)) return true;
        FluidStack fluid = getFluidInStack(stack);
        return fluid != null && fluidStorage.isGated(fluid);
    }

    public static List<String> getTooltips(ItemStack stack, EntityPlayer player) {
        List<String> output = new LinkedList<>();

        int tier = getTier(stack);
        if (isGated(stack)) {
            // Gated
            output.add(TextFormatting.AQUA + "This item is gated! Its recipe was temporarily removed.");
        } else if (GatewayConfig.vanilla.showItemTiers && tier != 0) {
            // Tier
            TextFormatting color = TooltipEvents.hasTier(player, tier) ? TextFormatting.GREEN : TextFormatting.RED;
            output.add(color + TooltipEvents.getTierText(tier));
        }

        output.addAll(stackStorage.getTooltips(stack));
        FluidStack fluid = getFluidInStack(stack);
        if (fluid != null)
            output.addAll(fluidStorage.getTooltips(fluid));
        return output;
    }

    public static List<String> getTooltips(FluidStack stack, EntityPlayer player) {
        List<String> output = new LinkedList<>();

        int tier = fluidStorage.getTier(stack);
        if (fluidStorage.isGated(stack)) {
            output.add(TextFormatting.AQUA + "This item is gated! Its recipe was temporarily removed.");
        } else if (GatewayConfig.vanilla.showItemTiers && tier != 0) {
            TextFormatting color = TooltipEvents.hasTier(player, tier) ? TextFormatting.GREEN : TextFormatting.RED;
            output.add(color + TooltipEvents.getTierText(tier));
        }

        output.addAll(fluidStorage.getTooltips(stack));
        return output;
    }

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent e) {
        List<String> tooltips = getTooltips(e.getItemStack(), e.getEntityPlayer());
        e.getToolTip().addAll(tooltips);
    }

}
