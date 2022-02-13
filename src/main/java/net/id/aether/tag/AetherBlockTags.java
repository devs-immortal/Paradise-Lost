package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

import static net.id.aether.Aether.locate;

public class AetherBlockTags {
    // Floating Blocks
    public static final Tag<Block> FAST_FLOATERS = TagFactory.BLOCK.create(locate("fast_floaters"));
    public static final Tag<Block> NON_FLOATERS = TagFactory.BLOCK.create(locate("non_floaters"));
    public static final Tag<Block> PUSH_FLOATERS = TagFactory.BLOCK.create(locate("push_floaters"));
    // TODO change this tag name
    public static final Tag<Block> HURTABLE_FLOATERS = TagFactory.BLOCK.create(locate("hurtable_floaters"));

    // Plants
    public static final Tag<Block> LICHEN_SPREADABLES = TagFactory.BLOCK.create(locate("plants/lichen_spreadable"));
    public static final Tag<Block> FUNGI_CLINGABLES = TagFactory.BLOCK.create(locate("plants/fungi_clingable"));
    public static final Tag<Block> GENERIC_VALID_GROUND = TagFactory.BLOCK.create(locate("plants/generic_valid_ground"));
    public static final Tag<Block> FLUTEGRASS_VALID_GROUND = TagFactory.BLOCK.create(locate("plants/flutegrass_valid_ground"));
    public static final Tag<Block> AECHOR_PLANT_VALID_GROUND = TagFactory.BLOCK.create(locate("plants/aechor_plant_valid_ground"));

    // Swets
    public static final Tag<Block> SWET_TRANSFORMERS_BLUE = TagFactory.BLOCK.create(locate("swet_transformers/blue"));
    public static final Tag<Block> SWET_TRANSFORMERS_GOLDEN = TagFactory.BLOCK.create(locate("swet_transformers/golden"));
    public static final Tag<Block> SWET_TRANSFORMERS_PURPLE = TagFactory.BLOCK.create(locate("swet_transformers/purple"));
    public static final Tag<Block> SWET_TRANSFORMERS_VERMILION = TagFactory.BLOCK.create(locate("swet_transformers/vermilion"));

    // Worldgen
    public static final Tag<Block> BASE_AETHER_STONE = TagFactory.BLOCK.create(locate("worldgen/base_aether_stone"));
    public static final Tag<Block> FLUID_REPLACEABLES = TagFactory.BLOCK.create(locate("worldgen/fluid_replaceable"));
    public static final Tag<Block> BASE_REPLACEABLES = TagFactory.BLOCK.create(locate("worldgen/base_replaceables"));

    //JEB, WHY ARE SHEARS HARDCODED
    public static final Tag<Block> AETHER_SHEARABLE = TagFactory.BLOCK.create(locate("mineable_by_shears"));
}
