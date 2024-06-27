package com.dimensional.gatewaycore.jei.immersive;

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

 import javax.annotation.Nonnull;
 import java.util.List;

public class MultiblockRecipeCategory implements IRecipeCategory<MultiblockRecipe>
 {
     private final IDrawable background;
    
     public MultiblockRecipeCategory(IGuiHelper helper) {
         this.background = helper.createBlankDrawable(176, 108);
     }

     @Override
     public @Nonnull String getUid() {
         return Plugin.IE_MULTIBLOCK;
     }

     @Override
     public @Nonnull String getTitle() {
         return I18n.format("container.gateway.ie_multiblock.name");
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
     public void setRecipe(IRecipeLayout layout, @Nonnull MultiblockRecipe multiblock, IIngredients ingredients) {
         IGuiItemStackGroup group = layout.getItemStacks();

         List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
         int i = 0;
         for (List<ItemStack> input : inputs) {
             group.init(i + 1, true, 154 - 18 * (i / 5), 2 + 18 * (i % 5));
             group.set(i + 1, input);
             i++;
         }
     }
 }
