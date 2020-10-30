package com.aether.items.accessories;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherAccessory {

    private Identifier registryName;
    private ItemStack accessoryStack;
    private AccessoryTypes accessoryTypes;
    private AccessoryTypes extraType;

    public AetherAccessory(ItemConvertible provider, AccessoryTypes type) {
        this(new ItemStack(provider), type);
    }

    public AetherAccessory(ItemStack stack, AccessoryTypes type) {
        this.accessoryTypes = type;
        this.accessoryStack = stack;

        this.registryName = Registry.ITEM.getId(stack.getItem());
        this.extraType = type == AccessoryTypes.RING ? AccessoryTypes.EXTRA_RING : type == AccessoryTypes.MISC ? AccessoryTypes.EXTRA_MISC : null;
    }

    public Identifier getRegistryName() {
        return this.registryName;
    }

    public AccessoryTypes getAccessoryType() {
        return this.accessoryTypes;
    }

    public AccessoryTypes getExtraType() {
        return this.extraType;
    }

    public ItemStack getAccessoryStack() {
        return this.accessoryStack;
    }
}