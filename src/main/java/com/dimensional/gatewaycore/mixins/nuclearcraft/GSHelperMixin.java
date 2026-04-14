package com.dimensional.gatewaycore.mixins.nuclearcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.helper.ingredient.ItemsIngredient;
import com.cleanroommc.groovyscript.helper.ingredient.OrIngredient;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;

import nc.integration.groovyscript.GSHelper;
import nc.recipe.RecipeHelper;
import nc.recipe.ingredient.EmptyItemIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.util.StreamHelper;
import net.minecraft.item.ItemStack;

@Mixin(value = GSHelper.class, remap = false)
public abstract class GSHelperMixin {

    /**
     * @reason Fixes an existing method to support modern versions of Groovyscript
     * @author Wizzerinus
     * @param object The ingredient to convert
     * @return the converted ingredient or a RuntimeException if it's not valid
     */
    @Overwrite
    public static IItemIngredient buildAdditionItemIngredient(Object object) {
		if (object == null || object.equals(IIngredient.EMPTY)) {
			return new EmptyItemIngredient();
		}
		else if (object instanceof IItemIngredient itemIngredient) {
			return itemIngredient;
		}
		else if (object instanceof ItemStack stack) {
			return RecipeHelper.buildItemIngredient(stack);
		}
		else if (object instanceof OreDictIngredient gsOreStack) {
			return new OreIngredient(gsOreStack.getOreDict(), gsOreStack.getAmount());
		}
		else if (object instanceof ItemsIngredient gsItemsIngredient) {
			return RecipeHelper.buildItemIngredient(StreamHelper.map(gsItemsIngredient.getMatchingStacks(), GSHelper::buildAdditionItemIngredient));
		}
		else if (object instanceof OrIngredient gsOrIngredient) {
			return RecipeHelper.buildItemIngredient(StreamHelper.map(gsOrIngredient.getMatchingStacks(), GSHelper::buildAdditionItemIngredient));
		}
		else {
		    throw new IllegalArgumentException(String.format("NuclearCraft: Invalid ingredient: %s, %s", object.getClass().getName(), object));
		}
    }

    /**
     * Because the method is a carbon copy of buildAdditionItemIngredient, we simply redirect to that one.
     * @reason Fixes an existing method to support modern versions of Groovyscript
     * @author Wizzerinus
     * @param object The ingredient to convert
     * @return the converted ingredient or a RuntimeException if it's not valid
     */
    @Overwrite
	public static IItemIngredient buildRemovalItemIngredient(Object object) {
        return buildAdditionItemIngredient(object);
    }
    
}
