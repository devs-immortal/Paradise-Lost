package net.id.aether.world.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.natural.tree.FruitingLeavesBlock;
import net.id.aether.world.feature.config.GroundcoverFeatureConfig;
import net.id.aether.world.feature.config.PillarFeatureConfig;
import net.id.aether.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.aether.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.aether.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.id.incubus_core.worldgen.BiFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
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

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import static net.id.aether.blocks.AetherBlocks.*;

@SuppressWarnings("unchecked")
public class AetherConfiguredFeatures extends ConfiguredFeatures {

    public static Feature<SingleStateFeatureConfig> BOULDER;

    public static ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK_TREE, CRYSTAL_TREE, SKYROOT_TREE, ORANGE_TREE, ROSE_WISTERIA_TREE, LAVENDER_WISTERIA_TREE, FROST_WISTERIA_TREE, FANCY_ROSE_WISTERIA_TREE, FANCY_LAVENDER_WISTERIA_TREE, FANCY_FROST_WISTERIA_TREE, FANCY_SKYROOT_TREE, BOREAL_WISTERIA_TREE, FANCY_BOREAL_WISTERIA_TREE;
    public static ConfiguredFeature<?, ?> SCATTERED_TREES, SHIELD_TREES, DENSE_SHIELD_TREES, SPARSE_TREES, THICKET_TREES, RAINBOW_FOREST_TREES;
    public static ConfiguredFeature<?, ?> HOLYSTONE_BOULDER, MOSSY_HOLYSTONE_BOULDER, GOLDEN_MOSSY_HOLYSTONE_BOULDER;
    public static ConfiguredFeature<?, ?> SHIELD_FOLIAGE, SHIELD_ROCKS, AETHER_BUSH;
    public static ConfiguredFeature<?, ?> FALLEN_LEAVES, FALLEN_RAINBOW_LEAVES, ALT_FALLEN_LEAVES, SHIELD_FALLEN_LEAVES;
    public static ConfiguredFeature<?, ?> SHIELD_PONDS, SHIELD_PODZOL, SHIELD_STONE, SHIELD_STUMPS, SHIELD_HOLLOW_STUMPS;

    public static void init() {

        BOULDER = Registry.register(Registry.FEATURE, Aether.locate("boulder"), new AetherBoulderFeature(SingleStateFeatureConfig.CODEC));

        AETHER_BUSH = register("aether_bush", Feature.RANDOM_PATCH.configure(Configs.AETHER_BUSH_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(5))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeatRandomly(4));
        SKYROOT_TREE = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG));
        GOLDEN_OAK_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG).decorate(Decorators.HEIGHTMAP));
        CRYSTAL_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("crystal_tree", Feature.TREE.configure(Configs.CRYSTAL_TREE_CONFIG).decorate(Decorators.HEIGHTMAP));
        ORANGE_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("orange_tree", Feature.TREE.configure(Configs.ORANGE_TREE_SAPLING_CONFIG).decorate(Decorators.HEIGHTMAP));
        ROSE_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("rose_wisteria_tree", Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        LAVENDER_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("lavender_wisteria_tree", Feature.TREE.configure(Configs.LAVENDER_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        FROST_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("frost_wisteria_tree", Feature.TREE.configure(Configs.FROST_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        BOREAL_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("boreal_wisteria_tree", Feature.TREE.configure(Configs.BOREAL_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        FANCY_ROSE_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_rose_wisteria_tree", Feature.TREE.configure(Configs.FANCY_ROSE_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        FANCY_LAVENDER_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_lavender_wisteria_tree", Feature.TREE.configure(Configs.FANCY_LAVENDER_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        FANCY_FROST_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_frost_wisteria_tree", Feature.TREE.configure(Configs.FANCY_FROST_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        FANCY_BOREAL_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_boreal_wisteria_tree", Feature.TREE.configure(Configs.FANCY_BOREAL_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
        FANCY_SKYROOT_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_skyroot_tree", Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG).decorate(Decorators.HEIGHTMAP));
        SCATTERED_TREES = register("scattered_trees", Feature.RANDOM_SELECTOR.configure(Configs.SCATTERED_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(10)))).spreadHorizontally().repeatRandomly(8);
        SHIELD_TREES = register("shield_trees", Feature.RANDOM_SELECTOR.configure(Configs.SHIELD_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(6)))).spreadHorizontally().repeatRandomly(5);
        DENSE_SHIELD_TREES = register("dense_shield_trees", Feature.RANDOM_SELECTOR.configure(Configs.DENSE_SHIELD_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(16))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(10)))).repeatRandomly(3);
        SPARSE_TREES = register("sparse_trees", Feature.RANDOM_SELECTOR.configure(Configs.SPARSE_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(14))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(50)))).repeatRandomly(2);
        THICKET_TREES = register("thicket_trees", Feature.RANDOM_SELECTOR.configure(Configs.THICKET_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(16)))).spreadHorizontally().repeatRandomly(3);
        RAINBOW_FOREST_TREES = register("wisteria_woods_trees", Feature.RANDOM_SELECTOR.configure(Configs.RAINBOW_FOREST_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(17)))).spreadHorizontally().repeatRandomly(4);

        // Used in json
        HOLYSTONE_BOULDER = register("holystone_boulder", BOULDER.configure(new SingleStateFeatureConfig(COBBLED_HOLYSTONE.getDefaultState()))).decorate(Decorators.SQUARE_HEIGHTMAP).repeatRandomly(6);
        MOSSY_HOLYSTONE_BOULDER = register("mossy_holystone_boulder", BOULDER.configure(new SingleStateFeatureConfig(MOSSY_HOLYSTONE.getDefaultState()))).decorate(Decorators.SQUARE_HEIGHTMAP).repeatRandomly(12);
        GOLDEN_MOSSY_HOLYSTONE_BOULDER = register("golden_mossy_holystone_boulder", BOULDER.configure(new SingleStateFeatureConfig(GOLDEN_MOSSY_HOLYSTONE.getDefaultState()))).decorate(Decorators.SQUARE_HEIGHTMAP).repeatRandomly(1);

        FALLEN_LEAVES = register("fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
        ALT_FALLEN_LEAVES = register("alt_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
        FALLEN_RAINBOW_LEAVES = register("rainbow_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.RAINBOW_LEAVES_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));

        SHIELD_FOLIAGE = register("shield_foliage", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).build()), new SimpleBlockPlacer()).build()).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(5))).repeatRandomly(2));
        SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(4))).spreadHorizontally().repeatRandomly(3)));
        SHIELD_ROCKS = register("shield_rocks", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), 10).add(COBBLED_HOLYSTONE.getDefaultState(), 4).build()), new SimpleBlockPlacer()).whitelist(Set.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE)).spreadX(9).spreadZ(9).tries(96).cannotProject().build()).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(3)).repeatRandomly(3).spreadHorizontally()));

        SHIELD_STUMPS = register("shield_stumps", Configs.PILLAR_FEATURE.configure(new PillarFeatureConfig(UniformIntProvider.create(1, 2), new SimpleBlockStateProvider(MOTTLED_SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 0.1F, 0.225F, List.of(AETHER_GRASS.getDefaultState(), HOLYSTONE.getDefaultState()))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)).repeatRandomly(3).spreadHorizontally()));
        SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", Configs.PILLAR_FEATURE.configure(new PillarFeatureConfig(ConstantIntProvider.create(1), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 0.015F, 0.3F, List.of(AETHER_GRASS.getDefaultState(), HOLYSTONE.getDefaultState()))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)).repeatRandomly(1)));

        SHIELD_PONDS = register("shield_pond", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.WATER.getDefaultState(), COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(Properties.WATERLOGGED, true), UniformIntProvider.create(2, 7), UniformIntProvider.create(1, 2))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(30))));

        SHIELD_STONE = register("shield_stone", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HOLYSTONE.getDefaultState(), 7).add(COBBLED_HOLYSTONE.getDefaultState(), 5).add(MOSSY_HOLYSTONE.getDefaultState(), 2).build()), UniformIntProvider.create(1, 4), UniformIntProvider.create(0, 0))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)))).repeatRandomly(2).spreadHorizontally();
        SHIELD_PODZOL = register("shield_podzol", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new SimpleBlockStateProvider(AETHER_PODZOL.getDefaultState()), UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 0))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(15))).repeatRandomly(2).spreadHorizontally();
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Aether.locate(id), configuredFeature);
    }

    public static class Configs {

        public static final BlockState ORANGE_LEAVES_BASIC = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true);
        public static final BlockState ORANGE_LEAVES_FLOWERING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 1);
        public static final BlockState ORANGE_LEAVES_FRUITING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 2);

        //public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(PURPLE_FLOWER.getDefaultState(), 2).add(WHITE_FLOWER.getDefaultState(), 1)), new SimpleBlockPlacer())).tries(64).build();
        public static final RandomPatchFeatureConfig AETHER_BUSH_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.AETHER_BUSH.getDefaultState()), SimpleBlockPlacer.INSTANCE)).spreadX(16).spreadY(7).spreadZ(16).tries(128).build();
        public static final RandomPatchFeatureConfig FALLEN_LEAVES_CONFIG = (new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(SKYROOT_LEAF_PILE.getDefaultState(), 8).add(SKYROOT_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)), new SimpleBlockPlacer())).whitelist(ImmutableSet.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE)).cannotProject().spreadX(10).spreadY(7).spreadZ(10).tries(96).build();
        public static final RandomPatchFeatureConfig RAINBOW_LEAVES_CONFIG = (new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 9).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)), new SimpleBlockPlacer())).whitelist(ImmutableSet.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE)).cannotProject().spreadX(10).spreadY(7).spreadZ(10).tries(256).build();

        //Skyroots
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SKYROOT_LOG.getDefaultState()), new StraightTrunkPlacer(4, 2, 0), new SimpleBlockStateProvider(SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(SKYROOT_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SKYROOT_LOG.getDefaultState()), new LargeOakTrunkPlacer(4, 11, 0), new SimpleBlockStateProvider(SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(SKYROOT_SAPLING.getDefaultState()), new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build();
        public static final TreeFeatureConfig MOTTLED_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MOTTLED_SKYROOT_LOG.getDefaultState()), new OvergrownTrunkPlacer(5, 10, 0, new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 1 / 14F), new SimpleBlockStateProvider(SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(SKYROOT_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig DWARF_MOTTLED_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MOTTLED_SKYROOT_LOG.getDefaultState()), new BendingTrunkPlacer(5, 3, 2, 4, UniformIntProvider.create(1, 3)), new SimpleBlockStateProvider(SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(SKYROOT_SAPLING.getDefaultState()), new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 68), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();

        //Fruit trees
        public static final TreeFeatureConfig CRYSTAL_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CRYSTAL_LOG.getDefaultState()), new StraightTrunkPlacer(5, 2, 2), new SimpleBlockStateProvider(CRYSTAL_LEAVES.getDefaultState()), new SimpleBlockStateProvider(CRYSTAL_SAPLING.getDefaultState()), new SpruceFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 1)), new TwoLayersFeatureSize(2, 0, 2))).ignoreVines().build();
        public static final TreeFeatureConfig ORANGE_TREE_SAPLING_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(ORANGE_LOG.getDefaultState()), new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)), new SimpleBlockStateProvider(ORANGE_LEAVES.getDefaultState()), new SimpleBlockStateProvider(ORANGE_SAPLING.getDefaultState()), new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63), new TwoLayersFeatureSize(1, 0, 1))).build();
        public static final TreeFeatureConfig ORANGE_TREE_WILD_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(ORANGE_LOG.getDefaultState()), new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)), new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ORANGE_LEAVES_BASIC, 2).add(ORANGE_LEAVES_FLOWERING, 2).add(ORANGE_LEAVES_FRUITING, 1)), new SimpleBlockStateProvider(ORANGE_SAPLING.getDefaultState()), new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63), new TwoLayersFeatureSize(1, 0, 1))).build();



        //Wisterias
        public static final TreeFeatureConfig ROSE_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(ROSE_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(ROSE_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig LAVENDER_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(LAVENDER_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(LAVENDER_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FROST_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(FROST_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(FROST_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig BOREAL_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(6, 3, 2), new SimpleBlockStateProvider(BOREAL_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(BOREAL_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(1, 3), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_ROSE_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(9, 4, 2), new SimpleBlockStateProvider(ROSE_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(ROSE_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 7), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_LAVENDER_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(9, 4, 2), new SimpleBlockStateProvider(LAVENDER_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(LAVENDER_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 7), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_FROST_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(11, 6, 3), new SimpleBlockStateProvider(FROST_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(FROST_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_BOREAL_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(11, 6, 3), new SimpleBlockStateProvider(BOREAL_WISTERIA_LEAVES.getDefaultState()), new SimpleBlockStateProvider(BOREAL_WISTERIA_SAPLING.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();

        //Special trees
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(GOLDEN_OAK_LOG.getDefaultState()), new LargeOakTrunkPlacer(4, 8, 0), new SimpleBlockStateProvider(GOLDEN_OAK_LEAVES.getDefaultState()), new SimpleBlockStateProvider(GOLDEN_OAK_SAPLING.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(3), 3), new TwoLayersFeatureSize(3, 0, 3, OptionalInt.of(2)))).ignoreVines().build();

        //Misc
        public static final Feature<DeltaFeatureConfig> AETHER_DELTA_FEATURE = register("aether_delta_feature", new AetherDeltaFeature(DeltaFeatureConfig.CODEC));
        public static final Feature<GroundcoverFeatureConfig> GROUNDCOVER_FEATURE = register("groundcover_feature", new GroundcoverFeature(GroundcoverFeature.CODEC));
        public static final Feature<PillarFeatureConfig> PILLAR_FEATURE = register("pillar_feature", new PillarFeature(PillarFeature.CODEC));

        public static final RandomFeatureConfig RAINBOW_FOREST_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(LAVENDER_WISTERIA_CONFIG).withChance(0.33F), Feature.TREE.configure(ROSE_WISTERIA_CONFIG).withChance(0.075F), Feature.TREE.configure(FANCY_LAVENDER_WISTERIA_CONFIG).withChance(0.025F), Feature.TREE.configure(FANCY_ROSE_WISTERIA_CONFIG).withChance(0.075F), Feature.TREE.configure(FROST_WISTERIA_CONFIG).withChance(0.0001F), Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.2F), Feature.TREE.configure(ORANGE_TREE_WILD_CONFIG).withChance(0.0125F)),
                Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG)
        );

        public static final RandomFeatureConfig SPARSE_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.TREE.configure(FANCY_SKYROOT_CONFIG).withChance(0.1F),
                        Feature.TREE.configure(ORANGE_TREE_WILD_CONFIG).withChance(0.02F)
                ),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig SCATTERED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(FANCY_SKYROOT_CONFIG).withChance(0.05F), Feature.TREE.configure(ROSE_WISTERIA_CONFIG).withChance(0.002F)),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig THICKET_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(ROSE_WISTERIA_CONFIG).withChance(0.0001F), Feature.TREE.configure(LAVENDER_WISTERIA_CONFIG).withChance(0.0001F), Feature.TREE.configure(GOLDEN_OAK_CONFIG).withChance(0.0025F), Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.1F)),
                Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig DENSE_SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(DWARF_MOTTLED_SKYROOT_CONFIG).withChance(0.1F), Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.05F)),
                Feature.TREE.configure(Configs.MOTTLED_SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(DWARF_MOTTLED_SKYROOT_CONFIG).withChance(0.15F)),
                Feature.TREE.configure(Configs.MOTTLED_SKYROOT_CONFIG)
        );


        private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, Aether.locate(name), feature);
        }
    }
}