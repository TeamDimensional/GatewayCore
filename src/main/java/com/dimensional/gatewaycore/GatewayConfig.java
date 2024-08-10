package com.dimensional.gatewaycore;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, category = "")
public class GatewayConfig {

    @Config.Comment("Vanilla tweaks")
    public static ConfigVanilla vanilla = new ConfigVanilla();

    @Config.Comment("Tinker's Construct tweaks")
    public static ConfigTConstruct tConstruct = new ConfigTConstruct();

    @Config.Comment("Tweaks to various mods")
    public static ConfigMiscMods mods = new ConfigMiscMods();

    public static class ConfigVanilla {
        @Config.Comment("Should we show tiers on all items? Tiers are intended to be added through Groovyscript.")
        public boolean showItemTiers = true;

        @Config.Comment("Should we show fluid tooltips, similar to item tooltips, when Advanced Tooltips (F3+H) is active?")
        public boolean showFluidTooltips = true;
    }

    public static class ConfigTConstruct {
        @Config.Comment("Should we register custom (non-vanilla) TConstruct materials?")
        @Config.RequiresMcRestart
        public boolean createCustomMaterials = true;

        @Config.Comment("Should all TConstruct materials be removed and replaced with our own materials?")
        @Config.RequiresMcRestart
        public boolean removeAllMaterials = true;

        @Config.Comment("Should instead only the TConstruct materials we register be replaced with our own materials?")
        @Config.RequiresMcRestart
        public boolean replaceMaterials = false;

        @Config.Comment("The TiC materials that should not be deleted, even if we're deleting materials:")
        public String[] allowedMaterials = new String[]{
            "string", "vine", "reed", "ice", "feather", "leaf", "slimeleaf_blue", "slimeleaf_orange", "slimeleaf_purple"
        };

        @Config.Comment("Should we disable Tinker's Construct from scanning all recipes during Post-Init?")
        public boolean disableRecipeScan = true;

        public boolean tinkerOverridesNeeded() {
            return removeAllMaterials || replaceMaterials;
        }
    }

    public static class ConfigMiscMods {
        @Config.Comment("Should we prevent Block Drops from generating the drops file if possible?")
        @Config.RequiresMcRestart
        public boolean stopBlockDropsCaching = true;

        @Config.Comment("Should we disable Aether's ores from generating? Intended to be used with CoFH World or similar.")
        public boolean disableAetherOres = true;
    }

    @Mod.EventBusSubscriber(modid = Tags.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Tags.MOD_ID)) {
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
