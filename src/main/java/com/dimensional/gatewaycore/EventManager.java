package com.dimensional.gatewaycore;

import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.MaterialEvent;

@Mod.EventBusSubscriber
public class EventManager {

    @SubscribeEvent
    public static void registerTinkerMaterial(MaterialEvent.MaterialRegisterEvent e) {

        String id = e.material.identifier;
        if (MaterialRegistry.materialAllowed(id)) {
            return;
        }

        if (GatewayConfig.removeTinkerMats ||
            (GatewayConfig.replaceTinkerMats && MaterialRegistry.materialExists(id))) {
            GatewayCore.LOGGER.info("Disabling Tinker's Construct material: {}", id);
            e.setCanceled(true);
        }

    }

}