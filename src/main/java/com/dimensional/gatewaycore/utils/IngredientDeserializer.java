package com.dimensional.gatewaycore.utils;

import java.lang.reflect.Type;

import javax.annotation.Nullable;

import com.dimensional.gatewaycore.GatewayCore;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class IngredientDeserializer implements JsonDeserializer<GenericIngredient<?>> {
    private @Nullable GenericIngredient<?> parseString(String input) {
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
                    return new GenericIngredient<>(VanillaTypes.ITEM, ItemStack.EMPTY);
                }
                return new GenericIngredient<>(VanillaTypes.ITEM, new ItemStack(item, 1, meta));
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
                return new GenericIngredient<>(VanillaTypes.FLUID, new FluidStack(fluid, 1000));
            default:
                return null;
        }
    }

    private static class DeserializeFailure extends Exception {
    }

    private String safeGetString(JsonObject o, String name) throws DeserializeFailure {
        JsonElement type = o.get(name);
        if (type == null || !type.isJsonPrimitive()) {
            GatewayCore.LOGGER.warn(name + " should be a primitive, got {}", type);
            return null;
        }
        JsonPrimitive typePrimitive = type.getAsJsonPrimitive();
        if (!typePrimitive.isString()) {
            GatewayCore.LOGGER.warn(name + " should be a string, got {}", type);
            return null;
        }
        return typePrimitive.getAsString();
    }

    private int getIntOr(JsonObject o, String name, int def) {
        JsonElement type = o.get(name);
        if (type == null || !type.isJsonPrimitive()) {
            return def;
        }
        JsonPrimitive typePrimitive = type.getAsJsonPrimitive();
        if (!typePrimitive.isString()) {
            return def;
        }
        return typePrimitive.getAsInt();
    }

    private @Nullable GenericIngredient<?> parseObject(JsonObject input) throws DeserializeFailure {
        switch (safeGetString(input, "type")) {
            case "item":
                String itemName = safeGetString(input, "item");
                int meta = getIntOr(input, "meta", 0);
                int quantity = getIntOr(input, "count", 1);
                Item item = Item.getByNameOrId(itemName);
                if (item == null) {
                    GatewayCore.LOGGER.warn("Item {} is not registered", itemName);
                    return new GenericIngredient<>(VanillaTypes.ITEM, ItemStack.EMPTY);
                }
                NBTTagCompound nbt = null;
                String nbtContent = "";
                try {
                    nbtContent = safeGetString(input, "nbt");
                    nbt = JsonToNBT.getTagFromJson(nbtContent);
                } catch (NBTException e) {
                    GatewayCore.LOGGER.warn("Unable to parse NBT tag '{}'", nbtContent);
                    return null;
                } catch (DeserializeFailure e) {
                }
                return new GenericIngredient<>(VanillaTypes.ITEM, new ItemStack(item, quantity, meta, nbt));
            case "fluid":
                String fluidName = safeGetString(input, "fluid");
                Fluid fluid = FluidRegistry.getFluid(fluidName);
                if (fluid == null) {
                    GatewayCore.LOGGER.warn("Fluid {} is not registered", fluidName);
                    return null;
                }
                int fluidQuantity = getIntOr(input, "amount", 1000);
                return new GenericIngredient<>(VanillaTypes.FLUID, new FluidStack(fluid, fluidQuantity));
            default:
                return null;
        }
    }

    @Override
    public GenericIngredient<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonPrimitive()) {
            JsonPrimitive pr = json.getAsJsonPrimitive();
            if (pr.isString()) {
                String s = json.getAsString();
                GenericIngredient<?> out = parseString(s);
                if (out == null) {
                    throw new JsonParseException(
                            "Unable to parse " + s + " as string! Check the logs for more information.");
                }
                return out;
            }
            throw new JsonParseException("JSON primitive " + json + " is not of correct type!");
        }

        if (json.isJsonObject()) {
            JsonObject o = json.getAsJsonObject();
            GenericIngredient<?> out = null;
            try {
                out = parseObject(o);
            } catch (DeserializeFailure e) {
            }
            if (out == null) {
                throw new JsonParseException(
                        "Unable to parse " + o + " as object! Check the logs for more information.");
            }
            return out;
        }

        throw new JsonParseException("Invalid JSON type: " + json);
    }

}
