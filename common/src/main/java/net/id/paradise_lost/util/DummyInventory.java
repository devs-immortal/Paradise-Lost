package net.id.paradise_lost.util;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class DummyInventory extends SimpleContainer {

    public DummyInventory() {
        super(0);
    }

    public void setItem(int slot, ItemStack stack) {
    }

}
