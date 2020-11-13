package com.aether.items.armor;

import com.aether.items.AetherItems;
import net.minecraft.entity.EquipmentSlot;

public final class SentryBoots extends AetherArmor {
    public SentryBoots(AetherArmorType typeIn, EquipmentSlot slotIn) {
        super("sentry", typeIn, AetherItems.AETHER_LOOT, slotIn);
    }
}
