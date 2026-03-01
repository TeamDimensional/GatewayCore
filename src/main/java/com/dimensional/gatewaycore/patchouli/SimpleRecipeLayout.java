package com.dimensional.gatewaycore.patchouli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimensional.gatewaycore.jei.Plugin;

import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.ingredients.GuiFluidStackGroup;
import mezz.jei.gui.ingredients.GuiIngredientGroup;
import mezz.jei.gui.ingredients.GuiItemStackGroup;

public class SimpleRecipeLayout implements IRecipeLayout {

    private final Map<IIngredientType<?>, GuiIngredientGroup<?>> groups = new HashMap<>();
    private final GuiItemStackGroup guiItemStackGroup = new GuiItemStackGroup(null, 0);
    private final GuiFluidStackGroup guiFluidStackGroup = new GuiFluidStackGroup(null, 0);
    private final IRecipeCategory<IRecipeWrapper> category;

    @SuppressWarnings("unchecked")
    public SimpleRecipeLayout(IRecipeCategory<?> category) {
        this.category = (IRecipeCategory<IRecipeWrapper>) category;
    }

    @Override
    public IFocus<?> getFocus() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IGuiIngredientGroup<T> getIngredientsGroup(IIngredientType<T> arg0) {
        IGuiIngredientGroup<Object> out = (IGuiIngredientGroup<Object>) groups.computeIfAbsent(arg0,
                k -> new GuiIngredientGroup<>(k, null, 0));
        return (IGuiIngredientGroup<T>) out;
    }

    @Override
    public <T> IGuiIngredientGroup<T> getIngredientsGroup(Class<T> clazz) {
        IIngredientRegistry ingredientRegistry = Plugin.getIngredientRegistry();
        IIngredientType<T> ingredientType = ingredientRegistry.getIngredientType(clazz);
        return getIngredientsGroup(ingredientType);
    }

    @Override
    public IGuiItemStackGroup getItemStacks() {
        return guiItemStackGroup;
    }

    @Override
    public IGuiFluidStackGroup getFluidStacks() {
        return guiFluidStackGroup;
    }

    @Override
    public IRecipeCategory<?> getRecipeCategory() {
        return category;
    }

    @Override
    public void setRecipeTransferButton(int arg0, int arg1) {
    }

    @Override
    public void setShapeless() {
    }

    public List<GuiIngredientGroup<?>> getGroups() {
        List<GuiIngredientGroup<?>> groups = new ArrayList<>();
        groups.addAll(this.groups.values());
        groups.add(guiItemStackGroup);
        groups.add(guiFluidStackGroup);
        return groups;
    }

}
