package com.dimensional.gatewaycore.tinker.modded;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.tinker.GatewayMaterial;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraft.block.Block;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

public class NaturesAuraMaterials {
    private static Material ancientWood;

    public static void addMaterials() {
        // Tier 2
        ancientWood = new GatewayMaterial("ancientwood", 0xa36889).setCraftable(true);
        ancientWood.addTrait(MaterialRegistry.naturalSelection, MaterialTypes.HANDLE);
        ancientWood.addTrait(MaterialRegistry.lightweight, MaterialTypes.EXTRA);
        TinkerRegistry.addMaterialStats(ancientWood,
            new ExtraMaterialStats(0),
            new HandleMaterialStats(1.0f, 25));
        MaterialRegistry.register(ancientWood);
    }

    public static void registerMaterials() {
        if (GatewayConfig.tConstruct.createCustomMaterials) {
            TinkerRegistry.addMaterial(ancientWood);
        }
    }

    public static void postInit() {
        Block b = Block.getBlockFromName("naturesaura:ancient_log");
        ancientWood.addItem(b, Material.VALUE_Ingot);
        Block b2 = Block.getBlockFromName("naturesaura:ancient_bark");
        ancientWood.addItem(b2, Material.VALUE_Ingot);
        ancientWood.setRepresentativeItem(b);
    }
}
