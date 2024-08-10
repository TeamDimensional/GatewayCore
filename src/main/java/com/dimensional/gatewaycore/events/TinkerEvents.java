package com.dimensional.gatewaycore.events;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.GatewayCore;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.MaterialEvent;

public class TinkerEvents {

    @SubscribeEvent
    public static void registerTinkerMaterial(MaterialEvent.MaterialRegisterEvent e) {

        String id = e.material.identifier;
        if (MaterialRegistry.materialAllowed(id)) {
            return;
        }

        if (GatewayConfig.tConstruct.removeAllMaterials ||
            (GatewayConfig.tConstruct.replaceMaterials && MaterialRegistry.materialExists(id))) {
            GatewayCore.LOGGER.info("Disabling Tinker's Construct material: {}", id);
            e.setCanceled(true);
        }

    }

}