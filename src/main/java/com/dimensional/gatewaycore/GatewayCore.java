package com.dimensional.gatewaycore;

import com.dimensional.gatewaycore.events.TinkerEvents;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
    dependencies = "before:jei;after:cofhworld"
)
public class GatewayCore {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public GatewayCore() {
        if (Loader.isModLoaded("tconstruct")) {
            MinecraftForge.EVENT_BUS.register(TinkerEvents.class);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (Loader.isModLoaded("tconstruct") && (GatewayConfig.tConstruct.tinkerOverridesNeeded() || GatewayConfig.tConstruct.createCustomMaterials)) {
            LOGGER.info("Creating our Tinker's materials...");
            MaterialRegistry.setup();
            MaterialRegistry.registerMaterials();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (Loader.isModLoaded("tconstruct") && (GatewayConfig.tConstruct.tinkerOverridesNeeded() || GatewayConfig.tConstruct.createCustomMaterials)) {
            LOGGER.info("Post-initializing TiC...");
            MaterialRegistry.postInit();
        }
    }

}
