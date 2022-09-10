package net.id.paradiselost.loot;

import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.registry.Registry;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostLootNumberProviderTypes {
    public static final LootNumberProviderType SLIME_SIZE = new LootNumberProviderType(new SlimeSizeLootNumberProvider.Serializer());

    public static void init() {
        Registry.register(Registry.LOOT_NUMBER_PROVIDER_TYPE, locate("slime_size"), SLIME_SIZE);
    }
}
