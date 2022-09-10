package net.id.paradiselost.entities.util;

import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Allows an entity to open a custom inventory like a Horse does.
 *
 * @deprecated Minecraft 1.19 has a replacement!
 */
@Deprecated(forRemoval = true)
public interface CustomInventoryEntity extends RideableInventory {
    /**
     * Open the custom inventory for this entity.
     *
     * @param player The player that opened the inventory
     */
    void openInventory(PlayerEntity player);
}
