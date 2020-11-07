package com.aether.api.enchantments;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherEnchantment {

    public int timeRequired;
    public ItemStack input, output;
    private Identifier registryName;

    public AetherEnchantment(ItemConvertible input, int timeRequired) {
        this(input, new ItemStack(input), timeRequired);
    }

    public AetherEnchantment(ItemStack input, ItemConvertible output, int timeRequired) {
        this(input, new ItemStack(output), timeRequired);
    }

    public AetherEnchantment(ItemConvertible input, ItemStack output, int timeRequired) {
        this(new ItemStack(input), output, timeRequired);
    }

    public AetherEnchantment(ItemConvertible input, ItemConvertible output, int timeRequired) {
        this(new ItemStack(input), new ItemStack(output), timeRequired);
    }

    public AetherEnchantment(ItemStack input, ItemStack output, int timeRequired) {
        this.input = input;
        this.output = output;
        this.timeRequired = timeRequired;

        this.registryName = Registry.ITEM.getId(input.getItem());
    }

    public Identifier getRegistryName() {
        return this.registryName;
    }

    public int getTimeRequired() {
        return this.timeRequired;
    }

    public ItemStack getInput() {
        return this.input;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AetherEnchantment) {
            AetherEnchantment freezable = (AetherEnchantment) obj;

            boolean inputCheck = this.getInput().getItem() == freezable.getInput().getItem();
            boolean outputCheck = this.getOutput().getItem() == freezable.getOutput().getItem();

            return inputCheck && outputCheck;
        }

        return false;
    }

}