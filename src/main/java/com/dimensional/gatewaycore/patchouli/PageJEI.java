package com.dimensional.gatewaycore.patchouli;

import java.util.List;

import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

public class PageJEI extends BookPage {
    private List<JEIRecipeRenderer> recipes;
    private String title = "";

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        int offset = title.isEmpty() ? 0 : 16;
        for (JEIRecipeRenderer r : recipes) {
            r.setYOffset(offset);
            r.load();
            offset += r.getHeight() + 10;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float pticks) {
        if (!title.isEmpty()) {
            parent.drawCenteredStringNoShadow(title, GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        }

        for (JEIRecipeRenderer r : recipes) {
            r.render(this, mouseX, mouseY);
        }
    }

}
