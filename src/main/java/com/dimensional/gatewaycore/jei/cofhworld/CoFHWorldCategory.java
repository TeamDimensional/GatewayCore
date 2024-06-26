package com.dimensional.gatewaycore.jei.cofhworld;

import com.dimensional.gatewaycore.Tags;
import com.dimensional.gatewaycore.jei.Plugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class CoFHWorldCategory implements IRecipeCategory<CoFHWorldRecipe<?>> {

    private final IDrawable background;

    public CoFHWorldCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
            new ResourceLocation("gatewaycore", "textures/gui/cofh_world.png"), 0, 0, 128, 132);
    }

    @Override
    public @Nonnull String getUid() {
        return Plugin.COFH_WORLD;
    }

    @Override
    public @Nonnull String getTitle() {
        return I18n.format("container.gateway.cofh_world.name");
    }

    @Override
    public @Nonnull String getModName() {
        return Tags.MOD_NAME;
    }

    @Override
    public @Nonnull IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, @Nonnull CoFHWorldRecipe<?> recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.addTooltipCallback(recipe);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
        for (int i = 0; i < outputs.size(); i++) {
            int x = 1 + 18 * (i % 7);
            int y = 40 + 18 * (i / 7);
            group.init(i, false, x, y);
            group.set(i, outputs.get(i));
        }
    }
}
