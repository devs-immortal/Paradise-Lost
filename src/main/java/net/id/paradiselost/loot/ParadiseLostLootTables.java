package net.id.paradiselost.loot;

import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostLootTables {
    public static final RegistryKey<LootTable> MOTHER_AUREL_STRIPPING = register("gameplay/mother_aurel_log_strip");


    public ParadiseLostLootTables() {
    }

    private static RegistryKey<LootTable> register(String id) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, locate(id));
    }

}
