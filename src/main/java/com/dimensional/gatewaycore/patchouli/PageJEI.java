package com.dimensional.gatewaycore.patchouli;

import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageJEI extends PageWithText {
    private List<JEIRecipeRenderer> recipes;
    private String title = "";

    private transient int textOffset;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        int offset = title.isEmpty() ? 0 : 16;
        for (JEIRecipeRenderer r : recipes) {
            r.setYOffset(offset);
            r.load();
            offset += r.getHeight() + 10;
        }

        textOffset = offset;
        super.onDisplayed(parent, left, top);
    }

    @Override
    public void render(int mouseX, int mouseY, float pticks) {
        super.render(mouseX, mouseY, pticks);
        GlStateManager.color(1, 1, 1, 1);
        if (!title.isEmpty()) {
            parent.drawCenteredStringNoShadow(title, GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        }

        for (JEIRecipeRenderer r : recipes) {
            r.render(this, mouseX, mouseY);
        }
    }

    @Override
    public int getTextHeight() {
        return textOffset;
    }

}
