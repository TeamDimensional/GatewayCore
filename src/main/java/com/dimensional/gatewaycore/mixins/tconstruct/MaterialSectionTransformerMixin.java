package com.dimensional.gatewaycore.mixins.tconstruct;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.book.sectiontransformer.MaterialSectionTransformer;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;

@Mixin(value = MaterialSectionTransformer.class, remap = false)
public class MaterialSectionTransformerMixin {
    /**
     * @author Wizzerinus
     * @reason Show materials without the Head part
     */
    @Overwrite
    protected boolean isValidMaterial(Material material) {
        return material.hasStats(MaterialTypes.HEAD)    ||
               material.hasStats(MaterialTypes.EXTRA)   ||
               material.hasStats(MaterialTypes.HANDLE);
    }
}
