package com.aether.api.enchantments;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherEnchantmentFuel {

    public int timeGiven;
    private Identifier registryName;
    private ItemStack fuel;

    public AetherEnchantmentFuel(ItemConvertible fuel, int timeGiven) {
        this(new ItemStack(fuel), timeGiven);
    }

    public AetherEnchantmentFuel(ItemStack fuelStack, int timeGiven) {
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
        if (obj instanceof AetherEnchantmentFuel) {
            AetherEnchantmentFuel fuel = (AetherEnchantmentFuel) obj;

            return this.getFuel().getItem() == fuel.getFuel().getItem();
        }

        return false;
    }

}