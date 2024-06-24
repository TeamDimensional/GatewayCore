package com.dimensional.gatewaycore.tinker;

import com.dimensional.gatewaycore.GatewayConfig;
import com.dimensional.gatewaycore.GatewayCore;
import com.dimensional.gatewaycore.tinker.traits.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.tools.TinkerTraits;
import slimeknights.tconstruct.tools.traits.TraitBonusSpeed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaterialRegistry {

    public static final StackableTrait ecological = new StackableEcological();
    public static final StackableTrait ecological2 = new StackableEcological(2);
    public static final StackableTrait crude = new StackableCrude();
    public static final StackableTrait crude2 = new StackableCrude(2);
    public static final AbstractTrait stonebreaker = new TraitStonebreaker();
    public static final AbstractTrait strike = new TraitStrike();
    public static final AbstractTrait activation = new TraitActivation();
    public static final AbstractTrait slowdown = new TraitBonusSpeed("slowdown", -2.4f);
    public static final AbstractTrait naturalSelection = new TraitNaturalSelection();

    private static final Map<String, Material> customMaterials = new HashMap<>();
    private static final Set<String> allowedMaterials = new HashSet<>();

    private static Material wood, flint, netherrack, bone, elementalBlock, livingrock, livingwood;

    public static boolean materialExists(String mat) {
        return customMaterials.containsKey(mat);
    }

    public static boolean materialAllowed(String id) {
        if (!GatewayConfig.tinkerOverridesNeeded()) return true;
        if (GatewayConfig.replaceTinkerMats) return !customMaterials.containsKey(id);
        // we have removeTinkerMats in here
        return id.startsWith("_internal") || id.startsWith("gateway_") || allowedMaterials.contains(id);
    }

    public static void register(Material material) {
        String id = material.identifier;
        if (!id.startsWith("gateway_")) {
            GatewayCore.LOGGER.error("Skipping material registration due to invalid ID: {}", id);
            return;
        }
        String newName = id.replace("gateway_", "");
        GatewayCore.LOGGER.info("Registering material: {}", newName);
        customMaterials.put(newName, material);
    }

    public static void createMaterials() {

        /* Tier 1 materials */
        // Wood
        wood = new GatewayMaterial("wood", 0x8e661b).setCraftable(true);
        wood.addItem("stickWood", 1, Material.VALUE_Shard);
        wood.addItem("plankWood", 1, Material.VALUE_Ingot);
        wood.addItem("logWood", 1, Material.VALUE_Ingot * 4);
        wood.setRepresentativeItem(new ItemStack(Blocks.PLANKS));
        wood.addTrait(ecological, MaterialTypes.HANDLE);
        wood.addTrait(ecological, MaterialTypes.EXTRA);
        TinkerRegistry.addMaterialStats(wood, new HandleMaterialStats(1.0f, 25), new ExtraMaterialStats(35));
        register(wood);

        // Flint
        flint = new GatewayMaterial("flint", 0x595959).setCraftable(true);
        flint.addItem(Items.FLINT, 1, Material.VALUE_Ingot);
        flint.setRepresentativeItem(new ItemStack(Items.FLINT));
        flint.addTrait(crude, MaterialTypes.EXTRA);
        flint.addTrait(crude2, MaterialTypes.HEAD);
        TinkerRegistry.addMaterialStats(flint, new HeadMaterialStats(200, 5.25f, 2.60f, 0), new ExtraMaterialStats(10));
        register(flint);

        // Netherrack
        netherrack = new GatewayMaterial("netherrack", 0xb84f4f).setCraftable(true);
        netherrack.addItemIngot("netherrack");
        netherrack.setRepresentativeItem(Blocks.NETHERRACK);
        netherrack.addTrait(stonebreaker, MaterialTypes.EXTRA);
        netherrack.addTrait(TinkerTraits.hellish, MaterialTypes.HEAD);
        TinkerRegistry.addMaterialStats(netherrack, new HeadMaterialStats(270, 4.0f, 2.4f, 0), new ExtraMaterialStats(-15));
        register(netherrack);

        // Bone
        bone = new GatewayMaterial("bone", 0xede6bf).setCraftable(true);
        bone.addItemIngot("bone");
        bone.setRepresentativeItem(Items.BONE);
        bone.addTrait(strike, MaterialTypes.HEAD);
        bone.addTrait(TinkerTraits.splitting, MaterialTypes.SHAFT);
        bone.addTrait(TinkerTraits.fractured);
        TinkerRegistry.addMaterialStats(bone,
            new HeadMaterialStats(250, 4.0f, 3.0f, 0),
            new ExtraMaterialStats(50),
            new HandleMaterialStats(1.1f, 50));
        register(bone);

        // Elemental Block
        if (Loader.isModLoaded("essentialcraft")) {
            elementalBlock = new GatewayMaterial("elemental_block", 0xcf83ee).setCraftable(true);
            elementalBlock.addItem("blockElemental", 1, Material.VALUE_Ingot);
            elementalBlock.setRepresentativeItem("blockElemental");
            elementalBlock.addTrait(activation, MaterialTypes.HEAD);
            elementalBlock.addTrait(slowdown, MaterialTypes.HANDLE);
            TinkerRegistry.addMaterialStats(elementalBlock,
                new HeadMaterialStats(600, 5.0f, 1.0f, 0),
                new HandleMaterialStats(1.7f, 0));
            register(elementalBlock);
        }

        if (Loader.isModLoaded("botania")) {
            livingwood = new GatewayMaterial("livingwood", 0x702808).setCraftable(true);
            livingwood.addItem("livingwood", 1, Material.VALUE_Ingot);
            livingwood.setRepresentativeItem("livingwood");
            livingwood.addTrait(naturalSelection, MaterialTypes.HANDLE);
            livingwood.addTrait(ecological2, MaterialTypes.EXTRA);
            TinkerRegistry.addMaterialStats(livingwood,
                new ExtraMaterialStats(30),
                new HandleMaterialStats(0.6f, 30));
            register(livingwood);

            livingrock = new GatewayMaterial("livingrock", 0xd7dac7).setCraftable(true);
            livingrock.addItem("livingrock", 1, Material.VALUE_Ingot);
            livingrock.setRepresentativeItem("livingrock");
            livingrock.addTrait(TinkerTraits.stonebound, MaterialTypes.HEAD);
            livingrock.addTrait(ecological, MaterialTypes.EXTRA);
            TinkerRegistry.addMaterialStats(livingrock,
                new HeadMaterialStats(150, 4.5f, 2.5f, 0),
                new ExtraMaterialStats(50));
        }
    }

    public static void registerMaterials() {
        if (GatewayConfig.tinkerOverridesNeeded()) {
            TinkerRegistry.addMaterial(wood);
            TinkerRegistry.addMaterial(flint);
            TinkerRegistry.addMaterial(netherrack);
            TinkerRegistry.addMaterial(bone);
        }
        if (GatewayConfig.createTinkerMats) {

            if (Loader.isModLoaded("essentialcraft")) {
                TinkerRegistry.addMaterial(elementalBlock);
            }

            if (Loader.isModLoaded("botania")) {
                TinkerRegistry.addMaterial(livingwood);
                TinkerRegistry.addMaterial(livingrock);
            }

        }
    }

    public static void setup() {
        allowedMaterials.add("string");
        allowedMaterials.add("vine");
        allowedMaterials.add("reed");
        allowedMaterials.add("ice");
        allowedMaterials.add("feather");
        allowedMaterials.add("leaf");
        allowedMaterials.add("slimeleaf_blue");
        allowedMaterials.add("slimeleaf_orange");
        allowedMaterials.add("slimeleaf_purple");

        createMaterials();
    }

}
