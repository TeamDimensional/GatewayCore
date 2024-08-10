package com.dimensional.gatewaycore.tinker.modded;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.tinker.GatewayMaterial;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.tools.TinkerTraits;

public class BotaniaMaterials {

    private static Material livingwood, livingrock;

    public static void addMaterials() {
        // Tier 1
        livingwood = new GatewayMaterial("livingwood", 0x702808).setCraftable(true);
        livingwood.addItem("livingwood", 1, Material.VALUE_Ingot);
        livingwood.setRepresentativeItem("livingwood");
        livingwood.addTrait(MaterialRegistry.naturalSelection, MaterialTypes.HANDLE);
        livingwood.addTrait(MaterialRegistry.ecological2, MaterialTypes.EXTRA);
        TinkerRegistry.addMaterialStats(livingwood,
            new ExtraMaterialStats(30),
            new HandleMaterialStats(0.6f, 30));
        MaterialRegistry.register(livingwood);

        livingrock = new GatewayMaterial("livingrock", 0xd7dac7).setCraftable(true);
        livingrock.addItem("livingrock", 1, Material.VALUE_Ingot);
        livingrock.setRepresentativeItem("livingrock");
        livingrock.addTrait(TinkerTraits.stonebound, MaterialTypes.HEAD);
        livingrock.addTrait(MaterialRegistry.ecological, MaterialTypes.EXTRA);
        TinkerRegistry.addMaterialStats(livingrock,
            new HeadMaterialStats(150, 4.5f, 2.5f, 0),
            new ExtraMaterialStats(30));
        MaterialRegistry.register(livingrock);
    }

    public static void registerMaterials() {
        if (GatewayConfig.tConstruct.createCustomMaterials) {
            TinkerRegistry.addMaterial(livingwood);
            TinkerRegistry.addMaterial(livingrock);
        }
    }

}
