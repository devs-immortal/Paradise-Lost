package com.aether.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.
 *
 * Originally by Juuz
 */
public interface InventoryWrapper extends Inventory {
    /**
     * Gets the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    DefaultedList<ItemStack> getItems();
    // Creation
    /**
     * Creates an inventory from the item list.
     */
    static InventoryWrapper of(DefaultedList<ItemStack> items) {
        return () -> items;
    }
    /**
     * Creates a new inventory with the size.
     */
    static InventoryWrapper ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }
    // Inventory
    /**
     * Returns the inventory size.
     */
    @Override
    default int size() {
        return getItems().size();
    }
    /**
     * @return true if this inventory has only empty stacks, false otherwise
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    /**
     * Gets the item in the slot.
     */
    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    default int getItemSlot(ItemStack stack) {
        DefaultedList<ItemStack> inv = getItems();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack item = inv.get(i);
            if(item.getMaxCount() > item.getCount() && item.isItemEqual(stack)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Takes a stack of the size from the slot.
     * <p>(default implementation) If there are less items in the slot than what are requested,
     * takes all items in that slot.
     */
    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }
    /**
     * Removes the current stack in the {@code slot} and returns it.
     */
    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }
    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }
    /**
     * Clears {@linkplain #getItems() the item list}}.
     */
    @Override
    default void clear() {
        getItems().clear();
    }
    @Override
    default void markDirty() {
        // Override if you want behavior.
    }
    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}


