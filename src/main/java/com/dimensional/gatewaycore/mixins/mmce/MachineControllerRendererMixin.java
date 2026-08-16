package com.dimensional.gatewaycore.mixins.mmce;

import com.dimensional.gatewaycore.mmce.MMCEUtils;
import github.kasuminova.mmce.client.renderer.MachineControllerRenderer;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.tileentity.TileEntity;

@Mixin(value = MachineControllerRenderer.class, remap = false)
public class MachineControllerRendererMixin {

    // This does not work because the signature of modded method is different from vanilla, so remap fails
    // @Inject(method = "render(Lhellfirepvp/modularmachinery/common/tiles/base/TileMultiblockMachineController;DDDFIF)V", at = @At("HEAD"))
    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At("HEAD"), remap = true)
    private void inject(/* TileMultiblockMachineController */ TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        // Should be safe due to generic instantiation on MachineControllerRenderer
        TileMultiblockMachineController ctrl = (TileMultiblockMachineController) tile;
        String name = ctrl.getFormedMachineName();
        if (name != null) {
            String[] parts = name.split(":");
            if (parts.length > 0) {
                MMCEUtils.IMultiblockRenderFunction func = MMCEUtils.renderFunctions.get(parts[parts.length - 1]);
                if (func != null) {
                    func.render(ctrl, x, y, z);
                }
            }
        }
    }

}
