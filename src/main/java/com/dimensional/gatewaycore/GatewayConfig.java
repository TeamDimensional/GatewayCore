package com.dimensional.gatewaycore;

import net.minecraftforge.common.config.Config;

@Config(modid = "gatewaycore")
public class GatewayConfig {

    @Config.Comment("Should we register custom (non-vanilla) TConstruct materials?")
    @Config.RequiresMcRestart
    public static boolean createTinkerMats = true;

    @Config.Comment("Should all TConstruct materials be removed and replaced with our own materials?")
    @Config.RequiresMcRestart
    public static boolean removeTinkerMats = true;

    @Config.Comment("Should instead only the TConstruct materials we register be replaced with our own materials?")
    @Config.RequiresMcRestart
    public static boolean replaceTinkerMats = false;

    @Config.Comment("Should we show tiers on all items? Tiers are intended to be added through Groovyscript.")
    public static boolean showItemTiers = true;

    @Config.Comment("Should we prevent Block Drops from generating the drops file if possible?")
    @Config.RequiresMcRestart
    public static boolean stopBlockDropsCaching = true;

    @Config.Comment("Should we show fluid tooltips, similar to item tooltips, when Advanced Tooltips (F3+H) is active?")
    public static boolean showFluidTooltips = true;

    @Config.Comment("Should we disable Tinker's Construct from scanning all recipes during Post-Init?")
    public static boolean disableTinkerRecipeScan = true;

    @Config.Comment("Should we disable Aether's ores from generating? Intended to be used with CoFH World or similar.")
    public static boolean disableAetherOres = true;

    @Config.Comment("The TiC materials that should not be deleted, even if we're deleting materials:")
    public static String[] allowedTinkerMaterials = new String[]{
        "string", "vine", "reed", "ice", "feather", "leaf", "slimeleaf_blue", "slimeleaf_orange", "slimeleaf_purple"
    };

    public static boolean tinkerOverridesNeeded() {
        return removeTinkerMats || replaceTinkerMats;
    }

}
