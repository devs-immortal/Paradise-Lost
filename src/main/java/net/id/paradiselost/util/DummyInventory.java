package net.id.paradiselost.util;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class DummyInventory extends SimpleInventory {

    public DummyInventory() {
        super(0);
    }

    public void setStack(int slot, ItemStack stack) {
    }

}
