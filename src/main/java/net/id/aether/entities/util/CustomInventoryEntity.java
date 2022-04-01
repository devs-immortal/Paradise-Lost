package net.id.aether.entities.util;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Allows an entity to open a custom inventory like a Horse does.
 */
public interface CustomInventoryEntity {
    /**
     * Open the custom inventory for this entity.
     *
     * @param player The player that opened the inventory
     */
    void openInventory(PlayerEntity player);
}
