package net.id.paradise_lost.tag;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ParadiseLostItemTags {
    public static final TagKey<Item> AUREL_LOGS = register("aurel_logs");
    public static final TagKey<Item> MOTHER_AUREL_LOGS = register("mother_aurel_logs");
    public static final TagKey<Item> MENTH_LOGS = register("menth_logs");
    public static final TagKey<Item> WISTERIA_LOGS = register("wisteria_logs");
    public static final TagKey<Item> HOLLOW_LOGS = register("hollow_logs");
    public static final TagKey<Item> PARADISE_PLANKS = register("paradise_planks");
    public static final TagKey<Item> CLOUDS = register("clouds");
    public static final TagKey<Item> HANGERS = register("hangers");
    public static final TagKey<Item> MUSHROOMS = register("mushrooms");
    public static final TagKey<Item> IRON_INTERCHANGABLE = register("iron_interchangable");
    public static final TagKey<Item> CALCITE_POT_SHERDS = register("calcite_pot_sherds");
    public static final TagKey<Item> MOA_BREEDABLES = register("moa_breedables");
    public static final TagKey<Item> MOA_TEMPTABLES = register("moa_temptables");
    public static final TagKey<Item> RIGHTEOUS_WEAPONS = register("righteous_weapons");
    public static final TagKey<Item> SACRED_WEAPONS = register("sacred_weapons");
    public static final TagKey<Item> IGNITING_TOOLS = register("igniting_tools");
    public static final TagKey<Item> CALCITE_DECORATED_POT_INGREDIENTS = register("calcite_decorated_pot_ingredients");

    private static TagKey<Item> register(String id) {
        return TagKey.create(Registries.ITEM, ModConstants.id(id));
    }
}
