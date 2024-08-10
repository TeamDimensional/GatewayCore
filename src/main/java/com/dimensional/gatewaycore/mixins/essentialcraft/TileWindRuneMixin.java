package com.dimensional.gatewaycore.mixins.essentialcraft;

import DummyCore.Utils.MathUtils;
import com.dimensional.gatewaycore.GatewayConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import essentialcraft.api.IESPEHandler;
import essentialcraft.api.WindImbueRecipe;
import essentialcraft.common.capabilities.espe.CapabilityESPEHandler;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileWindRune;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileWindRune.class, remap = false)
public class TileWindRuneMixin extends TileEntity {

    @Shadow
    protected static Vec3i[] coords;

    @WrapOperation(method = "action", at = @At(value = "INVOKE", target = "Lessentialcraft/common/tile/TileWindRune;getEnderstarEnergy()I"))
    private int extractESPE(TileWindRune instance, Operation<Integer> original, @Local(argsOnly = true) EntityPlayer player) {
        int value = original.call(instance);

        if (GatewayConfig.mods.windRuneTax == 0 || value == 0) return value;

        // Only pay the taxes if the recipe cannot be currently executed
        ItemStack item = player.getHeldItemMainhand();
        WindImbueRecipe rec = WindImbueRecipe.getRecipeByInput(item);
        if (rec == null) return value;
        boolean hasEnergy = value >= rec.enderEnergy;
        if (hasEnergy) return value;

        double rightClickCost = GatewayConfig.mods.windRuneTax;
        for (Vec3i coord : coords) {
            TileEntity tile = getWorld().getTileEntity(pos.add(coord));
            if(tile == null) {
                continue;
            }
            if(tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
                IESPEHandler handler = tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
                assert handler != null;
                rightClickCost -= handler.extractESPE(rightClickCost, !player.world.isRemote);
                if (rightClickCost < 0.01) break;
            }
        }


        if(player.world.isRemote) {
            for(int j = 0; j < 300; ++j) {
                EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, 0, getWorld().rand.nextDouble()*0.3D, 0, 1, 0.2D, 0.6D, 0.85D);
            }
        }

        return Math.max(0, value - GatewayConfig.mods.windRuneTax);
    }

    @Inject(method = "action", at = @At("TAIL"), cancellable = true)
    private void clickSuccessful(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (!player.getHeldItemMainhand().isEmpty()) {
            ItemStack item = player.getHeldItemMainhand();
            WindImbueRecipe rec = WindImbueRecipe.getRecipeByInput(item);
            if (rec != null) cir.setReturnValue(true);
        }
    }

}
