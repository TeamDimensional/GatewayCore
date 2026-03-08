package com.dimensional.gatewaycore.mixins.extrautils2;

import java.awt.Color;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.rwtema.extrautils2.crafting.jei.JEIResonatorHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(value = JEIResonatorHandler.ResonatorWrapper.class, remap = false)
public class JEIResonatorHandlerMixin {
    @Shadow
    String energyString;

    @Shadow
    String txtString;

    /**
     * @author Wizzerinus
     * @reason Replaces the Resonator text drawing method to support word wrap.
     */
    @Overwrite
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int stringWidth = minecraft.fontRenderer.getStringWidth(energyString);
        minecraft.fontRenderer.drawSplitString(energyString,
                Math.max(JEIResonatorHandler.slotX0, (recipeWidth - stringWidth) / 2), (18 - 9) / 2,
                JEIResonatorHandler.BETWEEN_DIST,
                Color.gray.getRGB());

        double scaleFactor = 0.75;
        stringWidth = minecraft.fontRenderer.getStringWidth(txtString);
        GlStateManager.pushMatrix();
        GlStateManager.translate(JEIResonatorHandler.slotX0, (18 - 9) / 2 + 18 + 12, 0);
        GlStateManager.scale(scaleFactor, scaleFactor, 1);
        int availableSpace = (int) ((recipeWidth - 2 * JEIResonatorHandler.slotX0) / scaleFactor);
        minecraft.fontRenderer.drawSplitString(txtString,
                Math.max(0, (availableSpace - stringWidth) / 2), 0,
                availableSpace, Color.gray.getRGB());
        GlStateManager.popMatrix();
    }
}
