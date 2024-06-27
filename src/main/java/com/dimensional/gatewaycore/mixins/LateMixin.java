package com.dimensional.gatewaycore.mixins;

import com.google.common.collect.ImmutableList;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.stream.Collectors;

public class LateMixin implements ILateMixinLoader {

    public static final List<String> modMixins = ImmutableList.of(
        "tinkers",
        "cofhworld",
        "blockdrops"
    );

    @Override
    public List<String> getMixinConfigs() {
        return modMixins.stream().map(mod -> "mixins/mixins.gatewaycore." + mod + ".json").collect(Collectors.toList());
    }
}
