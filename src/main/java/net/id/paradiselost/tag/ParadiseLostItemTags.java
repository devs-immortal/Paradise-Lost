package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ParadiseLostItemTags {
    public static final TagKey<Item> MOA_BREEDABLES = register("moa_breedables");
    public static final TagKey<Item> MOA_TEMPTABLES = register("moa_temptables");
    public static final TagKey<Item> RIGHTEOUS_WEAPONS = register("righteous_weapons");
    public static final TagKey<Item> SACRED_WEAPONS = register("sacred_weapons");
    public static final TagKey<Item> IGNITING_TOOLS = register("igniting_tools");
    public static final TagKey<Item> CALCITE_DECORATED_POT_INGREDIENTS = register("calcite_decorated_pot_ingredients");

    public static final TagKey<Item> OLVITE_TOOL_MATERIALS = register("olvite_tool_materials");
    public static final TagKey<Item> SURTRUM_TOOL_MATERIALS = register("surtrum_tool_materials");
    public static final TagKey<Item> GLAZED_GOLD_TOOL_MATERIALS = register("glazed_gold_tool_materials");
    public static final TagKey<Item> SOUL_BLADE_TOOL_MATERIALS = register("soul_blade_tool_materials");

    public static final TagKey<Item> REPAIRS_OLVITE_ARMOR = register("repairs_olvite_armor");
    public static final TagKey<Item> REPAIRS_GLAZED_GOLD_ARMOR = register("repairs_glazed_gold_armor");
    public static final TagKey<Item> REPAIRS_SURTRUM_ARMOR = register("repairs_surtrum_armor");
    public static final TagKey<Item> REPAIRS_RELIC_ARMOR = register("repairs_relic_armor");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, ParadiseLost.locate(id));
    }
}
