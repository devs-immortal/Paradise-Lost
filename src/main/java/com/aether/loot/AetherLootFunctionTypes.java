package com.aether.loot;

import com.aether.Aether;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class AetherLootFunctionTypes {
    public static final LootFunctionType SET_STACKABLE_VARIANT = register("set_stackable_variant", new SetStackableVariantLootFunction.Serializer());

    private static LootFunctionType register(String id, JsonSerializer<? extends LootFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, Aether.locate(id), new LootFunctionType(serializer));
    }

    public static void init() {
        // N/A
    }
}
