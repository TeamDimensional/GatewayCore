package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.world.IConfigurableFeatureGenerator;
import com.dimensional.gatewaycore.mixins.cofhworld.DistributionMixin;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

public class CoFHDimensionResolver {

    public static @Nullable String stringify(boolean blacklist, Set<Integer> info) {
        if (info.isEmpty()) return null;
        if (info.size() == 2 && info.contains(1) && info.contains(-1) && blacklist) {
            // this is a common special case where the spawn says "not nether and not the end"
            return null;
        }

        String text = info.stream().map(r -> DimensionManager.getProviderType(r).getName()).collect(Collectors.joining(" | "));
        if (blacklist)
            text = "not (" + text + ")";
        return text;
    }

    public static void addDimensionInfo(CoFHWorldRecipe<?> recipe, DistributionMixin accessor) {
        if (accessor.getDimensionRestriction() == IConfigurableFeatureGenerator.GenRestriction.NONE) return;
        String output = stringify(
            accessor.getDimensionRestriction() == IConfigurableFeatureGenerator.GenRestriction.BLACKLIST, accessor.getDimensions());
        if (output != null) {
            output = "Dim: " + output;
            if (output.length() > 25) {
                output = output.substring(0, 23) + "...";
            }
            recipe.addString(output);
        }
    }
}
