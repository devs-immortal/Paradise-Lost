package net.id.aether.world.feature.placed_features;

import com.google.common.collect.ImmutableList;
import net.id.aether.blocks.natural.tree.FruitingLeavesBlock;
import net.id.aether.world.feature.decorators.ChancePlacementModifier;
import net.id.aether.world.feature.configured_features.AetherConfiguredFeatures;
import net.id.aether.world.feature.configured_features.AetherTreeConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import static net.id.aether.blocks.AetherBlocks.*;

public class AetherTreePlacedFeatures extends AetherPlacedFeatures{
    // Reorganize according to biome, eventually
    /*
    Highlands
     */
    // Default
    public static final PlacedFeature SKYROOT_TREE = register("skyroot_tree", AetherTreeConfiguredFeatures.SKYROOT_TREE.withWouldSurviveFilter(SKYROOT_SAPLING));
    public static final PlacedFeature GOLDEN_OAK_TREE = register("golden_oak_tree", AetherTreeConfiguredFeatures.GOLDEN_OAK_TREE.withWouldSurviveFilter(GOLDEN_OAK_SAPLING));
    public static final PlacedFeature CRYSTAL_TREE = register("crystal_tree", AetherTreeConfiguredFeatures.CRYSTAL_TREE.withWouldSurviveFilter(CRYSTAL_SAPLING));
    public static final PlacedFeature ORANGE_TREE = register("orange_tree", AetherTreeConfiguredFeatures.ORANGE_TREE.withWouldSurviveFilter(ORANGE_SAPLING));
    // Wisteria
    public static final PlacedFeature ROSE_WISTERIA_TREE = register("rose_wisteria_tree", AetherTreeConfiguredFeatures.ROSE_WISTERIA_TREE.withWouldSurviveFilter(ROSE_WISTERIA_SAPLING));
    public static final PlacedFeature LAVENDER_WISTERIA_TREE = register("lavender_wisteria_tree", AetherTreeConfiguredFeatures.LAVENDER_WISTERIA_TREE.withWouldSurviveFilter(LAVENDER_WISTERIA_SAPLING));
    public static final PlacedFeature FROST_WISTERIA_TREE = register("frost_wisteria_tree", AetherTreeConfiguredFeatures.FROST_WISTERIA_TREE.withWouldSurviveFilter(FROST_WISTERIA_SAPLING));
    public static final PlacedFeature BOREAL_WISTERIA_TREE = register("boreal_wisteria_tree", AetherTreeConfiguredFeatures.BOREAL_WISTERIA_TREE.withWouldSurviveFilter(BOREAL_WISTERIA_SAPLING));
    // Fancy
    public static final PlacedFeature FANCY_ROSE_WISTERIA_TREE = register("fancy_rose_wisteria_tree", AetherTreeConfiguredFeatures.FANCY_ROSE_WISTERIA_TREE.withWouldSurviveFilter(ROSE_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_LAVENDER_WISTERIA_TREE = register("fancy_lavender_wisteria_tree", AetherTreeConfiguredFeatures.FANCY_LAVENDER_WISTERIA_TREE.withWouldSurviveFilter(LAVENDER_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_FROST_WISTERIA_TREE = register("fancy_frost_wisteria_tree", AetherTreeConfiguredFeatures.FANCY_FROST_WISTERIA_TREE.withWouldSurviveFilter(FROST_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_BOREAL_WISTERIA_TREE = register("fancy_boreal_wisteria_tree", AetherTreeConfiguredFeatures.FANCY_BOREAL_WISTERIA_TREE.withWouldSurviveFilter(BOREAL_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_SKYROOT_TREE = register("fancy_skyroot_tree", AetherTreeConfiguredFeatures.FANCY_SKYROOT_TREE.withWouldSurviveFilter(SKYROOT_SAPLING));

    /*
        // These are here because if these are not here, it doesn't work.
        // Why doesn't it work though? There has to be a better way to handle this.
     */
    // I'm changing like everything up now so out of fear of breaking something, these will remain here for now.
    // Todo move these way to AetherTreeConfiguredFeatures
    public static final ConfiguredFeature<?, ?> CF_SCATTERED_TREES = AetherConfiguredFeatures.register("scattered_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.SCATTERED_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_SHIELD_TREES = AetherConfiguredFeatures.register("shield_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.SHIELD_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_DENSE_SHIELD_TREES = AetherConfiguredFeatures.register("dense_shield_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.DENSE_SHIELD_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_PLATEAU_TREES = AetherConfiguredFeatures.register("plateau_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.PLATEAU_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_MIXED_TREES = AetherConfiguredFeatures.register("mixed_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.MIXED_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_SPARSE_TREES = AetherConfiguredFeatures.register("sparse_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.SPARSE_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_THICKET_TREES = AetherConfiguredFeatures.register("thicket_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.THICKET_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_RAINBOW_FOREST_TREES = AetherConfiguredFeatures.register("wisteria_woods_trees", Feature.RANDOM_SELECTOR.configure(AetherTreePlacedFeatures.Configs.RAINBOW_FOREST_CONFIG));
    // Tree Assortments
    public static final PlacedFeature SCATTERED_TREES = register("scattered_trees", CF_SCATTERED_TREES.withPlacement(CountMultilayerPlacementModifier.of(10), CountPlacementModifier.of(UniformIntProvider.create(0, 7))))/*.spreadHorizontally()*/;
    public static final PlacedFeature SHIELD_TREES = register("shield_trees", CF_SHIELD_TREES.withPlacement(CountMultilayerPlacementModifier.of(6), CountPlacementModifier.of(UniformIntProvider.create(0, 5))))/*.spreadHorizontally()*/;
    public static final PlacedFeature DENSE_SHIELD_TREES = register("dense_shield_trees", CF_DENSE_SHIELD_TREES.withPlacement(CountMultilayerPlacementModifier.of(16), new ChancePlacementModifier(ConstantIntProvider.create(10))));
    public static final PlacedFeature PLATEAU_TREES = register("plateau_trees", CF_PLATEAU_TREES.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))))/*.spreadHorizontally()*/;
    public static final PlacedFeature MIXED_TREES = register("mixed_trees", CF_MIXED_TREES.withPlacement(CountMultilayerPlacementModifier.of(1), new ChancePlacementModifier(ConstantIntProvider.create(10))));
    public static final PlacedFeature SPARSE_TREES = register("sparse_trees", CF_SPARSE_TREES.withPlacement(CountMultilayerPlacementModifier.of(14), new ChancePlacementModifier(ConstantIntProvider.create(50)), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    public static final PlacedFeature THICKET_TREES = register("thicket_trees", CF_THICKET_TREES.withPlacement(CountMultilayerPlacementModifier.of(20), CountPlacementModifier.of(UniformIntProvider.create(0, 5))))/*.spreadHorizontally()*/;
    public static final PlacedFeature RAINBOW_FOREST_TREES = register("wisteria_woods_trees", CF_RAINBOW_FOREST_TREES.withPlacement(CountMultilayerPlacementModifier.of(17), CountPlacementModifier.of(UniformIntProvider.create(0, 4))))/*.spreadHorizontally()*/;
    // Fallen leaves
    public static final PlacedFeature FALLEN_LEAVES = register("fallen_leaves", AetherTreeConfiguredFeatures.FALLEN_LEAVES.withPlacement(SPREAD_32_ABOVE, PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, CountPlacementModifier.of(3), new ChancePlacementModifier(ConstantIntProvider.create(5)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Configs.AETHER_GROUD))));
    public static final PlacedFeature ALT_FALLEN_LEAVES = register("alt_fallen_leaves", AetherTreeConfiguredFeatures.ALT_FALLEN_LEAVES.withPlacement(SPREAD_32_ABOVE, PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, CountPlacementModifier.of(3), new ChancePlacementModifier(ConstantIntProvider.create(5)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Configs.AETHER_GROUD))));

    public static final PlacedFeature FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", AetherTreeConfiguredFeatures.FALLEN_ROSE_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);
    public static final PlacedFeature FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", AetherTreeConfiguredFeatures.FALLEN_LAVENDER_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);
    // Logs & Stumps
    public static final PlacedFeature THICKET_FALLEN_LOG = register("thicket_fallen_log", AetherTreeConfiguredFeatures.THICKET_FALLEN_LOG.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2)))/*.spreadHorizontally()*/);

    public static final PlacedFeature MOTTLED_FALLEN_LOG = register("mottled_fallen_log", AetherTreeConfiguredFeatures.MOTTLED_FALLEN_LOG.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(1)));
    public static final PlacedFeature MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", AetherTreeConfiguredFeatures.MOTTLED_HOLLOW_FALLEN_LOG.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature SHIELD_STUMPS = register("shield_stumps", AetherTreeConfiguredFeatures.SHIELD_STUMPS.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))/*.spreadHorizontally()*/));
    public static final PlacedFeature SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", AetherTreeConfiguredFeatures.SHIELD_HOLLOW_STUMPS.withPlacement(CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", AetherTreeConfiguredFeatures.SHIELD_FALLEN_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(2), new ChancePlacementModifier(ConstantIntProvider.create(4)), CountPlacementModifier.of(UniformIntProvider.create(0, 3)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Configs.AETHER_GROUD)))/*.spreadHorizontally()*/);
    // Crystal Tree Island
    public static final PlacedFeature CRYSTAL_TREE_ISLAND = register("crystal_tree_island", AetherTreeConfiguredFeatures.CRYSTAL_TREE_ISLAND.withPlacement());

    /*
        {
      "count": {
        "distribution": [
          {
            "data": 1,
            "weight": 3
          },
          {
            "data": 0,
            "weight": 1
          }
        ],
        "type": "minecraft:weighted_list"
      },
      "type": "minecraft:count"
    },
     */
    public static final PlacedFeature CRYSTAL_TREE_ISLAND_DECORATED = register("crystal_tree_island_decorated", AetherTreeConfiguredFeatures.CRYSTAL_TREE_ISLAND.withPlacement(RarityFilterPlacementModifier.of(80), /*TODO See above comment, */ SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(55), YOffset.fixed(70)), BiomePlacementModifier.of()));

    public static class Configs extends AetherPlacedFeatures.Configs{

        public static final BlockState ORANGE_LEAVES_BASIC = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true);
        public static final BlockState ORANGE_LEAVES_FLOWERING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 1);
        public static final BlockState ORANGE_LEAVES_FRUITING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 2);

        public static final PlacedFeature RAINBOW_LEAVES_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 9).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)))).withPlacement();
        public static final RandomPatchFeatureConfig RAINBOW_LEAVES_CONFIG = (new RandomPatchFeatureConfig(256, 10, 7, () -> RAINBOW_LEAVES_SINGLE_BLOCK))/*.cannotProject()*/;

        public static final RandomFeatureConfig RAINBOW_FOREST_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(LAVENDER_WISTERIA_TREE, 0.33F),
                        new RandomFeatureEntry(ROSE_WISTERIA_TREE, 0.075F),
                        new RandomFeatureEntry(FANCY_LAVENDER_WISTERIA_TREE, 0.025F),
                        new RandomFeatureEntry(FANCY_ROSE_WISTERIA_TREE, 0.075F),
                        new RandomFeatureEntry(FROST_WISTERIA_TREE, 0.0001F),
                        new RandomFeatureEntry(SKYROOT_TREE, 0.2F),
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.ORANGE_TREE_WILD_CONFIG, ORANGE_SAPLING), 0.0125F)),
                ROSE_WISTERIA_TREE
        );

        public static final RandomFeatureConfig SPARSE_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(FANCY_SKYROOT_TREE, 0.1F),
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.ORANGE_TREE_WILD_CONFIG, ORANGE_SAPLING), 0.02F)
                ),
                SKYROOT_TREE
        );

        public static final RandomFeatureConfig SCATTERED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(FANCY_SKYROOT_TREE, 0.05F),
                        new RandomFeatureEntry(FANCY_ROSE_WISTERIA_TREE, 0.002F)),
                SKYROOT_TREE
        );

        public static final RandomFeatureConfig THICKET_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(ROSE_WISTERIA_TREE, 0.0001F),
                        new RandomFeatureEntry(LAVENDER_WISTERIA_TREE, 0.0001F),
                        new RandomFeatureEntry(GOLDEN_OAK_TREE, 0.0025F),
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.SKYROOT_SHRUB_CONFIG, SKYROOT_SAPLING), 0.15F), // convert to feature
                        new RandomFeatureEntry(SKYROOT_TREE, 0.75F)),
                FANCY_SKYROOT_TREE
        );

        public static final RandomFeatureConfig DENSE_SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.DWARF_MOTTLED_SKYROOT_CONFIG, SKYROOT_SAPLING), 0.1F),
                        new RandomFeatureEntry(SKYROOT_TREE, 0.05F)),
                placedTree(AetherTreeConfiguredFeatures.Configs.MOTTLED_SKYROOT_CONFIG, SKYROOT_SAPLING)
        );

        public static final RandomFeatureConfig SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.DWARF_MOTTLED_SKYROOT_CONFIG, SKYROOT_SAPLING), 0.15F)),
                placedTree(AetherTreeConfiguredFeatures.Configs.MOTTLED_SKYROOT_CONFIG, SKYROOT_SAPLING)
        );

        public static final RandomFeatureConfig PLATEAU_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.ORANGE_TREE_WILD_CONFIG, ORANGE_SAPLING), 0.05F),
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.DWARF_MOTTLED_SKYROOT_CONFIG, SKYROOT_SAPLING), 0.225F)),
                placedTree(AetherTreeConfiguredFeatures.Configs.SKYROOT_SHRUB_CONFIG, SKYROOT_SAPLING)
        );

        public static final RandomFeatureConfig MIXED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(AetherTreeConfiguredFeatures.Configs.DWARF_MOTTLED_SKYROOT_CONFIG, SKYROOT_SAPLING), 0.4F)),
                SKYROOT_TREE
        );

        private static PlacedFeature placedTree(TreeFeatureConfig cfg, Block sapling) {
            return Feature.TREE.configure(cfg).withWouldSurviveFilter(sapling);
        }

    }

    public static void init(){}
}
