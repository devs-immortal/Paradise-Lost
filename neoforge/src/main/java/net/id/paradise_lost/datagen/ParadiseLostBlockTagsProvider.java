package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostBlockTagsProvider extends BlockTagsProvider {
    private static final String[] WOODS = {"aurel", "mother_aurel", "menth", "wisteria"};

    public ParadiseLostBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        entry(Tags.Blocks.BOOKSHELVES, "aurel_bookshelf");
        entry(Tags.Blocks.CHAINS, "olvite_chain");
        entry(Tags.Blocks.COBBLESTONES, "cobbled_floestone");
        entry(Tags.Blocks.ORES, "cherine_ore", "olvite_ore", "levita_ore");
        entry(Tags.Blocks.STONES, "floestone");
        entry(Tags.Blocks.STORAGE_BLOCKS, "flaxweave_cushion", "amadrys_bundle", "cherine_block", "olvite_block", "refined_surtrum_block", "nitra_bunch");
        woods(Tags.Blocks.STRIPPED_LOGS, "stripped_%s_log");
        woods(Tags.Blocks.STRIPPED_WOODS, "stripped_%s_wood");
        entry(mc("animals_spawnable_on"), "highlands_grass", "frozen_grass");
        entry(BlockTags.BEACON_BASE_BLOCKS, "olvite_block", "refined_surtrum_block");
        entry(BlockTags.CAMPFIRES, "cherine_campfire");
        woods(BlockTags.CEILING_HANGING_SIGNS, "%s_hanging_sign");
        entry(BlockTags.COMBINATION_STEP_SOUND_BLOCKS, "liverwort_carpet");
        entry(mc("convertable_to_mud"), "dirt", "coarse_dirt");
        entry(BlockTags.CROPS, "amadrys", "flax", "swedroot", "nitra");
        entry(BlockTags.DIRT, "dirt", "coarse_dirt", "permafrost", "highlands_grass", "frozen_grass", "suspicious_dirt");
        entry(BlockTags.ENCHANTMENT_POWER_PROVIDER, "aurel_bookshelf");
        woods(BlockTags.FENCE_GATES, "%s_fence_gate");
        entry(BlockTags.FLOWER_POTS, "calcite_flower_pot", "potted_ancient_flower", "potted_ataraxia", "potted_cloudsbluff", "potted_drigean", "potted_luminar",
                "potted_aurel_sapling", "potted_mother_aurel_sapling", "potted_menth_sapling",
                "potted_rose_wisteria_sapling", "potted_frost_wisteria_sapling", "potted_lavender_wisteria_sapling", "potted_fern");
        entry(BlockTags.LEAVES, "aurel_leaves", "mother_aurel_leaves", "rose_wisteria_leaves", "frost_wisteria_leaves", "lavender_wisteria_leaves", "menth_leaves");
        entry(BlockTags.LOGS,
                ParadiseLostBlockTags.AUREL_LOGS, ParadiseLostBlockTags.MOTHER_AUREL_LOGS,
                ParadiseLostBlockTags.WISTERIA_LOGS, ParadiseLostBlockTags.MENTH_LOGS, ParadiseLostBlockTags.HOLLOW_LOGS);
        entry(BlockTags.LOGS_THAT_BURN,
                ParadiseLostBlockTags.AUREL_LOGS, ParadiseLostBlockTags.MOTHER_AUREL_LOGS,
                ParadiseLostBlockTags.WISTERIA_LOGS, ParadiseLostBlockTags.MENTH_LOGS, ParadiseLostBlockTags.HOLLOW_LOGS);
        entry(BlockTags.MAINTAINS_FARMLAND, "amadrys", "flax", "nitra");
        entry(BlockTags.MINEABLE_WITH_AXE,
                "packed_swedroot", "cherine_campfire", "aurel_bookshelf", "blackcurrant_bush", "incubator", "food_bowl", "tree_tap",
                "rootcap_block", "brown_sporecap_block", "pink_sporecap_block", "thatch", "thatch_stairs", "thatch_slab", "nest",
                ParadiseLostBlockTags.HOLLOW_LOGS);
        entry(BlockTags.MINEABLE_WITH_HOE,
                "liverwort", "liverwort_carpet", ParadiseLostBlockTags.HANGERS,
                "rose_wisteria_leaf_pile", "frost_wisteria_leaf_pile", "lavender_wisteria_leaf_pile", "aurel_leaf_pile",
                "amadrys_bundle", "nitra_bunch",
                "aurel_leaves", "mother_aurel_leaves", "menth_leaves",
                "rose_wisteria_leaves", "frost_wisteria_leaves", "lavender_wisteria_leaves");
        entry(BlockTags.MINEABLE_WITH_PICKAXE,
                "cherine_ore", "olvite_ore", "floestone_redstone_ore", "surtrum", "metamorphic_shell", "levita_ore",
                "cherine_block", "olvite_block", "refined_surtrum_block", "levitator", "olvite_chain", "cherine_lantern", "bloomed_calcite",
                "floestone", "floestone_slab", "floestone_stairs", "smooth_floestone", "smooth_floestone_slab", "smooth_floestone_stairs",
                "cobbled_floestone", "cobbled_floestone_slab", "cobbled_floestone_stairs",
                "mossy_floestone", "golden_mossy_floestone", "mossy_floestone_slab", "mossy_floestone_stairs",
                "heliolith", "heliolith_slab", "heliolith_stairs",
                "floestone_brick", "mossy_floestone_brick", "chiseled_floestone", "floestone_brick_slab", "mossy_floestone_brick_slab",
                "floestone_brick_stairs", "mossy_floestone_brick_stairs",
                "smooth_heliolith", "smooth_heliolith_slab", "smooth_heliolith_stairs",
                "levita_brick", "levita_brick_slab", "levita_brick_stairs", "chiseled_levita_brick",
                "burnished_stone", "burnished_stone_slab", "burnished_stone_stairs", "burnished_stone_plaque", "burnished_stone_script",
                "golden_amber_tile", "golden_amber_tile_slab", "golden_amber_tile_stairs",
                "floestone_button", "floestone_pressure_plate", "olvite_pressure_plate", "golden_amber_bars",
                "calcite_tiles", "calcite_tiles_slab", "calcite_tiles_stairs",
                "bloomed_calcite_tiles", "bloomed_calcite_tiles_slab", "bloomed_calcite_tiles_stairs");
        entry(BlockTags.MINEABLE_WITH_SHOVEL,
                "highlands_grass", "frozen_grass", "dirt", "coarse_dirt", "permafrost", "levita", "farmland", "grass_path", "frozen_path",
                "levita_brick", "levita_brick_slab", "levita_brick_stairs", "chiseled_levita_brick", "suspicious_dirt");
        entry(mc("moss_replaceable"), "dirt", "coarse_dirt", "floestone", "cobbled_floestone", "mossy_floestone");
        entry(BlockTags.NEEDS_DIAMOND_TOOL, "metamorphic_shell");
        entry(BlockTags.NEEDS_IRON_TOOL, "floestone_redstone_ore", "surtrum", "levita_ore", "refined_surtrum_block");
        entry(BlockTags.NEEDS_STONE_TOOL, "olvite_ore", "olvite_block", "levitator");
        entry(BlockTags.OVERWORLD_CARVER_REPLACEABLES,
                "levita", "floestone", "cobbled_floestone", "mossy_floestone", "heliolith", "cherine_ore", "olvite_ore", "levita_ore");
        woods(BlockTags.PLANKS, "%s_planks");
        entry(BlockTags.PRESSURE_PLATES, "olvite_pressure_plate");
        entry(BlockTags.RAILS, "levita_rail");
        entry(BlockTags.REDSTONE_ORES, "floestone_redstone_ore");
        String[] replaceable = {
                "aurel_leaf_pile", "rose_wisteria_leaf_pile", "rose_wisteria_hanger",
                "frost_wisteria_leaf_pile", "frost_wisteria_hanger", "lavender_wisteria_leaf_pile", "lavender_wisteria_hanger",
                "grass_plant", "grass_flowering", "short_grass", "tall_grass", "fern", "bush", "shamrock", "malt_sprig"
        };
        entry(BlockTags.REPLACEABLE, (Object[]) replaceable);
        entry(BlockTags.REPLACEABLE_BY_TREES, (Object[]) replaceable);
        entry(BlockTags.SAPLINGS, "aurel_sapling", "mother_aurel_sapling", "rose_wisteria_sapling", "frost_wisteria_sapling", "lavender_wisteria_sapling", "menth_sapling");
        entry(BlockTags.SMALL_FLOWERS, "ancient_flower", "ataraxia", "cloudsbluff", "drigean", "luminar");
        entry(BlockTags.STAIRS, "levita_brick_stairs", "floestone_brick_stairs", "mossy_floestone_brick_stairs", "floestone_stairs", "mossy_floestone_stairs", "cobbled_floestone_stairs");
        woods(BlockTags.STANDING_SIGNS, "%s_sign");
        entry(BlockTags.STONE_BUTTONS, "floestone_button");
        entry(BlockTags.STONE_PRESSURE_PLATES, "floestone_pressure_plate");
        entry(BlockTags.SWORD_EFFICIENT, "flaxweave_cushion", "flaxweave_cushion_slab");
        entry(BlockTags.TALL_FLOWERS, "wild_flax");
        entry(BlockTags.VALID_SPAWN, "highlands_grass");
        woods(BlockTags.WALL_HANGING_SIGNS, "%s_wall_hanging_sign");
        entry(BlockTags.WALL_POST_OVERRIDE, "cherine_torch");
        woods(BlockTags.WALL_SIGNS, "%s_wall_sign");
        entry(BlockTags.WALLS,
                "floestone_wall", "cobbled_floestone_wall", "mossy_floestone_wall", "heliolith_wall",
                "floestone_brick_wall", "mossy_floestone_brick_wall", "burnished_stone_wall",
                "calcite_tiles_wall", "bloomed_calcite_tiles_wall");
        woods(BlockTags.WOODEN_BUTTONS, "%s_button");
        woods(BlockTags.WOODEN_DOORS, "%s_door");
        woods(BlockTags.WOODEN_FENCES, "%s_fence");
        woods(BlockTags.WOODEN_PRESSURE_PLATES, "%s_pressure_plate");
        woods(BlockTags.WOODEN_SLABS, "%s_slab");
        woods(BlockTags.WOODEN_STAIRS, "%s_stairs");
        woods(BlockTags.WOODEN_TRAPDOORS, "%s_trapdoor");

        entry(ParadiseLostBlockTags.ANIMALS_PREFERRED, "highlands_grass", "frozen_grass");
        entry(ParadiseLostBlockTags.AUREL_LOGS,
                "aurel_log", "mottled_aurel_log", "mottled_aurel_wood", "aurel_wood", "stripped_aurel_log", "stripped_aurel_wood");
        entry(ParadiseLostBlockTags.CLOUDS, "cold_cloud", "blue_cloud", "golden_cloud", "green_cloud");
        entry(ParadiseLostBlockTags.DECAYING_FLOATERS, "levita", "levita_ore");
        entry(ParadiseLostBlockTags.FAST_FLOATERS, "levitator");
        entry(ParadiseLostBlockTags.HANGERS, "frost_wisteria_hanger", "rose_wisteria_hanger", "lavender_wisteria_hanger");
        entry(ParadiseLostBlockTags.HOLLOW_LOGS, "mottled_aurel_fallen_log");
        this.tag(ParadiseLostBlockTags.HURTABLE_FLOATERS);
        entry(ParadiseLostBlockTags.INCUBATOR_WARMER_BEDS, "amadrys_bundle", "thatch", "minecraft:hay_block");
        entry(ParadiseLostBlockTags.INCUBATOR_WARMER_LIGHTS, "cherine_torch", "cherine_lantern");
        entry(ParadiseLostBlockTags.MENTH_LOGS, "menth_log", "menth_wood", "stripped_menth_log", "stripped_menth_wood");
        entry(ParadiseLostBlockTags.MOTHER_AUREL_LOGS, "mother_aurel_log", "mother_aurel_wood", "stripped_mother_aurel_log", "stripped_mother_aurel_wood");
        this.tag(ParadiseLostBlockTags.NON_FLOATERS);
        entry(ParadiseLostBlockTags.FUNGI_CLINGABLES, BlockTags.LOGS, BlockTags.PLANKS);
        entry(ParadiseLostBlockTags.GENERIC_VALID_GROUND, BlockTags.LOGS, ParadiseLostBlockTags.DIRT_BLOCKS, "mossy_floestone");
        entry(ParadiseLostBlockTags.SWEDROOT_PLANTABLE, ParadiseLostBlockTags.DIRT_BLOCKS, "packed_swedroot");
        entry(ParadiseLostBlockTags.PUSH_FLOATERS, "levitator");
        entry(ParadiseLostBlockTags.STRUCTURES_AVOID,
                BlockTags.AIR, BlockTags.LEAVES, ParadiseLostBlockTags.CLOUDS,
                "metamorphic_shell", "surtrum", "surtrum_air");
        entry(ParadiseLostBlockTags.WISTERIA_LOGS, "wisteria_log", "wisteria_wood", "stripped_wisteria_log", "stripped_wisteria_wood");
        entry(ParadiseLostBlockTags.BASE_REPLACEABLES, ParadiseLostBlockTags.NATURAL_STONE, ParadiseLostBlockTags.DIRT_BLOCKS);
        entry(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE, "floestone", "mossy_floestone");
        entry(ParadiseLostBlockTags.CLOUD_CARVER_REPLACEABLES, "minecraft:air", "minecraft:void_air", "minecraft:cave_air");
        entry(ParadiseLostBlockTags.DIRT_BLOCKS, "highlands_grass", "dirt", "coarse_dirt", "permafrost", "frozen_grass", "liverwort");
        entry(ParadiseLostBlockTags.FLUID_REPLACEABLES, ParadiseLostBlockTags.NATURAL_STONE, ParadiseLostBlockTags.DIRT_BLOCKS);
        entry(ParadiseLostBlockTags.NATURAL_STONE, "floestone", "cobbled_floestone", "mossy_floestone", "golden_mossy_floestone", "heliolith");
    }

    private void woods(TagKey<Block> tag, String pattern) {
        Object[] ids = new Object[WOODS.length];
        for (int i = 0; i < WOODS.length; i++) {
            ids[i] = pattern.formatted(WOODS[i]);
        }
        entry(tag, ids);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void entry(TagKey<Block> tag, Object... parts) {
        TagAppender appender = this.tag(tag);
        for (Object part : parts) {
            if (part instanceof TagKey<?> ref) {
                appender.addTag((TagKey) ref);
            } else {
                String id = (String) part;
                appender.addOptional(id.indexOf(':') >= 0 ? ResourceLocation.parse(id) : ModConstants.id(id));
            }
        }
    }

    private static TagKey<Block> mc(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(path));
    }
}
