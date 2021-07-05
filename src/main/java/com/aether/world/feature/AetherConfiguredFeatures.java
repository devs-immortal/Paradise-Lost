package com.aether.world.feature;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.AetherFruitingLeaves;
import com.aether.world.feature.tree.placers.WisteriaFoliagePlacer;
import com.aether.world.feature.tree.placers.WisteriaTrunkPlacer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.OptionalInt;

@SuppressWarnings("unchecked")
public class AetherConfiguredFeatures {

    public static Feature<SingleStateFeatureConfig> BOULDER;

    public static ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK_TREE, CRYSTAL_TREE, SKYROOT_TREE, ORANGE_TREE, WILD_ORANGE_TREE, ROSE_WISTERIA_TREE, LAVENDER_WISTERIA_TREE, FROST_WISTERIA_TREE, FANCY_ROSE_WISTERIA_TREE, FANCY_LAVENDER_WISTERIA_TREE, FANCY_FROST_WISTERIA_TREE, FANCY_SKYROOT_TREE, BOREAL_WISTERIA_TREE, FANCY_BOREAL_WISTERIA_TREE;
    public static ConfiguredFeature<?, ?> SCATTERED_TREES, SPARSE_TREES, THICKET_TREES, RAINBOW_FOREST_TREES;
    public static ConfiguredFeature<?, ?> HOLYSTONE_BOULDER, MOSSY_HOLYSTONE_BOULDER;
    public static ConfiguredFeature<?, ?> AETHER_GRASS, AETHER_TALL_GRASS, ALT_AETHER_GRASS, ALT_AETHER_TALL_GRASS, DENSE_TALL_GRASS, AETHER_FERN, DENSE_AETHER_FERN, AETHER_BUSH, FLUTEGRASS;
    public static ConfiguredFeature<?, ?> FALLEN_LEAVES, FALLEN_RAINBOW_LEAVES, ALT_FALLEN_LEAVES;

    public static void registerFeatures() {

        BOULDER = Registry.register(Registry.FEATURE, Aether.locate("boulder"), new AetherBoulderFeature(SingleStateFeatureConfig.CODEC));

        AETHER_BUSH = register("aether_bush", Feature.RANDOM_PATCH.configure(Configs.AETHER_BUSH_CONFIG).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
        SKYROOT_TREE = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG));
        GOLDEN_OAK_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        CRYSTAL_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("crystal_tree", Feature.TREE.configure(Configs.CRYSTAL_TREE_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        ORANGE_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("orange_tree", Feature.TREE.configure(Configs.ORANGE_TREE_SAPLING_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        ROSE_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("rose_wisteria_tree", Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        LAVENDER_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("lavender_wisteria_tree", Feature.TREE.configure(Configs.LAVENDER_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        FROST_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("frost_wisteria_tree", Feature.TREE.configure(Configs.FROST_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        BOREAL_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("boreal_wisteria_tree", Feature.TREE.configure(Configs.BOREAL_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        FANCY_ROSE_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_rose_wisteria_tree", Feature.TREE.configure(Configs.FANCY_ROSE_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        FANCY_LAVENDER_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_lavender_wisteria_tree", Feature.TREE.configure(Configs.FANCY_LAVENDER_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        FANCY_FROST_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_frost_wisteria_tree", Feature.TREE.configure(Configs.FANCY_FROST_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        FANCY_BOREAL_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_boreal_wisteria_tree", Feature.TREE.configure(Configs.FANCY_BOREAL_WISTERIA_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        FANCY_SKYROOT_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_skyroot_tree", Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP));
        SCATTERED_TREES = register("scattered_trees", Feature.RANDOM_SELECTOR.configure(Configs.SCATTERED_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(7, 0.1F, 2))));
        SPARSE_TREES = register("sparse_trees", Feature.RANDOM_SELECTOR.configure(Configs.SPARSE_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))));
        THICKET_TREES = register("thicket_trees", Feature.RANDOM_SELECTOR.configure(Configs.THICKET_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(20, 0.25F, 12)))).spreadHorizontally().repeatRandomly(3);
        RAINBOW_FOREST_TREES = register("wisteria_woods_trees", Feature.RANDOM_SELECTOR.configure(Configs.RAINBOW_FOREST_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(16, 0.25F, 16)))).spreadHorizontally().repeatRandomly(4);

        // Used in json
        HOLYSTONE_BOULDER = register("holystone_boulder", BOULDER.configure(new SingleStateFeatureConfig(AetherBlocks.COBBLED_HOLYSTONE.getDefaultState()))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).applyChance(5);
        MOSSY_HOLYSTONE_BOULDER = register("mossy_holystone_boulder", BOULDER.configure(new SingleStateFeatureConfig(AetherBlocks.MOSSY_HOLYSTONE.getDefaultState()))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(3))).spreadHorizontally().repeatRandomly(2);

        FALLEN_LEAVES = register("fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
        ALT_FALLEN_LEAVES = register("alt_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
        FALLEN_RAINBOW_LEAVES = register("rainbow_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.RAINBOW_LEAVES_CONFIG).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Aether.locate(id), configuredFeature);
    }

    public static class Configs {

        public static final BlockState ORANGE_LEAVES = AetherBlocks.ORANGE_LEAVES.getDefaultState().with(AetherFruitingLeaves.CAPPED, true);
        public static final BlockState ORANGE_LEAVES_FLOWERING = AetherBlocks.ORANGE_LEAVES.getDefaultState().with(AetherFruitingLeaves.CAPPED, true).with(AetherFruitingLeaves.GROWTH, 1);
        public static final BlockState ORANGE_LEAVES_FRUITING = AetherBlocks.ORANGE_LEAVES.getDefaultState().with(AetherFruitingLeaves.CAPPED, true).with(AetherFruitingLeaves.GROWTH, 2);

        //public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider(ConfiguredFeatures.method_35926().add(AetherBlocks.PURPLE_FLOWER.getDefaultState(), 2).add(AetherBlocks.WHITE_FLOWER.getDefaultState(), 1)), new SimpleBlockPlacer())).tries(64).build();
        public static final RandomPatchFeatureConfig AETHER_BUSH_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.AETHER_BUSH.getDefaultState()), SimpleBlockPlacer.INSTANCE)).spreadX(16).spreadY(7).spreadZ(16).tries(256).build();
        public static final RandomPatchFeatureConfig FALLEN_LEAVES_CONFIG = (new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(ConfiguredFeatures.method_35926().add(AetherBlocks.SKYROOT_LEAF_PILE.getDefaultState(), 8).add(AetherBlocks.SKYROOT_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)), new SimpleBlockPlacer())).whitelist(ImmutableSet.of(AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.HOLYSTONE, AetherBlocks.MOSSY_HOLYSTONE)).cannotProject().spreadX(10).spreadY(7).spreadZ(10).tries(96).build();
        public static final RandomPatchFeatureConfig RAINBOW_LEAVES_CONFIG = (new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(ConfiguredFeatures.method_35926().add(AetherBlocks.ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(AetherBlocks.LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 9).add(AetherBlocks.ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).add(AetherBlocks.LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)), new SimpleBlockPlacer())).whitelist(ImmutableSet.of(AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.HOLYSTONE, AetherBlocks.MOSSY_HOLYSTONE)).cannotProject().spreadX(10).spreadY(7).spreadZ(10).tries(256).build();
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new StraightTrunkPlacer(4, 2, 0), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig CRYSTAL_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.CRYSTAL_LOG.getDefaultState()), new StraightTrunkPlacer(5, 2, 2), new SimpleBlockStateProvider(AetherBlocks.CRYSTAL_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.CRYSTAL_SAPLING.getDefaultState()), new SpruceFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 1)), new TwoLayersFeatureSize(2, 0, 2))).ignoreVines().build();
        public static final TreeFeatureConfig ORANGE_TREE_SAPLING_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.ORANGE_LOG.getDefaultState()), new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)), new SimpleBlockStateProvider(AetherBlocks.ORANGE_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.ORANGE_SAPLING.getDefaultState()), new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63), new TwoLayersFeatureSize(1, 0, 1))).build();
        public static final TreeFeatureConfig ROSE_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(AetherBlocks.ROSE_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.ROSE_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig LAVENDER_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(AetherBlocks.LAVENDER_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.LAVENDER_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FROST_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(AetherBlocks.FROST_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.FROST_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig BOREAL_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(AetherBlocks.BOREAL_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.BOREAL_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 3), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_ROSE_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(9, 4, 2), new SimpleBlockStateProvider(AetherBlocks.ROSE_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.ROSE_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(2, 7), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_LAVENDER_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(9, 4, 2), new SimpleBlockStateProvider(AetherBlocks.LAVENDER_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.LAVENDER_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(2, 7), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_FROST_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(11, 6, 3), new SimpleBlockStateProvider(AetherBlocks.FROST_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.FROST_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 9), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_BOREAL_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(11, 6, 3), new SimpleBlockStateProvider(AetherBlocks.BOREAL_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.BOREAL_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 9), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LOG.getDefaultState()), new LargeOakTrunkPlacer(4, 8, 0), new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(3), 3), new TwoLayersFeatureSize(3, 0, 3, OptionalInt.of(2)))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new LargeOakTrunkPlacer(4, 11, 0), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_SAPLING.getDefaultState()), new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build();

        public static final RandomFeatureConfig SCATTERED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(FANCY_SKYROOT_CONFIG).withChance(0.05F), Feature.TREE.configure(ROSE_WISTERIA_CONFIG).withChance(0.002F)),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig THICKET_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(ROSE_WISTERIA_CONFIG).withChance(0.0001F), Feature.TREE.configure(LAVENDER_WISTERIA_CONFIG).withChance(0.0001F), Feature.TREE.configure(GOLDEN_OAK_CONFIG).withChance(0.0025F), Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.1F)),
                Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig RAINBOW_FOREST_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(LAVENDER_WISTERIA_CONFIG).withChance(0.33F), Feature.TREE.configure(ROSE_WISTERIA_CONFIG).withChance(0.075F), Feature.TREE.configure(FROST_WISTERIA_CONFIG).withChance(0.0001F), Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.1F)),
                Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG)
        );

        public static final RandomFeatureConfig SPARSE_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.1F)
                ),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );
    }
}