package net.id.aether.world.feature.features.configured;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.natural.tree.FruitingLeavesBlock;
import net.id.aether.world.feature.config.LongFeatureConfig;
import net.id.aether.world.feature.config.ProjectedOrganicCoverConfig;
import net.id.aether.world.feature.feature.FallenPillarFeature;
import net.id.aether.world.feature.feature.PillarFeature;
import net.id.aether.world.feature.features.AetherFeatures;
import net.id.aether.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.aether.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.aether.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
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

import static net.id.aether.blocks.AetherBlocks.*;

public class AetherTreeConfiguredFeatures extends AetherConfiguredFeatures{
    // Reorganize per biome
    /*
    Highlands
     */
    // Default
    public static final ConfiguredFeature<TreeFeatureConfig, ?> SKYROOT_TREE = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG)); // TODO REPLACED WITH JSON //
    public static final ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK_TREE = register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CRYSTAL_TREE = register("crystal_tree", Feature.TREE.configure(Configs.CRYSTAL_TREE_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> ORANGE_TREE = register("orange_tree", Feature.TREE.configure(Configs.ORANGE_TREE_SAPLING_CONFIG));
    // Wisteria
    public static final ConfiguredFeature<TreeFeatureConfig, ?> ROSE_WISTERIA_TREE = register("rose_wisteria_tree", Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LAVENDER_WISTERIA_TREE = register("lavender_wisteria_tree", Feature.TREE.configure(Configs.LAVENDER_WISTERIA_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FROST_WISTERIA_TREE = register("frost_wisteria_tree", Feature.TREE.configure(Configs.FROST_WISTERIA_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> BOREAL_WISTERIA_TREE = register("boreal_wisteria_tree", Feature.TREE.configure(Configs.BOREAL_WISTERIA_CONFIG));
    // Fancy
    public static final ConfiguredFeature<?, ?> FANCY_ROSE_WISTERIA_TREE = register("fancy_rose_wisteria_tree", Feature.TREE.configure(Configs.FANCY_ROSE_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_LAVENDER_WISTERIA_TREE = register("fancy_lavender_wisteria_tree", Feature.TREE.configure(Configs.FANCY_LAVENDER_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_FROST_WISTERIA_TREE = register("fancy_frost_wisteria_tree", Feature.TREE.configure(Configs.FANCY_FROST_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_BOREAL_WISTERIA_TREE = register("fancy_boreal_wisteria_tree", Feature.TREE.configure(Configs.FANCY_BOREAL_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_SKYROOT_TREE = register("fancy_skyroot_tree", Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG));
    // Crystal
    public static final ConfiguredFeature<?, ?> CRYSTAL_TREE_ISLAND = register("crystal_tree_island", AetherFeatures.CRYSTAL_TREE_ISLAND.configure(AetherConfiguredFeatures.Configs.DEFAULT_CONFIG));
    // Fallen leaves
    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = register("fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG));
    public static final ConfiguredFeature<?, ?> ALT_FALLEN_LEAVES = register("alt_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG));

    public static final ConfiguredFeature<?, ?> FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2)));
    public static final ConfiguredFeature<?, ?> FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2)));

    public static final ConfiguredFeature<?, ?> SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG));
    // Logs
    public static final ConfiguredFeature<?, ?> THICKET_FALLEN_LOG = register("thicket_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 6), BlockStateProvider.of(SKYROOT_LOG), BlockStateProvider.of(LIVERWORT_CARPET), BlockStateProvider.of(LIVERWORT_CARPET), 0.5F, 0.35F, Configs.GENERIC_FLOOR_WHITELIST)));

    public static final ConfiguredFeature<?, ?> MOTTLED_FALLEN_LOG = register("mottled_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 5), BlockStateProvider.of(MOTTLED_SKYROOT_LOG), BlockStateProvider.of(AetherBlocks.AETHER_GRASS), BlockStateProvider.of(ROOTCAP), 0.3F, 0.15F, Configs.GENERIC_FLOOR_WHITELIST)));
    public static final ConfiguredFeature<?, ?> MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 5), BlockStateProvider.of(MOTTLED_SKYROOT_FALLEN_LOG), BlockStateProvider.of(AETHER_GRASS_FLOWERING), BlockStateProvider.of(ROOTCAP), 0.4F, 0.25F, Configs.GENERIC_FLOOR_WHITELIST)));

    public static final ConfiguredFeature<?, ?> SHIELD_STUMPS = register("shield_stumps", Configs.PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(1, 2), BlockStateProvider.of(MOTTLED_SKYROOT_LOG), BlockStateProvider.of(MOTTLED_SKYROOT_FALLEN_LOG), BlockStateProvider.of(ROOTCAP), 0.1F, 0.225F, Configs.GENERIC_FLOOR_WHITELIST)));
    public static final ConfiguredFeature<?, ?> SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", Configs.PILLAR_FEATURE.configure(new LongFeatureConfig(ConstantIntProvider.create(1), BlockStateProvider.of(MOTTLED_SKYROOT_FALLEN_LOG), BlockStateProvider.of(MOTTLED_SKYROOT_FALLEN_LOG), BlockStateProvider.of(ROOTCAP), 0.015F, 0.3F, Configs.GENERIC_FLOOR_WHITELIST)));

    public static class Configs extends AetherConfiguredFeatures.Configs{

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

        public static final BlockState ORANGE_LEAVES_BASIC = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true);
        public static final BlockState ORANGE_LEAVES_FLOWERING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 1);
        public static final BlockState ORANGE_LEAVES_FRUITING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 2);

        // Find a way to move this to Placed Features
        public static final PlacedFeature FALLEN_LEAVES_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(SKYROOT_LEAF_PILE.getDefaultState(), 8).add(SKYROOT_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)))).withInAirFilter();
        public static final RandomPatchFeatureConfig FALLEN_LEAVES_CONFIG = (new RandomPatchFeatureConfig(96, 10, 7, () -> FALLEN_LEAVES_SINGLE_BLOCK))/*.cannotProject()*/;

        public static final Feature<LongFeatureConfig> PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
        public static final Feature<LongFeatureConfig> FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));

        //Skyroots
        public static final TreeFeatureConfig SKYROOT_CONFIG = generateTree(
                SKYROOT_LOG.getDefaultState(), SKYROOT_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new StraightTrunkPlacer(4, 2, 0),
                new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );
        public static final TreeFeatureConfig FANCY_SKYROOT_CONFIG = generateTree(
                SKYROOT_LOG.getDefaultState(), SKYROOT_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new LargeOakTrunkPlacer(4, 11, 0),
                new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)),
                true, false
        );
        public static final TreeFeatureConfig SKYROOT_SHRUB_CONFIG = generateTree(
                SKYROOT_LOG.getDefaultState(), SKYROOT_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new StraightTrunkPlacer(1, 1, 0),
                new BlobFoliagePlacer(UniformIntProvider.create(2, 4), ConstantIntProvider.create(0), 2),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );
        public static final TreeFeatureConfig MOTTLED_SKYROOT_CONFIG = generateTree(
                MOTTLED_SKYROOT_LOG.getDefaultState(), SKYROOT_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new OvergrownTrunkPlacer(5, 10, 0, BlockStateProvider.of(ROOTCAP), 1 / 14F),
                new BlobFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );
        public static final TreeFeatureConfig DWARF_MOTTLED_SKYROOT_CONFIG = generateTree(
                MOTTLED_SKYROOT_LOG.getDefaultState(), SKYROOT_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new BendingTrunkPlacer(5, 3, 2, 4, UniformIntProvider.create(1, 3)),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 68),
                new TwoLayersFeatureSize(1, 0, 1),
                true, false
        );

        //Fruit trees
        public static final TreeFeatureConfig CRYSTAL_TREE_CONFIG = generateTree(
                CRYSTAL_LOG.getDefaultState(), CRYSTAL_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new StraightTrunkPlacer(5, 2, 2),
                new SpruceFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 1)),
                new TwoLayersFeatureSize(2, 0, 2),
                true, false
        );
        public static final TreeFeatureConfig ORANGE_TREE_SAPLING_CONFIG = generateTree(
                ORANGE_LOG.getDefaultState(), ORANGE_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63),
                new TwoLayersFeatureSize(1, 0, 1),
                false, false
        );
        public static final TreeFeatureConfig ORANGE_TREE_WILD_CONFIG = generateTree(
                BlockStateProvider.of(ORANGE_LOG),
                new WeightedBlockStateProvider(
                        DataPool.<BlockState>builder()
                                .add(ORANGE_LEAVES_BASIC, 2)
                                .add(ORANGE_LEAVES_FLOWERING, 2)
                                .add(ORANGE_LEAVES_FRUITING, 1)
                ),
                BlockStateProvider.of(AETHER_DIRT),
                new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63),
                new TwoLayersFeatureSize(1, 0, 1),
                false, false
        );

        //Wisterias
        public static final TreeFeatureConfig ROSE_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), ROSE_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(5, 9),
                        UniformIntProvider.create(1, 3), UniformIntProvider.create(2, 5), UniformIntProvider.create(1, 3),
                        ConstantFloatProvider.create(0.334F),
                        3, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 4), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig LAVENDER_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), LAVENDER_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(5, 9),
                        UniformIntProvider.create(1, 3), UniformIntProvider.create(2, 5), UniformIntProvider.create(1, 3),
                        ConstantFloatProvider.create(0.334F),
                        3, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 4), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig FROST_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), FROST_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(5, 9),
                        UniformIntProvider.create(1, 3), UniformIntProvider.create(2, 5), UniformIntProvider.create(1, 3),
                        ConstantFloatProvider.create(0.334F),
                        3, 3, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 4), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig BOREAL_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), BOREAL_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(7, 11),
                        UniformIntProvider.create(2, 4), UniformIntProvider.create(2, 5), UniformIntProvider.create(2, 4),
                        ConstantFloatProvider.create(0.334F),
                        4, 5, 2
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig FANCY_ROSE_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), ROSE_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(10, 17),
                        UniformIntProvider.create(3, 7), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 7),
                        ConstantFloatProvider.create(0.334F),
                        6, 5, 3
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig FANCY_LAVENDER_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), LAVENDER_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(10, 17),
                        UniformIntProvider.create(3, 7), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 7),
                        ConstantFloatProvider.create(0.334F),
                        6, 5, 3
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig FANCY_FROST_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), FROST_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(10, 17),
                        UniformIntProvider.create(3, 7), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 7),
                        ConstantFloatProvider.create(0.334F),
                        6, 5, 3
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );
        public static final TreeFeatureConfig FANCY_BOREAL_WISTERIA_CONFIG = generateTree(
                WISTERIA_LOG.getDefaultState(), BOREAL_WISTERIA_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new WisteriaTrunkPlacer(
                        UniformIntProvider.create(13, 21),
                        UniformIntProvider.create(3, 9), UniformIntProvider.create(4, 10), UniformIntProvider.create(3, 9),
                        ConstantFloatProvider.create(0.334F),
                        7, 6, 3
                ),
                new WisteriaFoliagePlacer(UniformIntProvider.create(3, 6), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(3, 0, 3),
                true, false
        );

        //Special trees
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = generateTree(
                GOLDEN_OAK_LOG.getDefaultState(), GOLDEN_OAK_LEAVES.getDefaultState(), AETHER_DIRT.getDefaultState(),
                new LargeOakTrunkPlacer(4, 8, 0),
                new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(3), 3),
                new TwoLayersFeatureSize(3, 0, 3, OptionalInt.of(2)),
                true, false
        );

    }

    public static void init(){}
}
