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
    public static final TagKey<Item> RENDING_ENCHANTABLE = register("enchantable/rending");

    public static final TagKey<Item> OLVITE_TOOL_MATERIALS = register("olvite_tool_materials");
    public static final TagKey<Item> SURTRUM_TOOL_MATERIALS = register("surtrum_tool_materials");
    public static final TagKey<Item> GLAZED_GOLD_TOOL_MATERIALS = register("glazed_gold_tool_materials");
    public static final TagKey<Item> SOUL_BLADE_TOOL_MATERIALS = register("soul_blade_tool_materials");

    public static final TagKey<Item> REPAIRS_OLVITE_ARMOR = register("repairs_olvite_armor");
    public static final TagKey<Item> REPAIRS_GLAZED_GOLD_ARMOR = register("repairs_glazed_gold_armor");
    public static final TagKey<Item> REPAIRS_SURTRUM_ARMOR = register("repairs_surtrum_armor");
    public static final TagKey<Item> REPAIRS_RELIC_ARMOR = register("repairs_relic_armor");
    public static final TagKey<Item> REPAIRS_FLOATY_ARMOR = register("repairs_floaty_armor");

    private static TagKey<Item> register(String id) {
        return TagKey.create(Registries.ITEM, ModConstants.id(id));
    }
}
