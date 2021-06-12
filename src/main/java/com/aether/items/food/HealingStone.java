package com.aether.items.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HealingStone extends Item {
    public HealingStone(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isFoil(ItemStack stackIn) {
        return true;
    }
}