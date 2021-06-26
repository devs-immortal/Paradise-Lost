package com.aether.loot;

import com.aether.Aether;
import com.google.common.collect.Sets;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Set;

public class AetherLootTables {
    private static final Set<Identifier> LOOT_TABLES = Sets.newHashSet();
    private static final Set<Identifier> LOOT_TABLES_READ_ONLY;
    public static final Identifier GOLDEN_OAK_STRIPPING;

    public AetherLootTables() {
    }

    private static Identifier register(String id) {
        return registerLootTable(Aether.locate(id));
    }

    private static Identifier registerLootTable(Identifier id) {
        if (LOOT_TABLES.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<Identifier> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }

    static {
        LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);
        GOLDEN_OAK_STRIPPING = register("gameplay/golden_oak_log_strip");
    }
}
