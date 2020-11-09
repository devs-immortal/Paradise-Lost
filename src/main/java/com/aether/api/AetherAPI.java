package com.aether.api;

import com.aether.api.accessories.AetherAccessory;
import com.aether.api.enchantments.AetherEnchantment;
import com.aether.api.enchantments.AetherEnchantmentFuel;
import com.aether.api.freezables.AetherFreezable;
import com.aether.api.freezables.AetherFreezableFuel;
import com.aether.api.moa.MoaType;
import com.aether.api.player.IEntityPlayerAether;
import com.aether.api.player.IPlayerAether;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Random;

public class AetherAPI {

    private static final HashMap<Identifier, AetherAccessory> ACCESSORY_REGISTRY = new HashMap<>();
    private static final HashMap<Identifier, AetherEnchantment> ENCHANTMENT_REGISTRY = new HashMap<>();
    private static final HashMap<Identifier, AetherFreezable> FREEZABLE_REGISTRY = new HashMap<>();
    private static final HashMap<Identifier, AetherEnchantmentFuel> ENCHANTMENT_FUEL_REGISTRY = new HashMap<>();
    private static final HashMap<Identifier, AetherFreezableFuel> FREEZABLE_FUEL_REGISTRY = new HashMap<>();

    private static final HashMap<Identifier, MoaType> MOA_REGISTRY = new HashMap<>();
    private static final AetherAPI INSTANCE = new AetherAPI();
    private static int moaListSize;

    public AetherAPI() {

    }

    public static IPlayerAether get(PlayerEntity player) {
        return ((IEntityPlayerAether) player).getPlayerAether();
    }

    public static AetherAPI instance() {
        return INSTANCE;
    }

    public void register(Identifier registryName, MoaType moa) {
        moa.setRegistryName(registryName);

        MOA_REGISTRY.put(registryName, moa);
        ++moaListSize;
    }

    public void register(AetherAccessory accessory) {
        ACCESSORY_REGISTRY.put(accessory.getRegistryName(), accessory);
    }

    public void register(AetherFreezable freezable) {
        FREEZABLE_REGISTRY.put(freezable.getRegistryName(), freezable);
    }

    public void register(AetherFreezableFuel fuel) {
        FREEZABLE_FUEL_REGISTRY.put(fuel.getRegistryName(), fuel);
    }

    public void register(AetherEnchantment enchantment) {
        ENCHANTMENT_REGISTRY.put(enchantment.getRegistryName(), enchantment);
    }

    public void register(AetherEnchantmentFuel fuel) {
        ENCHANTMENT_FUEL_REGISTRY.put(fuel.getRegistryName(), fuel);
    }

    public AetherAccessory getAccessory(ItemStack stack) {
        return ACCESSORY_REGISTRY.get(Registry.ITEM.getId(stack.getItem()));
    }

    public boolean isAccessory(ItemStack stack) {
        return ACCESSORY_REGISTRY.containsKey(Registry.ITEM.getId(stack.getItem()));
    }

    public AetherFreezable getFreezable(ItemStack stack) {
        return FREEZABLE_REGISTRY.get(Registry.ITEM.getId(stack.getItem()));
    }

    public boolean isFreezable(ItemStack stack) {
        return FREEZABLE_REGISTRY.containsKey(Registry.ITEM.getId(stack.getItem()));
    }

    public AetherFreezableFuel getFreezableFuel(ItemStack stack) {
        return FREEZABLE_FUEL_REGISTRY.get(Registry.ITEM.getId(stack.getItem()));
    }

    public boolean isFreezerFuel(ItemStack stack) {
        return FREEZABLE_FUEL_REGISTRY.containsKey(Registry.ITEM.getId(stack.getItem()));
    }

    public AetherEnchantment getEnchantment(ItemStack stack) {
        return ENCHANTMENT_REGISTRY.get(Registry.ITEM.getId(stack.getItem()));
    }

    public boolean isEnchantable(ItemStack stack) {
        return ENCHANTMENT_REGISTRY.containsKey(Registry.ITEM.getId(stack.getItem()));
    }

    public AetherEnchantmentFuel getEnchantmentFuel(ItemStack stack) {
        return ENCHANTMENT_FUEL_REGISTRY.get(Registry.ITEM.getId(stack.getItem()));
    }

    public boolean isEnchantmentFuel(ItemStack stack) {
        return ENCHANTMENT_FUEL_REGISTRY.containsKey(Registry.ITEM.getId(stack.getItem()));
    }

    public MoaType getMoa() {
        Random random = new Random();
        MoaType[] rescValues = MOA_REGISTRY.values().toArray(new MoaType[0]);

        return rescValues[random.nextInt(rescValues.length)];
    }

    public MoaType getMoa(Identifier registryName) {
        return MOA_REGISTRY.get(registryName);
    }

    public MoaType getMoa(int id) {
        MoaType[] rescValues = MOA_REGISTRY.values().toArray(new MoaType[0]);

        return rescValues[id];
    }

    public int getMoaId(MoaType moa) {
        boolean isMatch = false;
        int indexNumber = -1;

        for (MoaType moaType : MOA_REGISTRY.values()) {
            indexNumber++;
            if (moa.equals(moaType)) {
                isMatch = true;
                break;
            }
        }
        return isMatch ? indexNumber : 0;
    }

    public int getMoaRegistrySize() {
        return moaListSize;
    }

}