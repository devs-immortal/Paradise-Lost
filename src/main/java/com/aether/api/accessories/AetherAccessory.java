package com.aether.api.accessories;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherAccessory {

    private final Identifier registryName;
    private final ItemStack accessoryStack;
    private final AccessoryType accessoryType;
    private final AccessoryType extraType;

    public AetherAccessory(ItemConvertible provider, AccessoryType type) {
        this(new ItemStack(provider), type);
    }

    public AetherAccessory(ItemStack stack, AccessoryType type) {
        this.accessoryType = type;
        this.accessoryStack = stack;
        this.registryName = Registry.ITEM.getId(stack.getItem());
        this.extraType = type == AccessoryType.RING ? AccessoryType.EXTRA_RING : type == AccessoryType.MISC ? AccessoryType.EXTRA_MISC : null;
    }

    public Identifier getRegistryName() {
        return this.registryName;
    }

    public AccessoryType getAccessoryType() {
        return this.accessoryType;
    }

    public AccessoryType getExtraType() {
        return this.extraType;
    }

    public ItemStack getAccessoryStack() {
        return this.accessoryStack;
    }
}