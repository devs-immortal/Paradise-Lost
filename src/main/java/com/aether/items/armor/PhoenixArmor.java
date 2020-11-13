package com.aether.items.armor;

import com.aether.items.AetherItems;
import net.minecraft.entity.EquipmentSlot;

public final class PhoenixArmor extends AetherArmor {
    public PhoenixArmor(AetherArmorType typeIn, EquipmentSlot slotIn) {
        super("phoenix", typeIn, AetherItems.AETHER_LOOT, slotIn);
    }
}
