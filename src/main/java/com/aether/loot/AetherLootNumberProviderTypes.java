package com.aether.loot;

import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.registry.Registry;

import static com.aether.Aether.locate;

public class AetherLootNumberProviderTypes {
    public static final LootNumberProviderType SLIME_SIZE = new LootNumberProviderType(new SlimeSizeLootNumberProvider.Serializer());

    public static void init() {
        Registry.register(Registry.LOOT_NUMBER_PROVIDER_TYPE, locate("slime_size"), SLIME_SIZE);
    }
}
