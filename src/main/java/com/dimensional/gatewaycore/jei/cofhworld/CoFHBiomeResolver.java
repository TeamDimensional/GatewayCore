package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.data.biome.BiomeInfo;
import cofh.cofhworld.world.IConfigurableFeatureGenerator;
import com.dimensional.gatewaycore.mixins.cofhworld.BiomeInfoMixin;
import com.dimensional.gatewaycore.mixins.cofhworld.DistributionMixin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CoFHBiomeResolver {

    @SuppressWarnings("unchecked")
    public static @Nullable String stringifyIgnoringWhitelist(BiomeInfo info) {
        BiomeInfoMixin accessor = (BiomeInfoMixin) info;
        switch (accessor.getType()) {
            case 0:
            case 7:
                return (String) accessor.getData();
            case 1:
                return ((Biome.TempCategory) accessor.getData()).name();
            case 2:
                return ((BiomeDictionary.Type) accessor.getData()).getName();
            case 4:
            case 8:
                return ((Collection<ResourceLocation>) accessor.getData()).stream().map(ResourceLocation::getPath).collect(Collectors.joining(" | "));
            case 5:
                return ((Collection<Biome.TempCategory>) accessor.getData()).stream().map(Enum::name).collect(Collectors.joining(" | "));
            default:
                return null;
        }
    }

    public static @Nullable String stringify(BiomeInfo info) {
        String output = stringifyIgnoringWhitelist(info);
        BiomeInfoMixin accessor = (BiomeInfoMixin) info;
        if (output == null || accessor.getWhitelist()) {
            return output;
        }
        return "not (" + output + ")";
    }

    public static void addBiomeInfo(CoFHWorldRecipe<?> recipe, DistributionMixin accessor) {
        if (accessor.getBiomeRestriction() == IConfigurableFeatureGenerator.GenRestriction.NONE) return;
        List<String> items = new LinkedList<>();
        for (BiomeInfo b : accessor.getBiomes()) {
            String value = stringify(b);
            if (value != null) items.add(value);
        }
        if (!items.isEmpty()) {
            String output = String.join(" | ", items);
            if (accessor.getBiomeRestriction() == IConfigurableFeatureGenerator.GenRestriction.BLACKLIST)
                output = "not (" + output + ")";
            output = "Biome: " + output;
            if (output.length() > 25) {
                output = output.substring(0, 23) + "...";
            }
            recipe.addString(output);
        }
    }

}
