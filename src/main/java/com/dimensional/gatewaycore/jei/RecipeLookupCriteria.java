package com.dimensional.gatewaycore.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IIngredientType;

public class RecipeLookupCriteria {

    private final Map<IIngredientType<?>, List<Object>> requiredInputs = new HashMap<>();
    private final Map<IIngredientType<?>, List<Object>> requiredOutputs = new HashMap<>();
    private final String categoryName;

    public RecipeLookupCriteria(String category) {
        categoryName = category;
    }

    public String getCategory() {
        return categoryName;
    }

    public Object getAnyOutput() {
        for (List<Object> entry : requiredOutputs.values()) {
            for (Object o : entry) {
                return o;
            }
        }
        return null;
    }

    public <T> RecipeLookupCriteria addInput(IIngredientType<T> ingType, T input) {
        List<Object> inputs = requiredInputs.computeIfAbsent(ingType, k -> new ArrayList<>());
        inputs.add(input);
        return this;
    }

    public <T> RecipeLookupCriteria addOutput(IIngredientType<T> ingType, T output) {
        List<Object> outputs = requiredOutputs.computeIfAbsent(ingType, k -> new ArrayList<>());
        outputs.add(output);
        return this;
    }

    private boolean validateAll(List<Object> requiredItems, List<List<Object>> inJEI) {
        for (Object o : requiredItems) {
            IIngredientHelper<Object> helper = Plugin.getIngredientHelper(o);
            for (List<Object> lo : inJEI) {
                if (helper.getMatch(lo, o) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matches(IIngredients ingredients) {
        for (Map.Entry<IIngredientType<?>, List<Object>> inputList : requiredInputs.entrySet()) {
            @SuppressWarnings("unchecked")
            List<List<Object>> inputs = (List<List<Object>>) (Object) ingredients.getInputs(inputList.getKey());
            if (!validateAll(inputList.getValue(), inputs)) {
                return false;
            }
        }
        for (Map.Entry<IIngredientType<?>, List<Object>> outputList : requiredOutputs.entrySet()) {
            @SuppressWarnings("unchecked")
            List<List<Object>> inputs = (List<List<Object>>) (Object) ingredients.getOutputs(outputList.getKey());
            if (!validateAll(outputList.getValue(), inputs)) {
                return false;
            }
        }

        return true;
    }
}
