package com.aether.items.food;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HealingStone extends Item {
    public HealingStone(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stackIn) {
        return true;
    }
}