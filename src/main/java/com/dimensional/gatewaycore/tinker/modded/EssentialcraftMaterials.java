package com.dimensional.gatewaycore.tinker.modded;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.tinker.GatewayMaterial;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;

public class EssentialcraftMaterials {

    private static Material elementalBlock;

    public static void addMaterials() {
        /* Tier 1 */
        // Elemental Block ("weakened elemental tools")
        elementalBlock = new GatewayMaterial("elemental_block", 0xcf83ee).setCraftable(true);
        elementalBlock.addItem("blockElemental", 1, Material.VALUE_Ingot);
        elementalBlock.setRepresentativeItem("blockElemental");
        elementalBlock.addTrait(MaterialRegistry.activation, MaterialTypes.HEAD);
        elementalBlock.addTrait(MaterialRegistry.slowdown, MaterialTypes.HANDLE);
        TinkerRegistry.addMaterialStats(elementalBlock,
            new HeadMaterialStats(700, 5.0f, 1.0f, 0),
            new HandleMaterialStats(2.1f, 120));
        MaterialRegistry.register(elementalBlock);
    }

    public static void registerMaterials() {
        if (GatewayConfig.tConstruct.createCustomMaterials) {
            TinkerRegistry.addMaterial(elementalBlock);
        }
    }

}
