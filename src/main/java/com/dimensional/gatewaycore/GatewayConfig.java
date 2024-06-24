package com.dimensional.gatewaycore;

import net.minecraftforge.common.config.Config;

@Config(modid = "gatewaycore")
public class GatewayConfig {

    @Config.Comment("Should we register custom (non-vanilla) TConstruct materials?")
    @Config.RequiresMcRestart
    public static boolean createTinkerMats = true;

    @Config.Comment("Should all TConstruct materials be removed and replaced with our own materials?")
    @Config.RequiresMcRestart
    public static boolean removeTinkerMats = false;

    @Config.Comment("Should instead only the TConstruct materials we register be replaced with our own materials?")
    @Config.RequiresMcRestart
    public static boolean replaceTinkerMats = false;

    public static boolean tinkerOverridesNeeded() {
        return removeTinkerMats || replaceTinkerMats;
    }

}
