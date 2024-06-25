package com.dimensional.gatewaycore.events;

import com.dimensional.gatewaycore.GatewayConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.darkhax.gamestages.GameStageHelper;

import java.util.*;

@Mod.EventBusSubscriber
public class TooltipEvents {

    public interface StackPredicate {
        boolean satisfies(ItemStack stack);
    }

    private static class PredicateWithText {
        final StackPredicate ps;
        final String s;
        PredicateWithText(StackPredicate predicate, String text) {
            ps = predicate;
            s = text;
        }
    }

    private static class PredicateWithNumber {
        final StackPredicate ps;
        final int s;
        PredicateWithNumber(StackPredicate predicate, int tier) {
            ps = predicate;
            s = tier;
        }
    }

    private static final Map<String, Integer> tiers = new HashMap<>();
    private static final Set<String> unlocks = new HashSet<>();
    private static final Set<String> gated = new HashSet<>();
    private static final Map<String, String> tooltips = new HashMap<>();
    private static final Map<Integer, String> tierNames = new HashMap<>();
    private static final List<PredicateWithText> predicates = new ArrayList<>();
    private static final List<PredicateWithNumber> tierPredicates = new ArrayList<>();

    private static String stringify(ItemStack item) {
        return item.getItem().getRegistryName() + "@" + item.getMetadata();
    }

    public static void setTier(String item, int tier) {
        tiers.put(item, tier);
    }

    public static void setTier(ItemStack item, int tier) {
        setTier(stringify(item), tier);
    }

    public static void setUnlock(String item, int tier) {
        setTier(item, tier - 1);
        unlocks.add(item);
    }

    public static void setUnlock(ItemStack item, int tier) {
        setUnlock(stringify(item), tier);
    }

    public static void setGated(String item) {
        gated.add(item);
    }

    public static void setGated(ItemStack item) {
        setGated(stringify(item));
    }

    public static void setTooltipBase(String item, String tooltip) {
        tooltips.put(item, tooltip);
    }

    public static void setTooltipBase(ItemStack item, String tooltip) {
        setTooltipBase(stringify(item), tooltip);
    }

    public static void setTooltip(ItemStack item, String tooltip, TextFormatting tf) {
        setTooltipBase(item, tf.toString() + tooltip);
    }

    public static void setTooltip(ItemStack item, String tooltip) {
        setTooltipBase(item, TextFormatting.YELLOW.toString() + tooltip);
    }

    public static void addPredicate(StackPredicate p, String s) {
        predicates.add(new PredicateWithText(p, s));
    }

    public static void addTierPredicate(StackPredicate p, int s) {
        tierPredicates.add(new PredicateWithNumber(p, s));
    }

    public static void setTierName(int tier, String name) {
        tierNames.put(tier, name);
    }

    private static String getTierText(int tier) {
        if (tier < 0) return "Future content";
        if (tierNames.containsKey(tier)) return "Tier " + tier + " - " + tierNames.get(tier);
        return "Tier " + tier;
    }

    private static boolean hasTier(EntityPlayer player, int tier) {
        if (!Loader.isModLoaded("gamestages") || tier == 1) {
            return true;
        }
        return GameStageHelper.hasStage(player, "tier" + tier);
    }

    private static int getTier(ItemStack stack) {
        for (PredicateWithNumber pair : tierPredicates) {
            if (pair.ps.satisfies(stack)) {
                return pair.s;
            }
        }
        String key = stringify(stack);
        return tiers.getOrDefault(key, 1);
    }

    private static List<String> getTooltips(ItemStack stack, EntityPlayer player) {
        List<String> output = new LinkedList<>();
        int tier = getTier(stack);
        String key = stringify(stack);
        boolean isGated = gated.contains(key);
        if (isGated) {
            // Gated
            output.add(TextFormatting.AQUA + "This item is gated! Its recipe was temporarily removed.");
        } else if (GatewayConfig.showItemTiers && tier != 0) {
            // Tier
            TextFormatting color = hasTier(player, tier) ? TextFormatting.GREEN : TextFormatting.RED;
            output.add(color + getTierText(tier));
        }
        // Unlock
        boolean unlock = unlocks.contains(key);
        if (unlock) output.add(TextFormatting.AQUA + "Unlocks tier " + (tier + 1));
        // Tooltip
        String tt = tooltips.get(key);
        if (tt != null) output.add(tt);
        // Predicates
        for (PredicateWithText pair : predicates) {
            if (pair.ps.satisfies(stack)) {
                output.add(pair.s);
            }
        }

        return output;
    }

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent e) {
        List<String> tooltips = getTooltips(e.getItemStack(), e.getEntityPlayer());
        e.getToolTip().addAll(tooltips);
    }

}
