package com.aether.items.accessories;

import com.aether.Aether;
import com.aether.items.AetherItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemAccessory extends Item {
    private final int color;
    private final AccessoryType type;
    private Identifier texture;
    private Identifier texture_slim;
    private float damageMultiplier = 1.0F;

    public ItemAccessory(AccessoryType type, String material, AetherItemSettings settings) {
        super(settings.maxDamage(5 * type.getDurability()));

        this.type = type;
        this.color = settings.getEnchantmentGlintColor();
        this.texture = Aether.locate("textures/armor/accessory_" + material + ".png");
        this.texture_slim = Aether.locate("textures/armor/accessory_" + material + "_slim.png");
    }

    public ItemAccessory(AccessoryType type, AetherItemSettings settings) {
        this(type, "base", settings);
    }
//
//    public ItemAccessory(AccessoryType type) {
//        this(type, Rarity.COMMON, 0xDDDDDD);
//    }

//    public ItemAccessory(AccessoryType type, Settings settings) {
//        this(type, rarity, 0xDDDDDD);
//    }

//    public ItemAccessory(AccessoryType type, int color) {
//        this(type, Rarity.COMMON, color);
//    }

//    public ItemAccessory(String material, AccessoryType type) {
//        this(material, type, Rarity.COMMON, 0xDDDDDD);
//    }
//
//    public ItemAccessory(String material, AccessoryType type, Settings settings) {
//        this(material, type, rarity, 0xDDDDDD);
//    }
//
//    public ItemAccessory(String material, AccessoryType type, int color) {
//        this(material, type, Rarity.COMMON, color);
//    }

    public Identifier getTexture() {
        return this.getTexture(false);
    }

    public Identifier getTexture(boolean isSlim) {
        return isSlim ? this.texture_slim : this.texture;
    }

    public AccessoryType getType() {
        return this.type;
    }

    public int getColor() {
        return this.color;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public ItemAccessory setDamageMultiplier(float multiplier) {
        this.damageMultiplier = multiplier;
        return this;
    }
}