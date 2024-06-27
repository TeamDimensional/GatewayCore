package com.dimensional.gatewaycore.mixins.blockdrops;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.GatewayCore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mrriegel.blockdrops.BlockDrops;
import mrriegel.blockdrops.Wrapper;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Mixin(value = BlockDrops.class, remap = false)
public abstract class BlockDropsMixin {
    @Shadow
    private File recipeWrapFile;

    @Shadow
    public static Gson gson;

    @Shadow
    public static List<Wrapper> recipeWrappers;

    @Inject(method = "postInit", at = @At("HEAD"), cancellable = true)
    public void onPostInit(FMLPostInitializationEvent event, CallbackInfo ci) throws IOException {
        if (!GatewayConfig.stopBlockDropsCaching || !recipeWrapFile.exists())
            return;

        recipeWrappers = gson.fromJson(
            new BufferedReader(new FileReader(recipeWrapFile)), new TypeToken<List<Wrapper>>() {}.getType());
        if (recipeWrappers == null)
            return;

        GatewayCore.LOGGER.info("Prevented Block Drops from caching the drops again. Yay!");
        ci.cancel();
    }
}
