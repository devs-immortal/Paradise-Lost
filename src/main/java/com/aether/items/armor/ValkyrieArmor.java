package com.aether.items.armor;

import com.aether.items.AetherItems;
import net.minecraft.entity.EquipmentSlot;

public final class ValkyrieArmor extends AetherArmor {
    public ValkyrieArmor(AetherArmorType typeIn, EquipmentSlot slotIn) {
        super("valkyrie", typeIn, AetherItems.AETHER_LOOT, slotIn);
    }
}