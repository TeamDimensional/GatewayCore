package com.dimensional.gatewaycore.patchouli;

import java.util.ArrayList;
import java.util.List;

import com.dimensional.gatewaycore.jei.Plugin;
import com.dimensional.gatewaycore.utils.GenericIngredient;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageMultiItem extends PageWithText {

    private String title = "";
    private List<GenericIngredient<?>> items = new ArrayList<>();

    public static final int ITEMS_PER_ROW = 6;
    public static final int PAGE_WIDTH = 110;
    public static final int ITEM_WIDTH = 16;

    @Override
    public void render(int mouseX, int mouseY, float pticks) {
        super.render(mouseX, mouseY, pticks);
        GlStateManager.color(1, 1, 1, 1);
        if (!title.isEmpty()) {
            parent.drawCenteredStringNoShadow(title, GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        }

        int baseOffset = title.isEmpty() ? 0 : 16;
        for (int i = 0; i < items.size(); i += ITEMS_PER_ROW) {
            int rowSize = Math.min(ITEMS_PER_ROW, items.size() - i);
            for (int j = 0; j < rowSize && j < ITEMS_PER_ROW; j++) {
                int x = j * ITEM_WIDTH + (PAGE_WIDTH - rowSize * ITEM_WIDTH) / 2,
                        y = baseOffset + i / ITEMS_PER_ROW * ITEM_WIDTH;

                @SuppressWarnings("unchecked")
                GenericIngredient<Object> item = (GenericIngredient<Object>) items.get(i + j);
                IIngredientRenderer<Object> renderer = Plugin.getIngredientRegistry()
                        .getIngredientRenderer(item.type());
                renderer.render(mc, x, y, item.ingredient());

                if (parent.isAreaHovered(mouseX, mouseY, x, y, ITEM_WIDTH, ITEM_WIDTH)) {
                    ITooltipFlag.TooltipFlags tooltipFlag = mc.gameSettings.advancedItemTooltips
                            ? TooltipFlags.ADVANCED
                            : TooltipFlags.NORMAL;
                    parent.setTooltip(renderer.getTooltip(mc, item.ingredient(), tooltipFlag));
                }
            }
        }
    }

    @Override
    public int getTextHeight() {
        return getBaseOffset() + (title.isEmpty() ? 0 : 16);
    }

    private int getBaseOffset() {
        if (items.isEmpty()) {
            return 0;
        } else {
            return (int) (Math.ceil((double) items.size() / ITEMS_PER_ROW)) * ITEM_WIDTH + 10;
        }
    }

}
