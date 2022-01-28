package net.id.aether.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Make this work, for now this is a reference to names that need changing. (PL-1.8)
public final class AetherDataFixers {
    private static final String OLD_NAMESPACE = "the_aether";
    private static final String NEW_NAMESPACE = "paradise_lost";
    
    private static final Map<String, String> BLOCKS = createMap(
            "aether_grass",             "grass_block",
            "enchanted_aether_grass",   "enchanted_grass_block",
            "aether_frozen_grass",      "frozen_grass_block",
            "aether_dirt",              "dirt",
            "coarse_aether_dirt",       "coarse_dirt",
            "aether_farmland",          "farmland",
            "aether_grass_path",        "grass_path",
            "aether_grass_plant",       "grass_plant",
            "aether_grass_flowering",   "flowering_grass_plant",
            "aether_short_grass",       "short_grass",
            "aether_tall_grass",        "tall_grass",
            "aether_fern",              "fern",
            "potted_aether_fern",       "potted_fern",
            "aether_bush",              "bush",
            "block_of_gravitite",       "gravitite_block"
    );
    
    private static final Map<String, String> ITEMS = mergeMaps(createMap(
            "blue_berry",               "blueberry",
            "ginger_bread_man",         "gingerbread_man",
            "moa_meat_cooked",          "cooked_moa_meat",
            "angelic_stone_cracked",    "cracked_angelic_stone",
            "lore_book",                "book_of_lore"
    ), BLOCKS);

    private static final Map<String, String> BIOMES = createMap(
            "aether_highlands",         "highlands_plains",
            "aether_highlands_forest",  "highlands_forest",
            "aether_highlands_thicket", "highlands_thicket",
            "aether_wisteria_woods",    "highlands_wisteria_woods"
    );

    private static final Map<String, String> ENTITIES = createMap(
            "chest_mimic",              "mimic", // honestly we can probably just change this one w/o a datafixer
            "white_swet",               "swet"
    );

    private static final Map<String, String> ITEM_GROUP = createMap(
            "aether_building_blocks",   "building_blocks",
            "aether_decorations",       "decorations",
            "aether_tools",             "tools",
            "aether_food",              "food",
            "aether_resources",         "resources",
            "aether_misc",              "misc",
            "aether_wearables",         "wearables"
    );

    /*
    TODO FEATURES OF A BLOCK SHOULD SHARE STRING ID
        e.g PlacedFeature AETHER_GRASS_BUSH id: (aether_grass) -> GRASS_PLANT id: (grass_plant)
        And ConfiguredFeatures too.
     */
    
    @SafeVarargs
    private static <T> Map<T, T> createMap(T... values){
        if((values.length & 1) != 0){
            throw new IllegalArgumentException("Odd number of values");
        }
        Map<T, T> map = new HashMap<>();
        for(int i = 0; i < values.length; i += 2){
            map.put(values[i], values[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }
    
    @SafeVarargs
    private static <K, V> Map<K, V> mergeMaps(Map<K, V>... maps){
        return Stream.of(maps)
            .map(Map::entrySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
