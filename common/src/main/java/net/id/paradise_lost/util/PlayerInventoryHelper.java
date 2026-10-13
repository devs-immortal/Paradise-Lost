package net.id.paradise_lost.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class PlayerInventoryHelper {
    private PlayerInventoryHelper() {}

    public static void giveBack(Player player, InteractionHand hand, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        ItemStack remaining = stack.copy();
        if (remaining.getMaxStackSize() > 1) {
            remaining = mergeIntoExistingStacks(player.getInventory(), remaining);
            if (remaining.isEmpty()) {
                player.getInventory().setChanged();
                return;
            }
        }

        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.isEmpty()) {
            player.setItemInHand(hand, remaining);
            return;
        }

        if (ItemStack.isSameItemSameComponents(handStack, remaining)
                && handStack.getCount() < handStack.getMaxStackSize()) {
            int move = Math.min(handStack.getMaxStackSize() - handStack.getCount(), remaining.getCount());
            handStack.grow(move);
            remaining.shrink(move);
            if (remaining.isEmpty()) {
                return;
            }
        }

        if (!player.getInventory().add(remaining)) {
            player.drop(remaining, false);
        }
    }

    private static ItemStack mergeIntoExistingStacks(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.isEmpty()
                    || !ItemStack.isSameItemSameComponents(slot, stack)
                    || slot.getCount() >= slot.getMaxStackSize()) {
                continue;
            }
            int move = Math.min(slot.getMaxStackSize() - slot.getCount(), stack.getCount());
            slot.grow(move);
            stack.shrink(move);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }
}
