package net.id.paradise_lost.loot;

import static net.id.paradise_lost.ModConstants.id;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ParadiseLostLootTables {
    public static final ResourceKey<LootTable> REMAINS_COMMON = register("archaeology/remains_common");

    public static final ResourceKey<LootTable> AUREL_TOWER = register("chests/aurel_tower");
    public static final ResourceKey<LootTable> BIRDCAGE_TOMB = register("chests/birdcage/tomb");
    public static final ResourceKey<LootTable> BIRDCAGE_TOMB_BARE = register("chests/birdcage/tomb_bare");
    public static final ResourceKey<LootTable> PALACE_ARTISAN = register("chests/palace/artisan");
    public static final ResourceKey<LootTable> PALACE_KEY = register("chests/palace/key");
    public static final ResourceKey<LootTable> PALACE_LIBRARY = register("chests/palace/library");
    public static final ResourceKey<LootTable> PALACE_SECRET_JUNK = register("chests/palace/secret_junk");
    public static final ResourceKey<LootTable> PALACE_SWEETS = register("chests/palace/sweets");
    public static final ResourceKey<LootTable> VAULT_FOOD = register("chests/vault/food");
    public static final ResourceKey<LootTable> VAULT_JUNK_BLOCKS = register("chests/vault/junk_blocks");
    public static final ResourceKey<LootTable> VAULT_JUNK_DEBRIS = register("chests/vault/junk_debris");
    public static final ResourceKey<LootTable> VAULT_JUNK_LOOT = register("chests/vault/junk_loot");
    public static final ResourceKey<LootTable> VAULT_JUNK_UTILITY = register("chests/vault/junk_utility");
    public static final ResourceKey<LootTable> VAULT_VALUABLE = register("chests/vault/valuable");

    public static final ResourceKey<LootTable> POTS_GENERIC = register("pots/generic");
    public static final ResourceKey<LootTable> POTS_REMAINS = register("pots/remains");

    public static final ResourceKey<LootTable> SPAWNER_GENERIC_FOOD = register("spawners/generic_food");
    public static final ResourceKey<LootTable> SPAWNER_LEVITATION_RESOURCES = register("spawners/levitation_resources");
    public static final ResourceKey<LootTable> SPAWNER_UTILITY = register("spawners/utility");

    public static final ResourceKey<LootTable> BIRDCAGE_ENVOY = register("equipment/birdcage_envoy");
    public static final ResourceKey<LootTable> BIRDCAGE_ENVOY_OMINOUS = register("equipment/birdcage_envoy_ominous");

    public static final ResourceKey<LootTable> MOTHER_AUREL_STRIPPING = register("gameplay/mother_aurel_log_strip");

    public static final ResourceKey<LootTable> POPOM_JELLY_LEVEL_0 = register("entities/popom/jelly_0");
    public static final ResourceKey<LootTable> POPOM_JELLY_LEVEL_2 = register("entities/popom/jelly_2");
    public static final ResourceKey<LootTable> POPOM_JELLY_LEVEL_3 = register("entities/popom/jelly_3");

    public ParadiseLostLootTables() {
    }

    private static ResourceKey<LootTable> register(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, id(path));
    }
}
