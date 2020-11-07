package com.aether.items.armor;

import com.aether.items.AetherItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.sound.SoundEvents;

public enum AetherArmorType {
    //TODO: Maybe tweak knockback resistance
    Zanite(new AetherArmorMaterial("zanite", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, AetherItems.zanite_gem, 0), 0x711AE8),
    Gravitite(new AetherArmorMaterial("gravitite", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, Blocks.AIR, 0), 0xe752DB),
    Neptune(new AetherArmorMaterial("neptune", 35, new int[]{4, 6, 8, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.5F, Blocks.AIR, 0), 0x2654FF),
    Phoenix(new AetherArmorMaterial("phoenix", 35, new int[]{4, 6, 8, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.5F, Blocks.AIR, 0), 0xFF7700),
    Obsidian(new AetherArmorMaterial("obsidian", 38, new int[]{4, 8, 10, 4}, 6, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, Blocks.AIR, 0), 0x1B1447),
    Valkyrie(new AetherArmorMaterial("valkyrie", 35, new int[]{4, 8, 10, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.5F, Blocks.AIR, 0), 0xDDDDDD);

    private final ArmorMaterial material;
    private final int color;

    AetherArmorType(ArmorMaterial materialIn) {
        this.color = 0xDDDDDD;
        this.material = materialIn;
    }

    AetherArmorType(ArmorMaterial materialIn, int colorIn) {
        this.color = colorIn;
        this.material = materialIn;
    }

    public int getColor() {
        return this.color;
    }

    public ArmorMaterial getMaterial() {
        return this.material;
    }
}