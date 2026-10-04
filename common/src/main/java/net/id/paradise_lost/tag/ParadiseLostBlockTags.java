package net.id.paradise_lost.tag;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ParadiseLostBlockTags {

    public static final TagKey<Block> AUREL_LOGS = register("aurel_logs");
    public static final TagKey<Block> MOTHER_AUREL_LOGS = register("mother_aurel_logs");
    public static final TagKey<Block> MENTH_LOGS = register("menth_logs");
    public static final TagKey<Block> WISTERIA_LOGS = register("wisteria_logs");
    public static final TagKey<Block> HOLLOW_LOGS = register("hollow_logs");
    public static final TagKey<Block> CLOUDS = register("clouds");
    public static final TagKey<Block> HANGERS = register("hangers");
    public static final TagKey<Block> STRUCTURES_AVOID = register("structures_avoid");

    public static final TagKey<Block> ANIMALS_PREFERRED = register("animals_preferred");

    public static final TagKey<Block> FAST_FLOATERS = register("fast_floaters");
    public static final TagKey<Block> NON_FLOATERS = register("non_floaters");
    public static final TagKey<Block> PUSH_FLOATERS = register("push_floaters");
    public static final TagKey<Block> HURTABLE_FLOATERS = register("hurtable_floaters");
    public static final TagKey<Block> DECAYING_FLOATERS = register("decaying_floaters");

    public static final TagKey<Block> FUNGI_CLINGABLES = register("plants/fungi_clingable");
    public static final TagKey<Block> GENERIC_VALID_GROUND = register("plants/generic_valid_ground");
    public static final TagKey<Block> SWEDROOT_PLANTABLE = register("plants/swedroot_plantable");

    public static final TagKey<Block> DIRT_BLOCKS = register("worldgen/dirt_blocks");
    public static final TagKey<Block> NATURAL_STONE = register("worldgen/natural_stone");
    public static final TagKey<Block> CLOUD_CARVER_REPLACEABLES = register("worldgen/cloud_carver_replaceables");
    public static final TagKey<Block> BASE_PARADISE_LOST_STONE = register("worldgen/base_stone");
    public static final TagKey<Block> FLUID_REPLACEABLES = register("worldgen/fluid_replaceable");
    public static final TagKey<Block> BASE_REPLACEABLES = register("worldgen/base_replaceables");

    public static final TagKey<Block> INCUBATOR_WARMER_LIGHTS = register("incubator_warmer_lights");
    public static final TagKey<Block> INCUBATOR_WARMER_BEDS = register("incubator_warmer_beds");

    private static TagKey<Block> register(String id) {
        return TagKey.create(Registries.BLOCK, ModConstants.id(id));
    }
}
