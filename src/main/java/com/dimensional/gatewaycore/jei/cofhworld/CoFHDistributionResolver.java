package com.dimensional.gatewaycore.jei.cofhworld;

import cofh.cofhworld.util.random.WeightedBlock;
import cofh.cofhworld.world.IFeatureGenerator;
import cofh.cofhworld.world.distribution.*;
import cofh.cofhworld.world.generator.WorldGen;
import cofh.cofhworld.world.generator.WorldGenMinableCluster;
import com.dimensional.gatewaycore.mixins.cofhworld.*;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

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

    public static void init() {
        if (initialized) return;
        initialized = true;

        // Distribution converters
        distributionConverters.put(DistributionCave.class, (DistributionCave cave) -> {
            DistributionCaveMixin accessor = (DistributionCaveMixin) cave;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            return new CoFHWorldRecipe<>((WorldGen) generator);
        });
        distributionConverters.put(DistributionUniform.class, (DistributionUniform uniform) -> {
            DistributionUniformMixin accessor = (DistributionUniformMixin) uniform;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            return new CoFHWorldRecipe<>((WorldGen) generator);
        });
        distributionConverters.put(DistributionGaussian.class, (DistributionGaussian gaussian) -> {
            DistributionGaussianMixin accessor = (DistributionGaussianMixin) gaussian;
            WorldGenerator generator = accessor.getWorldGen();
            if (!(generator instanceof WorldGen)) return null;
            return new CoFHWorldRecipe<>((WorldGen) generator);
        });

        // generator converters
        generatorConverters.put(WorldGenMinableCluster.class, (CoFHWorldRecipe<WorldGenMinableCluster> recipe) -> {
            WorldGenMinableClusterMixin accessor = (WorldGenMinableClusterMixin) recipe.generator;
            recipe.setBlockOptions(accessor.getCluster().toArray(new WeightedBlock[0]));
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
