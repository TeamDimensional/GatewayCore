package com.dimensional.gatewaycore;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import com.dimensional.gatewaycore.events.TinkerEvents;
import com.dimensional.gatewaycore.tinker.MaterialRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = "before:jei;after:cofhworld")
public class GatewayCore implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.EventHandler
    public void prePreInit(FMLConstructionEvent e) {
        if (Loader.isModLoaded("tconstruct")) {
            MinecraftForge.EVENT_BUS.register(TinkerEvents.class);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (Loader.isModLoaded("tconstruct") && (GatewayConfig.tConstruct.tinkerOverridesNeeded()
                || GatewayConfig.tConstruct.createCustomMaterials)) {
            LOGGER.info("Creating our Tinker's materials...");
            MaterialRegistry.setup();
            MaterialRegistry.registerMaterials();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (Loader.isModLoaded("tconstruct") && (GatewayConfig.tConstruct.tinkerOverridesNeeded()
                || GatewayConfig.tConstruct.createCustomMaterials)) {
            LOGGER.info("Post-initializing TiC...");
            MaterialRegistry.postInit();
        }
    }

    @Override
    public List<String> getMixinConfigs() {
        return ImmutableList.of("mixins/mixins.gatewaycore.vanilla.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
