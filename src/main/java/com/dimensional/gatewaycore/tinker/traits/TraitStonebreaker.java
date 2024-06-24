package com.dimensional.gatewaycore.tinker.traits;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitStonebreaker extends AbstractTrait {
    public TraitStonebreaker() {
        super("stonebreaker", 0xcc3333);
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        IBlockState state = event.getState();
        if (state.getMaterial().equals(Material.ROCK)) {
            if (state.getBlockHardness(event.getEntity().world, event.getPos()) < 2.5f) {
                event.setNewSpeed(event.getNewSpeed() * 1.4f);
            } else {
                event.setNewSpeed(event.getNewSpeed() * 0.75f);
            }
        }
    }
}
