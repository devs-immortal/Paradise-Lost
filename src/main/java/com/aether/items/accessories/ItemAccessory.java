package com.aether.items.accessories;

import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ItemAccessory extends Item {

    private int color;
    private AccessoryTypes type;
    private Identifier texture;
    private Identifier texture_slim;
    private float damageMultiplier = 1.0F;

    public ItemAccessory(AccessoryTypes type, Rarity rarity, int color) {
        super(new Settings().group(AetherItemGroups.ACCESSORIES).maxDamage(5 * type.getDurability()).rarity(rarity));

        this.type = type;
        this.color = color;
        this.texture = AetherItems.locate("textures/armor/accessory_base.png");
        this.texture_slim = AetherItems.locate("textures/armor/accessory_base_slim.png");
    }

    public ItemAccessory(String material, AccessoryTypes type, Rarity rarity, int color) {
        this(type, rarity, color);

        this.texture = AetherItems.locate("textures/armor/accessory_" + material + ".png");
        this.texture_slim = AetherItems.locate("textures/armor/accessory_" + material + "_slim.png");
    }

    public ItemAccessory(AccessoryTypes type) {
        this(type, Rarity.COMMON, 0xDDDDDD);
    }

    public ItemAccessory(AccessoryTypes type, Rarity rarity) {
        this(type, rarity, 0xDDDDDD);
    }

    public ItemAccessory(AccessoryTypes type, int color) {
        this(type, Rarity.COMMON, color);
    }

    public ItemAccessory(String material, AccessoryTypes type) {
        this(material, type, Rarity.COMMON, 0xDDDDDD);
    }

    public ItemAccessory(String material, AccessoryTypes type, Rarity rarity) {
        this(material, type, rarity, 0xDDDDDD);
    }

    public ItemAccessory(String material, AccessoryTypes type, int color) {
        this(material, type, Rarity.COMMON, color);
    }

    public ItemAccessory setDamageMultiplier(float multiplier) {
        this.damageMultiplier = multiplier;
        return this;
    }

    public Identifier getTexture() {
        return this.getTexture(false);
    }

    public Identifier getTexture(boolean isSlim) {
        return isSlim ? this.texture_slim : this.texture;
    }

    public AccessoryTypes getType() {
        return this.type;
    }

    public int getColor() {
        return this.color;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }
}