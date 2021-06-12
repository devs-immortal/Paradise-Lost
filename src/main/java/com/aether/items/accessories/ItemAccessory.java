package com.aether.items.accessories;

import com.aether.Aether;
import com.aether.items.AetherItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemAccessory extends Item {
    private final int color;
    private final AccessoryType type;
    private final ResourceLocation texture, texture_slim;
    private float damageMultiplier = 1.0F;

    public ItemAccessory(AccessoryType type, String material, AetherItemSettings settings) {
        super(settings.durability(5 * type.getDurability()));

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

    public ResourceLocation getTexture() {
        return this.getTexture(false);
    }

    public ResourceLocation getTexture(boolean isSlim) {
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