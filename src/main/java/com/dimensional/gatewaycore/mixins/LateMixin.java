package com.dimensional.gatewaycore.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.stream.Collectors;

public class LateMixin implements ILateMixinLoader {

    public static final List<String> modMixins = ImmutableList.of(
        "tconstruct",
        "cofhworld",
        "blockdrops",
        "jei",
        "aether_legacy",
        "essentialcraft",
        "calculator",
        "nuclearcraft"
    );

    @Override
    public List<String> getMixinConfigs() {
        return modMixins.stream()
            .filter(Loader::isModLoaded)
            .map(mod -> "mixins/mixins.gatewaycore." + mod + ".json")
            .collect(Collectors.toList());
    }
}
