package com.aether.api.freezables;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherFreezableFuel {

    public int timeGiven;
    private final Identifier registryName;
    private final ItemStack fuel;

    public AetherFreezableFuel(ItemConvertible fuel, int timeGiven) {
        this(new ItemStack(fuel), timeGiven);
    }

    public AetherFreezableFuel(ItemStack fuelStack, int timeGiven) {
        this.timeGiven = timeGiven;
        this.fuel = fuelStack;
        this.registryName = Registry.ITEM.getId(fuelStack.getItem());
    }

    public Identifier getRegistryName() {
        return this.registryName;
    }

    public int getTimeGiven() {
        return this.timeGiven;
    }

    public ItemStack getFuel() {
        return this.fuel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AetherFreezableFuel) {
            AetherFreezableFuel fuel = (AetherFreezableFuel) obj;

            return this.getFuel().getItem() == fuel.getFuel().getItem();
        }
        return false;
    }
}