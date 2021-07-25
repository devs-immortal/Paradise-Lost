package com.aether.loot;

import com.aether.Aether;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class AetherLootNumberProviderTypes {
    public static final LootNumberProviderType SLIME_SIZE = register("slime_size", new SlimeSizeLootNumberProvider.Serializer());

    private static LootNumberProviderType register(String id, JsonSerializer<? extends LootNumberProvider> jsonSerializer) {
        return Registry.register(Registry.LOOT_NUMBER_PROVIDER_TYPE, Aether.locate(id), new LootNumberProviderType(jsonSerializer));
    }

    public static void init() {
        // N/A
    }
}
