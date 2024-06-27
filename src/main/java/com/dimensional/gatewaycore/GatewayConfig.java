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

    public static boolean tinkerOverridesNeeded() {
        return removeTinkerMats || replaceTinkerMats;
    }

}
