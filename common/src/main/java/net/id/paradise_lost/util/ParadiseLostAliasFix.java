package net.id.paradise_lost.util;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class ParadiseLostAliasFix {

    private static final Map<ResourceLocation, ResourceLocation> ALIASES = new HashMap<>();

    private ParadiseLostAliasFix() {}

    public static void init() {
        aliasBlock("orange_sapling", "aurel_sapling");
        aliasBlock("orange_log", "aurel_log");
        aliasBlock("orange_wood", "aurel_wood");
        aliasBlock("stripped_orange_log", "stripped_aurel_log");
        aliasBlock("stripped_orange_wood", "stripped_aurel_wood");
        aliasBlock("orange_leaves", "aurel_leaves");
        aliasBlock("orange_planks", "aurel_planks");
        aliasBlock("orange_fence", "aurel_fence");
        aliasBlock("orange_fence_gate", "aurel_fence_gate");
        aliasBlock("orange_slab", "aurel_slab");
        aliasBlock("orange_stairs", "aurel_stairs");
        aliasBlock("orange_trapdoor", "aurel_trapdoor");
        aliasBlock("orange_door", "aurel_door");
        aliasBlock("orange_button", "aurel_button");
        aliasBlock("orange_pressure_plate", "aurel_pressure_plate");
        aliasBlock("orange_sign", "aurel_sign");
        aliasBlock("orange_hanging_sign", "aurel_hanging_sign");

        aliasItem("orange_sapling", "aurel_sapling");
        aliasItem("orange_log", "aurel_log");
        aliasItem("orange_wood", "aurel_wood");
        aliasItem("stripped_orange_log", "stripped_aurel_log");
        aliasItem("stripped_orange_wood", "stripped_aurel_wood");
        aliasItem("orange_leaves", "aurel_leaves");
        aliasItem("orange_planks", "aurel_planks");
        aliasItem("orange_fence", "aurel_fence");
        aliasItem("orange_fence_gate", "aurel_fence_gate");
        aliasItem("orange_slab", "aurel_slab");
        aliasItem("orange_stairs", "aurel_stairs");
        aliasItem("orange_trapdoor", "aurel_trapdoor");
        aliasItem("orange_door", "aurel_door");
        aliasItem("orange_button", "aurel_button");
        aliasItem("orange_pressure_plate", "aurel_pressure_plate");
        aliasItem("orange_sign", "aurel_sign");
        aliasItem("orange_hanging_sign", "aurel_hanging_sign");

        alias(BuiltInRegistries.ITEM, ModConstants.id("orange"), ResourceLocation.withDefaultNamespace("apple"));

        ModConstants.LOGGER.info("Registered {} registry aliases for renamed Paradise Lost content", ALIASES.size());
    }

    public static ResourceLocation resolve(ResourceLocation id) {
        if (id == null) {
            return null;
        }
        ResourceLocation to = ALIASES.get(id);
        return to == null || ALIASES.containsKey(to) ? null : to;
    }

    private static void aliasBlock(String from, String to) {
        alias(BuiltInRegistries.BLOCK, ModConstants.id(from), ModConstants.id(to));
    }

    private static void aliasItem(String from, String to) {
        alias(BuiltInRegistries.ITEM, ModConstants.id(from), ModConstants.id(to));
    }

    private static void alias(Registry<?> registry, ResourceLocation from, ResourceLocation to) {
        if (!registry.containsKey(to)) {
            ModConstants.LOGGER.warn("Not aliasing {} -> {}: {} is not registered in {}", from, to, to, registry.key().location());
            return;
        }
        ALIASES.put(from, to);
    }
}
