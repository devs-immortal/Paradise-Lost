package com.aether.items.tools;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import com.aether.items.AetherItemGroups;

public class Parachute extends Item {

    public Parachute(int maxDamage) {
        super(new Settings().maxCount(1).maxDamage(maxDamage).group(AetherItemGroups.MISC));
    }

    public Parachute() {
        super(new Settings().maxCount(1).group(AetherItemGroups.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {

        //TODO: Implement parachute logic

        return super.use(worldIn, playerIn, handIn);
    }
}