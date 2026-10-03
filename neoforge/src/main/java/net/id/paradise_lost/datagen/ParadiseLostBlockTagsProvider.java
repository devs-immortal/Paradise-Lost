package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.BlockRegistry.SignSet;
import net.id.paradise_lost.registry.BlockRegistry.WoodBlockSet;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.id.paradise_lost.registry.BlockRegistry.*;

public class ParadiseLostBlockTagsProvider extends BlockTagsProvider {
    private static final TagKey<Block> ANIMALS_SPAWNABLE_ON = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("animals_spawnable_on"));
    private static final TagKey<Block> CONVERTABLE_TO_MUD = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("convertable_to_mud"));
    private static final TagKey<Block> MOSS_REPLACEABLE = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("moss_replaceable"));

    private static final List<WoodBlockSet> WOOD_SETS = List.of(
            AUREL_WOODSTUFF, MOTHER_AUREL_WOODSTUFF, MENTH_WOODSTUFF, WISTERIA_WOODSTUFF
    );
    private static final List<SignSet> SIGN_SETS = List.of(
            AUREL_SIGNS, MOTHER_AUREL_SIGNS, MENTH_SIGNS, WISTERIA_SIGNS
    );

    public ParadiseLostBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Blocks.BOOKSHELVES).add(AUREL_BOOKSHELF.get());
        tag(Tags.Blocks.CHAINS).add(OLVITE_CHAIN.get());
        tag(Tags.Blocks.COBBLESTONES).add(COBBLED_FLOESTONE.get());
        tag(Tags.Blocks.ORES).add(CHERINE_ORE.get(), OLVITE_ORE.get(), LEVITA_ORE.get());
        tag(Tags.Blocks.STONES).add(FLOESTONE.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).add(
                FLAXWEAVE_CUSHION.get(), AMADRYS_BUNDLE.get(), CHERINE_BLOCK.get(), OLVITE_BLOCK.get(),
                REFINED_SURTRUM_BLOCK.get(), NITRA_BUNCH.get()
        );

        addWoodSetVanillaTags();
        addSignVanillaTags();

        tag(ANIMALS_SPAWNABLE_ON).add(HIGHLANDS_GRASS.get(), FROZEN_GRASS.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(OLVITE_BLOCK.get(), REFINED_SURTRUM_BLOCK.get());
        tag(BlockTags.CAMPFIRES).add(CHERINE_CAMPFIRE.get());
        tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(LIVERWORT_CARPET.get());
        tag(CONVERTABLE_TO_MUD).add(DIRT.get(), COARSE_DIRT.get());
        tag(BlockTags.CROPS).add(AMADRYS.get(), FLAX.get(), SWEDROOT.get(), NITRA.get());
        tag(BlockTags.DIRT).add(
                DIRT.get(), COARSE_DIRT.get(), PERMAFROST.get(), HIGHLANDS_GRASS.get(), FROZEN_GRASS.get(),
                SUSPICIOUS_DIRT.get()
        );

        tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(AUREL_BOOKSHELF.get());

        tag(BlockTags.FLOWER_POTS).add(
                CALCITE_FLOWER_POT.get(),
                POTTED_ANCIENT_FLOWER.get(), POTTED_ATARAXIA.get(), POTTED_CLOUDSBLUFF.get(),
                POTTED_DRIGEAN.get(), POTTED_LUMINAR.get(),
                POTTED_ROSE_WISTERIA_SAPLING.get(), POTTED_FROST_WISTERIA_SAPLING.get(),
                POTTED_LAVENDER_WISTERIA_SAPLING.get(), POTTED_FERN.get()
        );
        for (WoodBlockSet set : WOOD_SETS) {
            if (set.flowerPot() != null) {
                tag(BlockTags.FLOWER_POTS).add(set.flowerPot().get());
            }
        }

        tag(BlockTags.LEAVES).add(
                ROSE_WISTERIA_LEAVES.get(), FROST_WISTERIA_LEAVES.get(), LAVENDER_WISTERIA_LEAVES.get()
        );
        for (WoodBlockSet set : WOOD_SETS) {
            if (set.leaves() != null) {
                tag(BlockTags.LEAVES).add(set.leaves().get());
            }
        }

        tag(BlockTags.LOGS)
                .addTag(ParadiseLostBlockTags.AUREL_LOGS)
                .addTag(ParadiseLostBlockTags.MOTHER_AUREL_LOGS)
                .addTag(ParadiseLostBlockTags.WISTERIA_LOGS)
                .addTag(ParadiseLostBlockTags.MENTH_LOGS)
                .addTag(ParadiseLostBlockTags.HOLLOW_LOGS);
        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(ParadiseLostBlockTags.AUREL_LOGS)
                .addTag(ParadiseLostBlockTags.MOTHER_AUREL_LOGS)
                .addTag(ParadiseLostBlockTags.WISTERIA_LOGS)
                .addTag(ParadiseLostBlockTags.MENTH_LOGS)
                .addTag(ParadiseLostBlockTags.HOLLOW_LOGS);

        tag(BlockTags.MAINTAINS_FARMLAND).add(AMADRYS.get(), FLAX.get(), NITRA.get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(
                PACKED_SWEDROOT.get(), CHERINE_CAMPFIRE.get(), AUREL_BOOKSHELF.get(), BLACKCURRANT_BUSH.get(),
                INCUBATOR.get(), FOOD_BOWL.get(), TREE_TAP.get(),
                ROOTCAP_BLOCK.get(), BROWN_SPORECAP_BLOCK.get(), PINK_SPORECAP_BLOCK.get(),
                THATCH_SET.block().get(), THATCH_SET.stairs().get(), THATCH_SET.slab().get(),
                NEST.get()
        ).addTag(ParadiseLostBlockTags.HOLLOW_LOGS);

        tag(BlockTags.MINEABLE_WITH_HOE).add(
                LIVERWORT.get(), LIVERWORT_CARPET.get(),
                ROSE_WISTERIA_LEAF_PILE.get(), FROST_WISTERIA_LEAF_PILE.get(), LAVENDER_WISTERIA_LEAF_PILE.get(),
                AUREL_LEAF_PILE.get(),
                AMADRYS_BUNDLE.get(), NITRA_BUNCH.get(),
                ROSE_WISTERIA_LEAVES.get(), FROST_WISTERIA_LEAVES.get(), LAVENDER_WISTERIA_LEAVES.get()
        ).addTag(ParadiseLostBlockTags.HANGERS);
        for (WoodBlockSet set : WOOD_SETS) {
            if (set.leaves() != null) {
                tag(BlockTags.MINEABLE_WITH_HOE).add(set.leaves().get());
            }
        }

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                CHERINE_ORE.get(), OLVITE_ORE.get(), FLOESTONE_REDSTONE_ORE.get(), SURTRUM.get(),
                METAMORPHIC_SHELL.get(), LEVITA_ORE.get(),
                CHERINE_BLOCK.get(), OLVITE_BLOCK.get(), REFINED_SURTRUM_BLOCK.get(), LEVITATOR.get(),
                OLVITE_CHAIN.get(), CHERINE_LANTERN.get(), BLOOMED_CALCITE.get(),
                FLOESTONE.get(), FLOESTONE_SLAB.get(), FLOESTONE_STAIRS.get(),
                SMOOTH_FLOESTONE.get(), SMOOTH_FLOESTONE_SLAB.get(), SMOOTH_FLOESTONE_STAIRS.get(),
                COBBLED_FLOESTONE.get(), COBBLED_FLOESTONE_SLAB.get(), COBBLED_FLOESTONE_STAIRS.get(),
                MOSSY_FLOESTONE.get(), GOLDEN_MOSSY_FLOESTONE.get(), MOSSY_FLOESTONE_SLAB.get(), MOSSY_FLOESTONE_STAIRS.get(),
                HELIOLITH.get(), HELIOLITH_SLAB.get(), HELIOLITH_STAIRS.get(),
                FLOESTONE_BRICK.get(), MOSSY_FLOESTONE_BRICK.get(), CHISELED_FLOESTONE.get(),
                FLOESTONE_BRICK_SLAB.get(), MOSSY_FLOESTONE_BRICK_SLAB.get(),
                FLOESTONE_BRICK_STAIRS.get(), MOSSY_FLOESTONE_BRICK_STAIRS.get(),
                SMOOTH_HELIOLITH.get(), SMOOTH_HELIOLITH_SLAB.get(), SMOOTH_HELIOLITH_STAIRS.get(),
                CHISELED_LEVITA_BRICK.get(),
                BURNISHED_STONE_SET.block().get(), BURNISHED_STONE_SET.stairs().get(), BURNISHED_STONE_SET.slab().get(),
                BURNISHED_STONE_PLAQUE.get(), BURNISHED_STONE_SCRIPT.get(),
                GOLDEN_AMBER_TILE.get(), GOLDEN_AMBER_TILE_SLAB.get(), GOLDEN_AMBER_TILE_STAIRS.get(),
                FLOESTONE_BUTTON.get(), FLOESTONE_PRESSURE_PLATE.get(), OLVITE_PRESSURE_PLATE.get(), GOLDEN_AMBER_BARS.get(),
                CALCITE_TILES_SET.block().get(), CALCITE_TILES_SET.stairs().get(), CALCITE_TILES_SET.slab().get(),
                BLOOMED_CALCITE_TILES_SET.block().get(), BLOOMED_CALCITE_TILES_SET.stairs().get(), BLOOMED_CALCITE_TILES_SET.slab().get()
        );
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                LEVITA_BRICK_SET.block().get(), LEVITA_BRICK_SET.stairs().get(), LEVITA_BRICK_SET.slab().get()
        );

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                HIGHLANDS_GRASS.get(), FROZEN_GRASS.get(), DIRT.get(), COARSE_DIRT.get(), PERMAFROST.get(),
                LEVITA.get(), FARMLAND.get(), DIRT_PATH.get(), PERMAFROST_PATH.get(), SUSPICIOUS_DIRT.get(),
                LEVITA_BRICK_SET.block().get(), LEVITA_BRICK_SET.stairs().get(), LEVITA_BRICK_SET.slab().get(),
                CHISELED_LEVITA_BRICK.get()
        );

        tag(MOSS_REPLACEABLE).add(
                DIRT.get(), COARSE_DIRT.get(), FLOESTONE.get(), COBBLED_FLOESTONE.get(), MOSSY_FLOESTONE.get()
        );

        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(METAMORPHIC_SHELL.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                FLOESTONE_REDSTONE_ORE.get(), SURTRUM.get(), LEVITA_ORE.get(), REFINED_SURTRUM_BLOCK.get()
        );
        tag(BlockTags.NEEDS_STONE_TOOL).add(OLVITE_ORE.get(), OLVITE_BLOCK.get(), LEVITATOR.get());
        tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(
                LEVITA.get(), FLOESTONE.get(), COBBLED_FLOESTONE.get(), MOSSY_FLOESTONE.get(), HELIOLITH.get(),
                CHERINE_ORE.get(), OLVITE_ORE.get(), LEVITA_ORE.get()
        );

        tag(BlockTags.PRESSURE_PLATES).add(OLVITE_PRESSURE_PLATE.get());
        tag(BlockTags.RAILS).add(LEVITA_RAIL.get());
        tag(BlockTags.REDSTONE_ORES).add(FLOESTONE_REDSTONE_ORE.get());

        addReplaceableTags();

        tag(BlockTags.SAPLINGS).add(
                ROSE_WISTERIA_SAPLING.get(), FROST_WISTERIA_SAPLING.get(), LAVENDER_WISTERIA_SAPLING.get()
        );
        for (WoodBlockSet set : WOOD_SETS) {
            if (set.sapling() != null) {
                tag(BlockTags.SAPLINGS).add(set.sapling().get());
            }
        }

        tag(BlockTags.SMALL_FLOWERS).add(
                ANCIENT_FLOWER.get(), ATARAXIA.get(), CLOUDSBLUFF.get(), DRIGEAN.get(), LUMINAR.get()
        );
        tag(BlockTags.STAIRS).add(
                LEVITA_BRICK_SET.stairs().get(),
                FLOESTONE_BRICK_STAIRS.get(), MOSSY_FLOESTONE_BRICK_STAIRS.get(),
                FLOESTONE_STAIRS.get(), MOSSY_FLOESTONE_STAIRS.get(), COBBLED_FLOESTONE_STAIRS.get()
        );
        tag(BlockTags.STONE_BUTTONS).add(FLOESTONE_BUTTON.get());
        tag(BlockTags.STONE_PRESSURE_PLATES).add(FLOESTONE_PRESSURE_PLATE.get());
        tag(BlockTags.SWORD_EFFICIENT).add(FLAXWEAVE_CUSHION.get(), FLAXWEAVE_CUSHION_SLAB.get());
        tag(BlockTags.TALL_FLOWERS).add(WILD_FLAX.get());
        tag(BlockTags.VALID_SPAWN).add(HIGHLANDS_GRASS.get());
        tag(BlockTags.WALL_POST_OVERRIDE).add(CHERINE_TORCH.get());
        tag(BlockTags.WALLS).add(
                FLOESTONE_WALL.get(), COBBLED_FLOESTONE_WALL.get(), MOSSY_FLOESTONE_WALL.get(), HELIOLITH_WALL.get(),
                FLOESTONE_BRICK_WALL.get(), MOSSY_FLOESTONE_BRICK_WALL.get(), BURNISHED_STONE_WALL.get(),
                CALCITE_TILES_WALL.get(), BLOOMED_CALCITE_TILES_WALL.get()
        );

        tag(ParadiseLostBlockTags.ANIMALS_PREFERRED).add(HIGHLANDS_GRASS.get(), FROZEN_GRASS.get());
        tag(ParadiseLostBlockTags.AUREL_LOGS).add(
                AUREL_WOODSTUFF.log().get(), MOTTLED_AUREL_LOG.get(), MOTTLED_AUREL_WOOD.get(),
                AUREL_WOODSTUFF.wood().get(), AUREL_WOODSTUFF.strippedLog().get(), AUREL_WOODSTUFF.strippedWood().get()
        );
        tag(ParadiseLostBlockTags.CLOUDS).add(
                COLD_CLOUD.get(), BLUE_CLOUD.get(), GOLDEN_CLOUD.get(), GREEN_CLOUD.get()
        );
        tag(ParadiseLostBlockTags.DECAYING_FLOATERS).add(LEVITA.get(), LEVITA_ORE.get());
        tag(ParadiseLostBlockTags.FAST_FLOATERS).add(LEVITATOR.get());
        tag(ParadiseLostBlockTags.HANGERS).add(
                FROST_WISTERIA_HANGER.get(), ROSE_WISTERIA_HANGER.get(), LAVENDER_WISTERIA_HANGER.get()
        );
        tag(ParadiseLostBlockTags.HOLLOW_LOGS).add(MOTTLED_AUREL_FALLEN_LOG.get());
        tag(ParadiseLostBlockTags.HURTABLE_FLOATERS);
        tag(ParadiseLostBlockTags.INCUBATOR_WARMER_BEDS).add(
                AMADRYS_BUNDLE.get(), THATCH_SET.block().get(), Blocks.HAY_BLOCK
        );
        tag(ParadiseLostBlockTags.INCUBATOR_WARMER_LIGHTS).add(CHERINE_TORCH.get(), CHERINE_LANTERN.get());
        tag(ParadiseLostBlockTags.MENTH_LOGS).add(
                MENTH_WOODSTUFF.log().get(), MENTH_WOODSTUFF.wood().get(),
                MENTH_WOODSTUFF.strippedLog().get(), MENTH_WOODSTUFF.strippedWood().get()
        );
        tag(ParadiseLostBlockTags.MOTHER_AUREL_LOGS).add(
                MOTHER_AUREL_WOODSTUFF.log().get(), MOTHER_AUREL_WOODSTUFF.wood().get(),
                MOTHER_AUREL_WOODSTUFF.strippedLog().get(), MOTHER_AUREL_WOODSTUFF.strippedWood().get()
        );
        tag(ParadiseLostBlockTags.NON_FLOATERS);
        tag(ParadiseLostBlockTags.FUNGI_CLINGABLES)
                .addTag(BlockTags.LOGS)
                .addTag(BlockTags.PLANKS);
        tag(ParadiseLostBlockTags.GENERIC_VALID_GROUND)
                .addTag(BlockTags.LOGS)
                .addTag(ParadiseLostBlockTags.DIRT_BLOCKS)
                .add(MOSSY_FLOESTONE.get());
        tag(ParadiseLostBlockTags.SWEDROOT_PLANTABLE)
                .addTag(ParadiseLostBlockTags.DIRT_BLOCKS)
                .add(PACKED_SWEDROOT.get());
        tag(ParadiseLostBlockTags.PUSH_FLOATERS).add(LEVITATOR.get());
        tag(ParadiseLostBlockTags.STRUCTURES_AVOID)
                .addTag(BlockTags.AIR)
                .addTag(BlockTags.LEAVES)
                .addTag(ParadiseLostBlockTags.CLOUDS)
                .add(METAMORPHIC_SHELL.get(), SURTRUM.get(), SURTRUM_AIR.get());
        tag(ParadiseLostBlockTags.WISTERIA_LOGS).add(
                WISTERIA_WOODSTUFF.log().get(), WISTERIA_WOODSTUFF.wood().get(),
                WISTERIA_WOODSTUFF.strippedLog().get(), WISTERIA_WOODSTUFF.strippedWood().get()
        );
        tag(ParadiseLostBlockTags.BASE_REPLACEABLES)
                .addTag(ParadiseLostBlockTags.NATURAL_STONE)
                .addTag(ParadiseLostBlockTags.DIRT_BLOCKS);
        tag(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE).add(FLOESTONE.get(), MOSSY_FLOESTONE.get());
        tag(ParadiseLostBlockTags.CLOUD_CARVER_REPLACEABLES).add(
                Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR
        );
        tag(ParadiseLostBlockTags.DIRT_BLOCKS).add(
                HIGHLANDS_GRASS.get(), DIRT.get(), COARSE_DIRT.get(), PERMAFROST.get(), FROZEN_GRASS.get(),
                LIVERWORT.get()
        );
        tag(ParadiseLostBlockTags.FLUID_REPLACEABLES)
                .addTag(ParadiseLostBlockTags.NATURAL_STONE)
                .addTag(ParadiseLostBlockTags.DIRT_BLOCKS);
        tag(ParadiseLostBlockTags.NATURAL_STONE).add(
                FLOESTONE.get(), COBBLED_FLOESTONE.get(), MOSSY_FLOESTONE.get(), GOLDEN_MOSSY_FLOESTONE.get(),
                HELIOLITH.get()
        );
    }

    private void addWoodSetVanillaTags() {
        for (WoodBlockSet set : WOOD_SETS) {
            tag(Tags.Blocks.STRIPPED_LOGS).add(set.strippedLog().get());
            tag(Tags.Blocks.STRIPPED_WOODS).add(set.strippedWood().get());
            tag(BlockTags.PLANKS).add(set.plank().get());
            tag(BlockTags.FENCE_GATES).add(set.fenceGate().get());
            tag(BlockTags.WOODEN_BUTTONS).add(set.button().get());
            tag(BlockTags.WOODEN_DOORS).add(set.door().get());
            tag(BlockTags.WOODEN_FENCES).add(set.fence().get());
            tag(BlockTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().get());
            tag(BlockTags.WOODEN_SLABS).add(set.plankSlab().get());
            tag(BlockTags.WOODEN_STAIRS).add(set.plankStairs().get());
            tag(BlockTags.WOODEN_TRAPDOORS).add(set.trapdoor().get());
        }
    }

    private void addSignVanillaTags() {
        for (SignSet signs : SIGN_SETS) {
            tag(BlockTags.CEILING_HANGING_SIGNS).add(signs.hangingSign().get());
            tag(BlockTags.STANDING_SIGNS).add(signs.sign().get());
            tag(BlockTags.WALL_HANGING_SIGNS).add(signs.wallHangingSign().get());
            tag(BlockTags.WALL_SIGNS).add(signs.wallSign().get());
        }
    }

    private void addReplaceableTags() {
        Block[] replaceable = {
                AUREL_LEAF_PILE.get(),
                ROSE_WISTERIA_LEAF_PILE.get(), ROSE_WISTERIA_HANGER.get(),
                FROST_WISTERIA_LEAF_PILE.get(), FROST_WISTERIA_HANGER.get(),
                LAVENDER_WISTERIA_LEAF_PILE.get(), LAVENDER_WISTERIA_HANGER.get(),
                GRASS.get(), GRASS_FLOWERING.get(), SHORT_GRASS.get(), TALL_GRASS.get(),
                FERN.get(), BUSH.get(), SHAMROCK.get(), MALT_SPRIG.get()
        };
        for (Block block : replaceable) {
            tag(BlockTags.REPLACEABLE).add(block);
            tag(BlockTags.REPLACEABLE_BY_TREES).add(block);
        }
    }
}
