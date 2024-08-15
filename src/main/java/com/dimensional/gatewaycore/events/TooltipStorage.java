package com.dimensional.gatewaycore.events;

import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

public class TooltipStorage<T> {
    public interface StackPredicate<U> {
        boolean satisfies(U stack);
    }

    public interface TStringifier<U> {
        String stringify(U stack);
    }

    public static class PredicateWithText<U> {
        final StackPredicate<U> ps;
        final String s;
        PredicateWithText(StackPredicate<U> predicate, String text) {
            ps = predicate;
            s = text;
        }
    }

    public static class PredicateWithNumber<U> {
        final StackPredicate<U> ps;
        final int s;
        PredicateWithNumber(StackPredicate<U> predicate, int tier) {
            ps = predicate;
            s = tier;
        }
    }

    private final Map<String, Integer> tiers = new HashMap<>();
    private final Set<String> unlocks = new HashSet<>();
    private final Set<String> gated = new HashSet<>();
    private final Map<String, String> tooltips = new HashMap<>();
    private final Map<String, PredicateWithText<T>> predicates = new HashMap<>();
    private final Map<String, PredicateWithNumber<T>> tierPredicates = new HashMap<>();
    private final Map<String, Integer> modTiers = new HashMap<>();

    private final TStringifier<T> stringifier;
    private final @Nullable TStringifier<T> stringifier2;
    private final @Nullable TStringifier<T> modLookup;

    TooltipStorage(TStringifier<T> stringifier, @Nullable TStringifier<T> stringifier2, @Nullable TStringifier<T> modLookup) {
        this.stringifier = stringifier;
        this.stringifier2 = stringifier2;
        this.modLookup = modLookup;
    }

    TooltipStorage(TStringifier<T> stringifier) {
        this(stringifier, null, null);
    }

    private void setTier(String item, int tier) {
        tiers.put(item, tier);
    }

    public void setTier(T item, int tier) {
        setTier(stringifier.stringify(item), tier);
    }

    public void setModTier(String mod, int tier) {
        modTiers.put(mod, tier);
    }

    private void setUnlock(String item, int tier) {
        setTier(item, tier - 1);
        unlocks.add(item);
    }

    public void setUnlock(T item, int tier) {
        setUnlock(stringifier.stringify(item), tier);
    }

    private void setGated(String item) {
        gated.add(item);
    }

    public void setGated(T item) {
        setGated(stringifier.stringify(item));
    }

    public boolean isGated(T item) {
        return gated.contains(stringifier.stringify(item));
    }

    private void setTooltipBase(String item, String tooltip) {
        tooltips.put(item, tooltip);
    }

    private void setTooltipBase(T item, String tooltip) {
        setTooltipBase(stringifier.stringify(item), tooltip);
    }

    public void setTooltip(T item, String tooltip, TextFormatting tf) {
        setTooltipBase(item, tf.toString() + tooltip);
    }

    public void setTooltip(T item, String tooltip) {
        setTooltipBase(item, TextFormatting.YELLOW.toString() + tooltip);
    }

    public void addPredicate(StackPredicate<T> p, String s) {
        addPredicate(UUID.randomUUID().toString(), p, s);
    }

    public void addTierPredicate(StackPredicate<T> p, int s) {
        addTierPredicate(UUID.randomUUID().toString(), p, s);
    }

    public void addPredicate(String n, StackPredicate<T> p, String s) {
        predicates.put(n, new PredicateWithText<>(p, s));
    }

    public void addTierPredicate(String n, StackPredicate<T> p, int s) {
        tierPredicates.put(n, new PredicateWithNumber<>(p, s));
    }

    public int getTier(T stack) {
        for (PredicateWithNumber<T> pair : tierPredicates.values()) {
            if (pair.ps.satisfies(stack)) {
                return pair.s;
            }
        }
        String key = stringifier.stringify(stack);
        if (tiers.containsKey(key)) return tiers.get(key);
        if (stringifier2 != null) {
            key = stringifier2.stringify(stack);
            if (tiers.containsKey(key)) return tiers.get(key);
        }
        if (modLookup != null) {
            String mod = modLookup.stringify(stack);
            return modTiers.getOrDefault(mod, 1);
        }
        return 1;
    }

    public List<String> getTooltips(T stack) {
        List<String> output = new LinkedList<>();
        int tier = getTier(stack);
        String key = stringifier.stringify(stack);
        // Unlock
        boolean unlock = unlocks.contains(key);
        if (unlock) output.add(TextFormatting.AQUA + "Unlocks tier " + (tier + 1));
        // Tooltip
        String tt = tooltips.get(key);
        if (tt != null) output.add(tt);
        // Predicates
        for (PredicateWithText<T> pair : predicates.values()) {
            if (pair.ps.satisfies(stack)) {
                output.add(pair.s);
            }
        }

        return output;
    }

}
