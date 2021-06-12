package com.aether.items.accessories;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class AetherAccessory {

    private final ResourceLocation registryName;
    private final ItemStack accessoryStack;
    private final AccessoryType accessoryType;
    private final AccessoryType extraType;

    public AetherAccessory(ItemLike provider, AccessoryType type) {
        this(new ItemStack(provider), type);
    }

    public AetherAccessory(ItemStack stack, AccessoryType type) {
        this.accessoryType = type;
        this.accessoryStack = stack;

        this.registryName = Registry.ITEM.getKey(stack.getItem());
        this.extraType = type == AccessoryType.RING ? AccessoryType.EXTRA_RING : type == AccessoryType.MISC ? AccessoryType.EXTRA_MISC : null;
    }

    public ResourceLocation getRegistryName() {
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