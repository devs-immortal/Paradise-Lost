package net.id.paradise_lost.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cross-loader deferred entries for data that Fabric registers at runtime and NeoForge
 * writes via {@code DataMapProvider} datagen. Prefer this over {@code IRegistrationHelper}
 * for fuels / compostables / strippables.
 */
public final class ParadiseLostDataEntries {
    private static final Map<Item, Integer> FUELS = new LinkedHashMap<>();
    private static final Map<Item, Float> COMPOSTABLES = new LinkedHashMap<>();
    private static final Map<Block, Block> STRIPPABLES = new LinkedHashMap<>();

    private ParadiseLostDataEntries() {
    }

    public static void registerFuel(ItemLike item, int burnTime) {
        FUELS.put(item.asItem(), burnTime);
    }

    public static void registerCompostable(ItemLike item, float chance) {
        COMPOSTABLES.put(item.asItem(), chance);
    }

    public static void registerStrippable(Block log, Block stripped) {
        STRIPPABLES.put(log, stripped);
    }

    public static Map<Item, Integer> fuels() {
        return Collections.unmodifiableMap(FUELS);
    }

    public static Map<Item, Float> compostables() {
        return Collections.unmodifiableMap(COMPOSTABLES);
    }

    public static Map<Block, Block> strippables() {
        return Collections.unmodifiableMap(STRIPPABLES);
    }
}
