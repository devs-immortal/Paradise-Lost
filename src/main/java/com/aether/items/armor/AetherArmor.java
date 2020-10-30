package com.aether.items.armor;

import com.aether.items.AetherItemGroups;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Rarity;

public class AetherArmor extends ArmorItem {
    private String armorName = "iron";
    private final AetherArmorType typeIn;

    public AetherArmor(AetherArmorType typeIn, EquipmentSlot slotIn) {
        super(typeIn.getMaterial(), slotIn, new Settings().group(AetherItemGroups.ARMOR));
        this.typeIn = typeIn;
    }

    public AetherArmor(AetherArmorType typeIn, Rarity rarityIn, EquipmentSlot slotIn) {
        super(typeIn.getMaterial(), slotIn, new Settings().group(AetherItemGroups.ARMOR).rarity(rarityIn));
        this.typeIn = typeIn;
    }

    public AetherArmor(String nameIn, AetherArmorType typeIn, EquipmentSlot slotIn) {
        this(typeIn, slotIn);
        this.armorName = nameIn;
    }

    public AetherArmor(String nameIn, AetherArmorType typeIn, Rarity rarityIn, EquipmentSlot slotIn) {
        this(typeIn, rarityIn, slotIn);
        this.armorName = nameIn;
    }

    public AetherArmorType getType() {
        return this.typeIn;
    }

    public String getArmorName() {
        return this.armorName;
    }
}