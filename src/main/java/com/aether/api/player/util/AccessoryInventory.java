package com.aether.api.player.util;

import com.aether.api.accessories.AccessoryType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;

public interface AccessoryInventory extends Inventory {

    void addListener(InventoryChangedListener listener);

    void removeListener(InventoryChangedListener listener);

    void damageAccessory(int damage, AccessoryType type);

    void damageWornStack(int damage, ItemStack stack);

    void setInvStack(AccessoryType type, ItemStack stack);

    ItemStack getInvStack(AccessoryType type);

    ItemStack removeInvStack(AccessoryType type);

    boolean setAccessorySlot(ItemStack stack);

    boolean wearingAccessory(ItemStack stack);

    boolean wearingArmor(ItemStack stack);

    boolean isWearingZaniteSet();

    boolean isWearingGravititeSet();

    boolean isWearingNeptuneSet();

    boolean isWearingPhoenixSet();

    boolean isWearingObsidianSet();

    boolean isWearingValkyrieSet();

    int getAccessoryCount(ItemStack stack);

    DefaultedList<ItemStack> getInventory();

    default CompoundTag serialize(CompoundTag compound) {
        return Inventories.toTag(compound, this.getInventory());
    }

    default void deserialize(CompoundTag compound) {
        Inventories.fromTag(compound, this.getInventory());
    }

    @Override
    default boolean isEmpty() {
        Iterator<ItemStack> iterator = this.getInventory().iterator();
        ItemStack stack;

        do {
            if (!iterator.hasNext()) return true;

            stack = iterator.next();
        }
        while (stack.isEmpty());

        return false;
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        this.getInventory().set(slot, stack);
    }

    @Override
    default ItemStack getStack(int slot) {
        return this.getInventory().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int stackSize) {
        return Inventories.splitStack(this.getInventory(), slot, stackSize);
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.getInventory(), slot);
    }

    @Override
    default boolean canPlayerUse(PlayerEntity playerIn) {
        return !playerIn.removed;
    }

    @Override
    default int size() {
        return this.getInventory().size();
    }

    @Override
    default void clear() {
        this.getInventory().clear();
    }
}