package net.id.paradiselost.world.feature.configured_features;

import com.google.common.collect.ImmutableList;
import net.id.paradiselost.blocks.natural.tree.FruitingLeavesBlock;
import net.id.paradiselost.world.feature.ParadiseLostFeatures;
import net.id.paradiselost.world.feature.configs.LongFeatureConfig;
import net.id.paradiselost.world.feature.configs.ProjectedOrganicCoverConfig;
import net.id.paradiselost.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.paradiselost.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.paradiselost.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;

import java.util.OptionalInt;

import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;

public class ParadiseLostTreeConfiguredFeatures extends ParadiseLostConfiguredFeatures {
    // Default
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> AUREL_TREE = register("aurel_tree", Feature.TREE, Configs.AUREL_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> MOTHER_AUREL_TREE = register("mother_aurel_tree", Feature.TREE, Configs.MOTHER_AUREL_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> ORANGE_TREE = register("orange_tree", Feature.TREE, Configs.ORANGE_TREE_SAPLING_CONFIG);
    // Wisteria
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> ROSE_WISTERIA_TREE = register("rose_wisteria_tree", Feature.TREE, Configs.ROSE_WISTERIA_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> LAVENDER_WISTERIA_TREE = register("lavender_wisteria_tree", Feature.TREE, Configs.LAVENDER_WISTERIA_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> FROST_WISTERIA_TREE = register("frost_wisteria_tree", Feature.TREE, Configs.FROST_WISTERIA_CONFIG);
    // Variants
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> WILD_ORANGE_TREE = register("wild_orange_tree", Feature.TREE, Configs.ORANGE_TREE_WILD_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> AUREL_SHRUB = register("aurel_shrub", Feature.TREE, Configs.AUREL_SHRUB_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> MOTTLED_AUREL = register("mottled_aurel_tree", Feature.TREE, Configs.MOTTLED_AUREL_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> DWARF_MOTTLED_AUREL = register("dwarf_mottled_aurel_tree", Feature.TREE, Configs.DWARF_MOTTLED_AUREL_CONFIG);
    // Fancy
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> FANCY_ROSE_WISTERIA_TREE = register("fancy_rose_wisteria_tree", Feature.TREE, Configs.FANCY_ROSE_WISTERIA_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> FANCY_LAVENDER_WISTERIA_TREE = register("fancy_lavender_wisteria_tree", Feature.TREE, Configs.FANCY_LAVENDER_WISTERIA_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> FANCY_FROST_WISTERIA_TREE = register("fancy_frost_wisteria_tree", Feature.TREE, Configs.FANCY_FROST_WISTERIA_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> FANCY_AUREL_TREE = register("fancy_aurel_tree", Feature.TREE, Configs.FANCY_AUREL_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> THICKET_AUREL_TREE = register("thicket_aurel_tree", Feature.TREE, Configs.THICKET_AUREL_CONFIG);
    // Fallen leaves
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> FALLEN_LEAVES = register("fallen_leaves", Feature.RANDOM_PATCH, Configs.FALLEN_LEAVES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> ALT_FALLEN_LEAVES = register("alt_fallen_leaves", Feature.RANDOM_PATCH, Configs.FALLEN_LEAVES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2));
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2));
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", Feature.RANDOM_PATCH, Configs.FALLEN_LEAVES_CONFIG);
    // Logs
    public static final RegistryEntry<ConfiguredFeature<LongFeatureConfig, ?>> THICKET_FALLEN_LOG = register("thicket_fallen_log", ParadiseLostFeatures.FALLEN_PILLAR_FEATURE, new LongFeatureConfig(UniformIntProvider.create(3, 6), BlockStateProvider.of(AUREL_LOG), BlockStateProvider.of(LIVERWORT_CARPET), BlockStateProvider.of(LIVERWORT_CARPET), 0.5F, 0.35F, Configs.GENERIC_FLOOR_WHITELIST));

    public static final RegistryEntry<ConfiguredFeature<LongFeatureConfig, ?>> MOTTLED_FALLEN_LOG = register("mottled_fallen_log", ParadiseLostFeatures.FALLEN_PILLAR_FEATURE, new LongFeatureConfig(UniformIntProvider.create(3, 5), BlockStateProvider.of(MOTTLED_AUREL_LOG), BlockStateProvider.of(GRASS), BlockStateProvider.of(ROOTCAP), 0.3F, 0.15F, Configs.GENERIC_FLOOR_WHITELIST));
    public static final RegistryEntry<ConfiguredFeature<LongFeatureConfig, ?>> MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", ParadiseLostFeatures.FALLEN_PILLAR_FEATURE, new LongFeatureConfig(UniformIntProvider.create(3, 5), BlockStateProvider.of(MOTTLED_AUREL_FALLEN_LOG), BlockStateProvider.of(GRASS_FLOWERING), BlockStateProvider.of(ROOTCAP), 0.4F, 0.25F, Configs.GENERIC_FLOOR_WHITELIST));

    public static final RegistryEntry<ConfiguredFeature<LongFeatureConfig, ?>> SHIELD_STUMPS = register("shield_stumps", ParadiseLostFeatures.PILLAR_FEATURE, new LongFeatureConfig(UniformIntProvider.create(1, 2), BlockStateProvider.of(MOTTLED_AUREL_LOG), BlockStateProvider.of(MOTTLED_AUREL_FALLEN_LOG), BlockStateProvider.of(ROOTCAP), 0.1F, 0.225F, Configs.GENERIC_FLOOR_WHITELIST));
    public static final RegistryEntry<ConfiguredFeature<LongFeatureConfig, ?>> SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", ParadiseLostFeatures.PILLAR_FEATURE, new LongFeatureConfig(ConstantIntProvider.create(1), BlockStateProvider.of(MOTTLED_AUREL_FALLEN_LOG), BlockStateProvider.of(MOTTLED_AUREL_FALLEN_LOG), BlockStateProvider.of(ROOTCAP), 0.015F, 0.3F, Configs.GENERIC_FLOOR_WHITELIST));
    // Assortments
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> SCATTERED_TREES = register("scattered_trees", Feature.RANDOM_SELECTOR, Assortments.SCATTERED_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> SHIELD_TREES = register("shield_trees", Feature.RANDOM_SELECTOR, Assortments.SHIELD_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> DENSE_SHIELD_TREES = register("dense_shield_trees", Feature.RANDOM_SELECTOR, Assortments.DENSE_SHIELD_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> PLATEAU_TREES = register("plateau_trees", Feature.RANDOM_SELECTOR, Assortments.PLATEAU_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> MIXED_TREES = register("mixed_trees", Feature.RANDOM_SELECTOR, Assortments.MIXED_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> SPARSE_TREES = register("sparse_trees", Feature.RANDOM_SELECTOR, Assortments.SPARSE_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> THICKET_TREES = register("thicket_trees", Feature.RANDOM_SELECTOR, Assortments.THICKET_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> THICKET_MOTHER_AUREL_TREES = register("thicket_mother_aurel_trees", Feature.RANDOM_SELECTOR, Assortments.RARE_MOTHER_AUREL_TREES_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> RAINBOW_FOREST_TREES = register("wisteria_woods_trees", Feature.RANDOM_SELECTOR, Assortments.RAINBOW_FOREST_CONFIG);

    public static void init() {
    }

    private static class Configs extends ParadiseLostConfiguredFeatures.Configs {
        private static final DefaultFeatureConfig DEFAULT_CONFIG = new DefaultFeatureConfig();

        private static TreeFeatureConfig generateTree(BlockState logState, BlockState foliageState,
                                                      BlockState dirtState, TrunkPlacer trunkPlacer,
                                                      FoliagePlacer foliagePlacer, FeatureSize minimumSize,
                                                      boolean ignoreVines, boolean forceDirt) {
            return generateTree(
                    BlockStateProvider.of(logState),
                    BlockStateProvider.of(foliageState),
                    BlockStateProvider.of(dirtState),
                    trunkPlacer, foliagePlacer, minimumSize,
                    ignoreVines, forceDirt
            );
        }

        private static TreeFeatureConfig generateTree(BlockStateProvider logProvider, BlockStateProvider foliageProvider,
                                                      BlockStateProvider dirtProvider, TrunkPlacer trunkPlacer,
                                                      FoliagePlacer foliagePlacer, FeatureSize minimumSize,
                                                      boolean ignoreVines, boolean forceDirt) {
            TreeFeatureConfig.Builder builder = new TreeFeatureConfig.Builder(
                    logProvider, trunkPlacer, foliageProvider, foliagePlacer, minimumSize
            ).dirtProvider(dirtProvider);

            if (ignoreVines) {
                builder = builder.ignoreVines();
            }
            if (forceDirt) {
                builder = builder.forceDirt();
            }
            return builder.build();
        }

        // Fallen leaves
        private static final RandomPatchFeatureConfig FALLEN_LEAVES_CONFIG = blockPatch(96, 10, 7, new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(AUREL_LEAF_PILE.getDefaultState(), 8)
                        .add(AUREL_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)
        ));

        /*
        Tree feature configs
         */
        //Aurels
        private static final TreeFeatureConfig AUREL_CONFIG = generateTree(
                AUREL_LOG.getDefaultState(), AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new StraightTrunkPlacer(4, 2, 0),
                new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );
        private static final TreeFeatureConfig FANCY_AUREL_CONFIG = generateTree(
                AUREL_LOG.getDefaultState(), AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new LargeOakTrunkPlacer(4, 11, 0),
                new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)),
                true, false
        );
        private static final TreeFeatureConfig THICKET_AUREL_CONFIG = generateTree(
                AUREL_LOG.getDefaultState(), AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new LargeOakTrunkPlacer(12, 4, 1),
                new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(4), 3),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)),
                true, false
        );
        private static final TreeFeatureConfig AUREL_SHRUB_CONFIG = generateTree(
                AUREL_LOG.getDefaultState(), AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new StraightTrunkPlacer(1, 1, 0),
                new BlobFoliagePlacer(UniformIntProvider.create(1, 3), ConstantIntProvider.create(0), 1),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );
        private static final TreeFeatureConfig MOTTLED_AUREL_CONFIG = generateTree(
                MOTTLED_AUREL_LOG.getDefaultState(), AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new OvergrownTrunkPlacer(5, 10, 0, BlockStateProvider.of(ROOTCAP), 1 / 14F),
                new BlobFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );
        private static final TreeFeatureConfig DWARF_MOTTLED_AUREL_CONFIG = generateTree(
                MOTTLED_AUREL_LOG.getDefaultState(), AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new BendingTrunkPlacer(5, 3, 2, 4, UniformIntProvider.create(1, 3)),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 68),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );

        //Fruit trees
        private static final TreeFeatureConfig ORANGE_TREE_SAPLING_CONFIG = generateTree(
                ORANGE_LOG.getDefaultState(), ORANGE_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63),
                new TwoLayersFeatureSize(1, 0, 1),
                false, false
        );

        private static final BlockState ORANGE_LEAVES_BASIC = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true);
        private static final BlockState ORANGE_LEAVES_FLOWERING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 1);
        private static final BlockState ORANGE_LEAVES_FRUITING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 2);

        private static final TreeFeatureConfig ORANGE_TREE_WILD_CONFIG = generateTree(
                BlockStateProvider.of(ORANGE_LOG),
                new WeightedBlockStateProvider(
                        DataPool.<BlockState>builder()
                                .add(ORANGE_LEAVES_BASIC, 2)
                                .add(ORANGE_LEAVES_FLOWERING, 2)
                                .add(ORANGE_LEAVES_FRUITING, 1)
                ),
                BlockStateProvider.of(DIRT),
                new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63),
                new TwoLayersFeatureSize(1, 0, 1),
                false, false
        );

        //Wisterias
        private static final TreeFeatureConfig ROSE_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), ROSE_WISTERIA_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(3, 4),
                        UniformIntProvider.create(3, 4), // branch count
                        UniformFloatProvider.create(1.5F, 3F), // branch height
                        UniformFloatProvider.create(6F, 10F), // branch width
                        4, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        private static final TreeFeatureConfig LAVENDER_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), LAVENDER_WISTERIA_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(3, 4),
                        UniformIntProvider.create(3, 4), // branch count
                        UniformFloatProvider.create(1.5F, 3F), // branch height
                        UniformFloatProvider.create(6F, 10F), // branch width
                        4, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        private static final TreeFeatureConfig FROST_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), FROST_WISTERIA_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(3, 4),
                        UniformIntProvider.create(3, 4), // branch count
                        UniformFloatProvider.create(1.5F, 3F), // branch height
                        UniformFloatProvider.create(6F, 10F), // branch width
                        4, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        private static final TreeFeatureConfig FANCY_ROSE_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), ROSE_WISTERIA_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(3, 4),
                        UniformIntProvider.create(3, 4), // branch count
                        UniformFloatProvider.create(1.5F, 3F), // branch height
                        UniformFloatProvider.create(6F, 10F), // branch width
                        4, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        private static final TreeFeatureConfig FANCY_LAVENDER_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), LAVENDER_WISTERIA_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(3, 4),
                        UniformIntProvider.create(3, 4), // branch count
                        UniformFloatProvider.create(1.5F, 3F), // branch height
                        UniformFloatProvider.create(6F, 10F), // branch width
                        4, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        private static final TreeFeatureConfig FANCY_FROST_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), FROST_WISTERIA_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(3, 4),
                        UniformIntProvider.create(3, 4), // branch count
                        UniformFloatProvider.create(1.5F, 3F), // branch height
                        UniformFloatProvider.create(6F, 10F), // branch width
                        4, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );

        //Special trees
        private static final TreeFeatureConfig MOTHER_AUREL_CONFIG = generateTree(
                MOTHER_AUREL_LOG.getDefaultState(), MOTHER_AUREL_LEAVES.getDefaultState(), DIRT.getDefaultState(),
                new LargeOakTrunkPlacer(8, 1, 1),
                new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(2), 3),
                new TwoLayersFeatureSize(3, 0, 3, OptionalInt.of(2)),
                true, false
        );


    }

    /**
     * This is a separate inner class to prevent things loading in the wrong order.
     */
    public static class Assortments {
        private static RegistryEntry<PlacedFeature> placed(RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> feature, Block sapling) {
            return PlacedFeatures.createEntry(feature, PlacedFeatures.wouldSurvive(sapling));
        }

        private static RandomFeatureEntry entry(RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> feature, Block sapling, float chance) {
            return new RandomFeatureEntry(placed(feature, sapling), chance);
        }

        /*
        Tree Assortments
         */
        private static final RandomFeatureConfig RAINBOW_FOREST_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(LAVENDER_WISTERIA_TREE, LAVENDER_WISTERIA_SAPLING, 0.33F),
                        entry(ROSE_WISTERIA_TREE, ROSE_WISTERIA_SAPLING, 0.075F),
                        entry(FANCY_LAVENDER_WISTERIA_TREE, LAVENDER_WISTERIA_SAPLING, 0.025F),
                        entry(FANCY_ROSE_WISTERIA_TREE, ROSE_WISTERIA_SAPLING, 0.075F),
                        entry(FROST_WISTERIA_TREE, FROST_WISTERIA_SAPLING, 0.0001F),
                        entry(AUREL_TREE, AUREL_SAPLING, 0.2F),
                        entry(WILD_ORANGE_TREE, ORANGE_SAPLING, 0.0125F)
                ),
                placed(ROSE_WISTERIA_TREE, ROSE_WISTERIA_SAPLING)
        );

        private static final RandomFeatureConfig SPARSE_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(FANCY_AUREL_TREE, AUREL_SAPLING, 0.1F),
                        entry(WILD_ORANGE_TREE, ORANGE_SAPLING, 0.02F)
                ),
                placed(AUREL_TREE, AUREL_SAPLING)
        );

        private static final RandomFeatureConfig SCATTERED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(FANCY_AUREL_TREE, AUREL_SAPLING, 0.05F),
                        entry(FANCY_ROSE_WISTERIA_TREE, ROSE_WISTERIA_SAPLING, 0.0002F)
                ),
                placed(AUREL_TREE, AUREL_SAPLING)
        );

        private static final RandomFeatureConfig THICKET_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(AUREL_SHRUB, AUREL_SAPLING, 0.1F)
                ),
                placed(THICKET_AUREL_TREE, AUREL_SAPLING)
        );

        private static final RandomFeatureConfig RARE_MOTHER_AUREL_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(MOTHER_AUREL_TREE, MOTHER_AUREL_SAPLING, 1.0F)
                ),
                placed(MOTHER_AUREL_TREE, MOTHER_AUREL_SAPLING)
        );

        private static final RandomFeatureConfig DENSE_SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(DWARF_MOTTLED_AUREL, AUREL_SAPLING, 0.1F),
                        entry(AUREL_TREE, AUREL_SAPLING, 0.05F)
                ),
                placed(MOTTLED_AUREL, AUREL_SAPLING)
        );

        private static final RandomFeatureConfig SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(DWARF_MOTTLED_AUREL, AUREL_SAPLING, 0.15F)
                ),
                placed(MOTTLED_AUREL, AUREL_SAPLING)
        );

        private static final RandomFeatureConfig PLATEAU_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(WILD_ORANGE_TREE, ORANGE_SAPLING, 0.05F),
                        entry(DWARF_MOTTLED_AUREL, AUREL_SAPLING, 0.225F)
                ),
                placed(AUREL_SHRUB, AUREL_SAPLING)
        );

        private static final RandomFeatureConfig MIXED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        entry(DWARF_MOTTLED_AUREL, AUREL_SAPLING, 0.4F)
                ),
                placed(AUREL_TREE, AUREL_SAPLING)
        );
    }

}
