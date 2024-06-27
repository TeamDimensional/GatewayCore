package com.dimensional.gatewaycore.jei;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class DrawableScaled implements IDrawable {

    private final IDrawable drawable;
    private final float scale;

    public DrawableScaled(IDrawable d, float s) {
        drawable = d;
        scale = s;
    }

    @Override
    public int getWidth() {
        return (int)(scale * drawable.getWidth());
    }

    @Override
    public int getHeight() {
        return (int)(scale * drawable.getHeight());
    }

    @Override
    public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        drawable.draw(minecraft, (int)(xOffset / scale), (int)(yOffset / scale));
        GlStateManager.popMatrix();
    }
}
