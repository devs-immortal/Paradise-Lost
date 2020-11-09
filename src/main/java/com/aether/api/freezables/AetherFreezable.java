package com.aether.api.freezables;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherFreezable {

    public int timeRequired;
    public ItemStack input, output;
    private final Identifier registryName;

    public AetherFreezable(ItemStack input, ItemConvertible output, int timeRequired) {
        this(input, new ItemStack(output), timeRequired);
    }

    public AetherFreezable(ItemConvertible input, ItemStack output, int timeRequired) {
        this(new ItemStack(input), output, timeRequired);
    }

    public AetherFreezable(ItemConvertible input, ItemConvertible output, int timeRequired) {
        this(new ItemStack(input), new ItemStack(output), timeRequired);
    }

    public AetherFreezable(ItemStack input, ItemStack output, int timeRequired) {
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
        if (obj instanceof AetherFreezable) {
            AetherFreezable freezable = (AetherFreezable) obj;

            boolean inputCheck = this.getInput().getItem() == freezable.getInput().getItem();
            boolean outputCheck = this.getOutput().getItem() == freezable.getOutput().getItem();

            return inputCheck && outputCheck;
        }
        return false;
    }
}