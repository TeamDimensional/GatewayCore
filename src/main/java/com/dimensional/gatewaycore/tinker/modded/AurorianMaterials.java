package com.dimensional.gatewaycore.tinker.modded;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.tinker.GatewayMaterial;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import com.shiroroku.theaurorian.Registry.BlockRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

import com.shiroroku.theaurorian.Compat.TinkersConstruct.TinkersConstructCompat;

public class AurorianMaterials {

    // Tier 2
    private static Material silentwood, aurorianStone, crystal;

    public static void addMaterials() {
        // Tier 2
        silentwood = new GatewayMaterial("silentwood", 0xa5b3d0).setCraftable(true);
        silentwood.addTrait(MaterialRegistry.otherworldly);
        TinkerRegistry.addMaterialStats(silentwood,
            new HandleMaterialStats(0.9f, -10),
            new ExtraMaterialStats(0));
        MaterialRegistry.register(silentwood);

        aurorianStone = new GatewayMaterial("aurorianstone", 0x455a76).setCraftable(true);
        aurorianStone.addTrait(MaterialRegistry.cheap, MaterialTypes.EXTRA);
        aurorianStone.addTrait(TinkersConstructCompat.traitaurorianempowered, MaterialTypes.HEAD);
        TinkerRegistry.addMaterialStats(aurorianStone,
            new HeadMaterialStats(160, 4.8f, 1.5f, 0),
            new ExtraMaterialStats(30));
        MaterialRegistry.register(aurorianStone);

        crystal = new GatewayMaterial("crystal", 0xd188d1).setCraftable(true);
        crystal.addTrait(MaterialRegistry.moonlit);
        TinkerRegistry.addMaterialStats(crystal,
            new HeadMaterialStats(400, 5.5f, 2f, 1),
            new ExtraMaterialStats(80));
        MaterialRegistry.register(crystal);
    }

    public static void registerMaterials() {

        if (GatewayConfig.tinkerOverridesNeeded()) {
            TinkerRegistry.addMaterial(silentwood);
            TinkerRegistry.addMaterial(aurorianStone);
        }

        if (GatewayConfig.createTinkerMats) {
            TinkerRegistry.addMaterial(crystal);
        }
    }

    public static void postInit() {
        silentwood.addItem(BlockRegistry.Registry.SILENTWOODLOG.getBlock(), Material.VALUE_Ingot);
        silentwood.setRepresentativeItem(BlockRegistry.Registry.SILENTWOODLOG.getBlock());
        aurorianStone.addItem(BlockRegistry.Registry.AURORIANCOBBLESTONE.getBlock(), Material.VALUE_Ingot);
        aurorianStone.setRepresentativeItem(BlockRegistry.Registry.AURORIANCOBBLESTONE.getBlock());
        crystal.addItem(BlockRegistry.Registry.CRYSTAL.getBlock(), Material.VALUE_Ingot);
        crystal.setRepresentativeItem(BlockRegistry.Registry.CRYSTAL.getBlock());
    }

}
