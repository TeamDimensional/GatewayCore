package com.dimensional.gatewaycore;

import com.dimensional.gatewaycore.events.TinkerEvents;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
    dependencies = "required-after:jei;after:roots;after:tconstruct;after:botania;after:essentialcraft;after:gamestages"
)
public class GatewayCore {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public GatewayCore() {
        if (Loader.isModLoaded("tconstruct")) {
            MinecraftForge.EVENT_BUS.register(TinkerEvents.class);
            MaterialRegistry.setup();
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (Loader.isModLoaded("tconstruct") && (GatewayConfig.tinkerOverridesNeeded() || GatewayConfig.createTinkerMats)) {
            LOGGER.info("Creating our Tinker's materials...");
            MaterialRegistry.registerMaterials();
        }
    }

}
