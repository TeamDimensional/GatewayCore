package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.data.numbers.ConstantProvider;
import cofh.cofhworld.data.numbers.INumberProvider;
import cofh.cofhworld.data.numbers.operation.BoundedProvider;
import cofh.cofhworld.util.random.WeightedBlock;
import cofh.cofhworld.world.IFeatureGenerator;
import cofh.cofhworld.world.distribution.*;
import cofh.cofhworld.world.generator.WorldGen;
import cofh.cofhworld.world.generator.WorldGenMinableCluster;
import com.dimensional.gatewaycore.mixins.cofhworld.*;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CoFHDistributionResolver {

    private interface DistributionRecipeConverter<T extends IFeatureGenerator> {
        @Nullable CoFHWorldRecipe<?> createRecipe(T generator);
    }

    private interface GeneratorRecipeConverter<S extends WorldGen> {
        void processWorldGen(CoFHWorldRecipe<S> recipe);
    }

    private static final Map<Class<?>, DistributionRecipeConverter<?>> distributionConverters = new HashMap<>();
    private static final Map<Class<?>, GeneratorRecipeConverter<?>> generatorConverters = new HashMap<>();
    private static boolean initialized = false;

    static void addInts(CoFHWorldRecipe<?> recipe, String unlocalizedName, INumberProvider... providers) {
        Stream<INumberProvider> providerStream =
            Arrays.stream(providers).map(r -> r instanceof BoundedProvider ? ((BoundedProviderMixin)r).getValue() : r);
        if (providerStream.allMatch(p -> p instanceof ConstantProvider)) {
            recipe.addString(I18n.format(unlocalizedName, Arrays.stream(providers).map(r -> r.intValue(null, null, null)).toArray()));
        }
    }

    static void addBaseParams(CoFHWorldRecipe<?> recipe, Distribution dist) {
        DistributionMixin accessor = (DistributionMixin) dist;
        int rarity = accessor.getRarity();
        if (rarity > 1) {
            float chance = ((float) 100) / rarity;
            String chanceStr = String.format("%.2f", chance);
            recipe.addString(I18n.format("container.gateway.cofh_world.rarity", chanceStr));
        }
        if (!accessor.getWithVillage())
            recipe.addString(I18n.format("container.gateway.cofh_world.no_village"));
        CoFHBiomeResolver.addBiomeInfo(recipe, accessor);
        CoFHDimensionResolver.addDimensionInfo(recipe, accessor);
    }

    public static void init() {
        if (initialized) return;
        initialized = true;

        // Distribution converters
        distributionConverters.put(DistributionCave.class, (DistributionCave cave) -> {
            DistributionCaveMixin accessor = (DistributionCaveMixin) cave;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            CoFHWorldRecipe<?> recipe = new CoFHWorldRecipe<>((WorldGen) generator);

            addBaseParams(recipe, cave);
            if (accessor.isCeiling())
                recipe.addString(I18n.format("container.gateway.cofh_world.cave.ceiling"));
            else
                recipe.addString(I18n.format("container.gateway.cofh_world.cave.floor"));
            addInts(recipe, "container.gateway.cofh_world.vein_count", accessor.getCount());
            addInts(recipe, "container.gateway.cofh_world.max_height", accessor.getGroundLevel());

            return recipe;
        });
        distributionConverters.put(DistributionUniform.class, (DistributionUniform uniform) -> {
            DistributionUniformMixin accessor = (DistributionUniformMixin) uniform;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            CoFHWorldRecipe<?> recipe = new CoFHWorldRecipe<>((WorldGen) generator);

            addBaseParams(recipe, uniform);
            addInts(recipe, "container.gateway.cofh_world.vein_count", accessor.getCount());
            addInts(recipe, "container.gateway.cofh_world.uniform_generation", accessor.getMinY(), accessor.getMaxY());

            return recipe;
        });
        distributionConverters.put(DistributionGaussian.class, (DistributionGaussian gaussian) -> {
            DistributionGaussianMixin accessor = (DistributionGaussianMixin) gaussian;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            CoFHWorldRecipe<?> recipe = new CoFHWorldRecipe<>((WorldGen) generator);

            addBaseParams(recipe, gaussian);
            addInts(recipe, "container.gateway.cofh_world.vein_count", accessor.getCount());
            if (accessor.getMeanY() instanceof ConstantProvider && accessor.getMaxVar() instanceof ConstantProvider) {
                int meanY = accessor.getMeanY().intValue(null, null, null);
                int maxVar = accessor.getMaxVar().intValue(null, null, null);
                int minY = meanY - maxVar, maxY = meanY + maxVar;
                recipe.addString(I18n.format("container.gateway.cofh_world.gaussian_generation", minY, maxY, meanY));
            }

            return recipe;
        });
        distributionConverters.put(DistributionUnderfluid.class, (DistributionUnderfluid underfluid) -> {
            DistributionUnderfluidMixin accessor = (DistributionUnderfluidMixin) underfluid;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            CoFHWorldRecipe<?> recipe = new CoFHWorldRecipe<>((WorldGen) generator);

            addBaseParams(recipe, underfluid);
            addInts(recipe, "container.gateway.cofh_world.attempt_count", accessor.getCount());
            String[] fluids = accessor.getFluidList();
            if (fluids == null) {
                recipe.fluids.add(new FluidStack(FluidRegistry.getFluid("water"), 1000));
            } else {
                for (String s : fluids) {
                    recipe.fluids.add(new FluidStack(FluidRegistry.getFluid(s), 1000));
                }
            }

            return recipe;
        });

        // generator converters
        generatorConverters.put(WorldGenMinableCluster.class, (CoFHWorldRecipe<WorldGenMinableCluster> recipe) -> {
            WorldGenMinableClusterMixin accessor = (WorldGenMinableClusterMixin) recipe.generator;
            recipe.setBlockOptions(accessor.getCluster().toArray(new WeightedBlock[0]));
            recipe.setMaterialOptions(accessor.getGenBlock());
            addInts(recipe, "container.gateway.cofh_world.vein_size", accessor.getGenClusterSize());
        });
    }

    @SuppressWarnings("unchecked")
    private static @Nullable <S extends WorldGen> CoFHWorldRecipe<S> processWorldGen(CoFHWorldRecipe<S> recipe) {
        Class<? extends WorldGen> clazzWG = recipe.generator.getClass();
        if (!generatorConverters.containsKey(clazzWG)) return null;
        ((GeneratorRecipeConverter<S>) (generatorConverters.get(clazzWG))).processWorldGen(recipe);
        return recipe;
    }

    @SuppressWarnings("unchecked")
    public static @Nullable <T extends IFeatureGenerator> CoFHWorldRecipe<?> makeRecipe(T item) {
        Class<? extends IFeatureGenerator> clazzD = item.getClass();
        if (!distributionConverters.containsKey(clazzD)) return null;
        CoFHWorldRecipe<?> recipe = (
            (DistributionRecipeConverter<T>) distributionConverters.get(clazzD)).createRecipe(item);
        if (recipe == null) return null;
        return processWorldGen(recipe);
    }

}
