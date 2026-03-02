package com.dimensional.gatewaycore.utils;

import com.google.gson.annotations.JsonAdapter;

import mezz.jei.api.recipe.IIngredientType;

@JsonAdapter(IngredientDeserializer.class)
public class GenericIngredient<T> {
    IIngredientType<T> ingType;
    T ing;

    GenericIngredient(IIngredientType<T> ingType, T ing) {
        this.ingType = ingType;
        this.ing = ing;
    }

    public IIngredientType<T> type() {
        return ingType;
    }

    public T ingredient() {
        return ing;
    }
}
