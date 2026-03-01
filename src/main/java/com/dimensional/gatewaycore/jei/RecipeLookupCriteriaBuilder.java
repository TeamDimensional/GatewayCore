package com.dimensional.gatewaycore.jei;

import javax.annotation.Nullable;

import com.dimensional.gatewaycore.GatewayCore;

import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLookupCriteriaBuilder {
    private class RecipeLookupCriteriaPair {
        IIngredientType<?> ingType;
        Object ing;

        RecipeLookupCriteriaPair(IIngredientType<?> ingType, Object ing) {
            this.ingType = ingType;
            this.ing = ing;
        }
    }

    private final RecipeLookupCriteria rlc;

    public RecipeLookupCriteriaBuilder(String category) {
        rlc = new RecipeLookupCriteria(category);
    }

    private @Nullable RecipeLookupCriteriaPair parse(String input) {
        String[] items = input.split(":");
        if (items.length < 2) {
            return null;
        }

        switch (items[0]) {
            case "item":
                if (items.length < 3 || items.length > 4) {
                    GatewayCore.LOGGER.warn("Expected item criteria length 2 or 3, instead got {}", input);
                    return null;
                }
                String modid = items[1], itemId = items[2];
                int meta;
                try {
                    meta = items.length == 4 ? Integer.parseInt(items[3]) : 0;
                } catch (NumberFormatException e) {
                    GatewayCore.LOGGER.warn("Unable to parse metadata number {}", items[3]);
                    return null;
                }
                Item item = Item.getByNameOrId(modid + ":" + itemId);
                if (item == null) {
                    GatewayCore.LOGGER.warn("Item {}:{} is not registered", modid, itemId);
                    return null;
                }
                return new RecipeLookupCriteriaPair(VanillaTypes.ITEM, new ItemStack(item, 1, meta));
            case "fluid":
                if (items.length > 2) {
                    GatewayCore.LOGGER.warn("Expected fluid criteria length 1, instead got {}", input);
                    return null;
                }
                Fluid fluid = FluidRegistry.getFluid(items[1]);
                if (fluid == null) {
                    GatewayCore.LOGGER.warn("Fluid {} is not registered", items[1]);
                    return null;
                }
                return new RecipeLookupCriteriaPair(VanillaTypes.FLUID, new FluidStack(fluid, 1000));
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    public RecipeLookupCriteriaBuilder addInput(String input) {
        RecipeLookupCriteriaPair pair = parse(input);
        if (pair == null) {
            GatewayCore.LOGGER.warn("Unable to parse a recipe input: " + input);
        } else {
            rlc.addInput((IIngredientType<Object>) pair.ingType, pair.ing);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public RecipeLookupCriteriaBuilder addOutput(String output) {
        RecipeLookupCriteriaPair pair = parse(output);
        if (pair == null) {
            GatewayCore.LOGGER.warn("Unable to parse a recipe output: " + output);
        } else {
            rlc.addOutput((IIngredientType<Object>) pair.ingType, pair.ing);
        }
        return this;
    }

    public RecipeLookupCriteria build() {
        return rlc;
    }
}
