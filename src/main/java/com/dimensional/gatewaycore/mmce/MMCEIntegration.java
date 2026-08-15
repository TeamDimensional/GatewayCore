package com.dimensional.gatewaycore.mmce;

import github.kasuminova.mmce.common.machine.component.MachineComponentProxyRegistry;
import net.minecraftforge.fml.common.Loader;

public class MMCEIntegration {
    public static void initialize() {
        if (Loader.isModLoaded("actuallyadditions")) {
            MachineComponentProxyRegistry.INSTANCE.register("LeafGenerator", LeafGeneratorProxy.INSTANCE);
        }
    }
}
