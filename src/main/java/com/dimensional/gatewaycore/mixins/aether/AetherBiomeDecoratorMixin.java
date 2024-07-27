package com.dimensional.gatewaycore.mixins.aether;

import com.dimensional.gatewaycore.GatewayConfig;
import com.gildedgames.the_aether.world.biome.AetherBiomeDecorator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(value = AetherBiomeDecorator.class, remap = false)
public class AetherBiomeDecoratorMixin {

    @WrapOperation(method = "genDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/terraingen/TerrainGen;generateOre(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/feature/WorldGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraftforge/event/terraingen/OreGenEvent$GenerateMinable$EventType;)Z"))
    private boolean dontGenerateOre(World world, Random random, WorldGenerator worldGenerator, BlockPos blockPos, OreGenEvent.GenerateMinable.EventType eventType, Operation<Boolean> original) {
        if (GatewayConfig.disableAetherOres) {
            return false;
        }
        return original.call(world, random, worldGenerator, blockPos, eventType);
    }

}
