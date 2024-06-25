package com.dimensional.gatewaycore.mixins.tconstruct;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.gui.book.GuiBook;
import slimeknights.mantle.client.gui.book.element.BookElement;
import slimeknights.tconstruct.library.book.TinkerPage;
import slimeknights.tconstruct.library.book.content.ContentMaterial;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Mixin(value = ContentMaterial.class, remap = false)
public class ContentMaterialMixin extends TinkerPage {

    @Shadow
    private transient Material material;

    @Shadow
    private void addDisplayItems(ArrayList<BookElement> list, int x) {}

    @Shadow
    private void addStatsDisplay(int x, int y, int w, ArrayList<BookElement> list,
                                 LinkedHashSet<ITrait> allTraits, String stattype) {}

    /**
     * @author Wizzerinus
     * @reason Properly place blocks on the page if less than 3 types of material exist. Note: very massive blow
     *  on the method because I do not know how to inject this in a simpler way.
     */
    @Overwrite
    public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {

        addTitle(list, material.getLocalizedNameColored(), true);

        // the cool tools to the left/right
        addDisplayItems(list, rightSide ? GuiBook.PAGE_WIDTH - 18 : 0);

        int col_margin = 22;
        int top = 15;
        int left = rightSide ? 0 : col_margin;

        int y = top + 10;
        int x = left + 10;
        int w = GuiBook.PAGE_WIDTH / 2 - 15;

        LinkedHashSet<ITrait> allTraits = new LinkedHashSet<>();
        final int[] xPositions = {x, x + w + 5};
        final Map<String, Integer> lineCounts = new HashMap<>();
        lineCounts.put(MaterialTypes.HEAD, 4);
        lineCounts.put(MaterialTypes.HANDLE, 2);
        lineCounts.put(MaterialTypes.EXTRA, 1);

        String[] mTypes = {MaterialTypes.HEAD, MaterialTypes.HANDLE, MaterialTypes.EXTRA};
        int[] ySkips = {0, 0};
        int i = 0;
        for (String mType : mTypes) {
            if (!material.hasStats(mType)) continue;

            addStatsDisplay(xPositions[i % 2], y + ySkips[i % 2], w, list, allTraits, mType);
            ySkips[i % 2] += 25 + 10 * (lineCounts.get(mType) + material.getAllTraitsForStats(mType).size());
            i++;
        }

        // I've tested inspirational notes but they don't seem to work properly :(

    }

}
