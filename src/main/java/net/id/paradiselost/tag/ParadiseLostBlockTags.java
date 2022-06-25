package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ParadiseLostBlockTags {

    public static final TagKey<Block> DIRT_BLOCKS = register("dirt_blocks");

    //Gravitite
    public static final TagKey<Block> FAST_FLOATERS = register("fast_floaters");
    public static final TagKey<Block> NON_FLOATERS = register("non_floaters");
    public static final TagKey<Block> PUSH_FLOATERS = register("push_floaters");
    public static final TagKey<Block> HURTABLE_FLOATERS = register("hurtable_floaters");

    //Plants
    public static final TagKey<Block> LICHEN_SPREADABLES = register("plants/lichen_spreadable");
    public static final TagKey<Block> FUNGI_CLINGABLES = register("plants/fungi_clingable");
    public static final TagKey<Block> GENERIC_VALID_GROUND = register("plants/generic_valid_ground");
    public static final TagKey<Block> FLUTEGRASS_VALID_GROUND = register("plants/flutegrass_valid_ground");
    public static final TagKey<Block> AECHOR_PLANT_VALID_GROUND = register("plants/aechor_plant_valid_ground");
    public static final TagKey<Block> SWEDROOT_PLANTABLE = register("plants/swedroot_plantable");

    //Swets
    public static final TagKey<Block> SWET_TRANSFORMERS_BLUE = register("swet_transformers/blue");
    public static final TagKey<Block> SWET_TRANSFORMERS_GOLDEN = register("swet_transformers/golden");
    public static final TagKey<Block> SWET_TRANSFORMERS_PURPLE = register("swet_transformers/purple");
    public static final TagKey<Block> SWET_TRANSFORMERS_VERMILION = register("swet_transformers/vermilion");

    //Worldgen
    public static final TagKey<Block> CLOUD_CARVER_REPLACEABLES = register("worldgen/cloud_carver_replaceables");
    public static final TagKey<Block> BASE_PARADISE_LOST_STONE = register("worldgen/base_paradise_lost_stone");
    public static final TagKey<Block> FLUID_REPLACEABLES = register("worldgen/fluid_replaceable");
    public static final TagKey<Block> BASE_REPLACEABLES = register("worldgen/base_replaceables");

    //JEB, WHY ARE SHEARS HARDCODED
    public static final TagKey<Block> PARADISE_LOST_SHEARABLE = register("mineable_by_shears");

    private static TagKey<Block> register(String id) {
        return TagKey.of(Registry.BLOCK_KEY, ParadiseLost.locate(id));
    }
}
