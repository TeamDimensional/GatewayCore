package com.dimensional.gatewaycore;

import com.dimensional.gatewaycore.tinker.MaterialRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
    dependencies = "required-after:jei;after:roots;after:tconstruct"
)
public class GatewayCore {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public GatewayCore() {
        MaterialRegistry.setup();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (Loader.isModLoaded("tconstruct") && (GatewayConfig.tinkerOverridesNeeded() || GatewayConfig.createTinkerMats)) {
            LOGGER.info("Creating our Tinker's materials...");
            MaterialRegistry.registerMaterials();
        }
    }

}
