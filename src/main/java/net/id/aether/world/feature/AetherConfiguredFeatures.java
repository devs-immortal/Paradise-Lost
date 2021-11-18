package net.id.aether.world.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.natural.tree.FruitingLeavesBlock;
import net.id.aether.world.IcestoneSpireFeature;
import net.id.aether.world.feature.config.BoulderFeatureConfig;
import net.id.aether.world.feature.config.GroundcoverFeatureConfig;
import net.id.aether.world.feature.config.LongFeatureConfig;
import net.id.aether.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.aether.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.aether.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
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
import net.minecraft.world.gen.placer.DoublePlantPlacer;
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

@SuppressWarnings({"unchecked", "unused"})
public class AetherConfiguredFeatures extends ConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> AETHER_BUSH = register("aether_bush", Feature.RANDOM_PATCH.configure(Configs.AETHER_BUSH_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(5))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeatRandomly(4));

    public static final ConfiguredFeature<TreeFeatureConfig, ?> SKYROOT_TREE = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CRYSTAL_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("crystal_tree", Feature.TREE.configure(Configs.CRYSTAL_TREE_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> ORANGE_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("orange_tree", Feature.TREE.configure(Configs.ORANGE_TREE_SAPLING_CONFIG).decorate(Decorators.HEIGHTMAP));

    public static final ConfiguredFeature<TreeFeatureConfig, ?> ROSE_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("rose_wisteria_tree", Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LAVENDER_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("lavender_wisteria_tree", Feature.TREE.configure(Configs.LAVENDER_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FROST_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("frost_wisteria_tree", Feature.TREE.configure(Configs.FROST_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> BOREAL_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("boreal_wisteria_tree", Feature.TREE.configure(Configs.BOREAL_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));

    public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_ROSE_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_rose_wisteria_tree", Feature.TREE.configure(Configs.FANCY_ROSE_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_LAVENDER_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_lavender_wisteria_tree", Feature.TREE.configure(Configs.FANCY_LAVENDER_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_FROST_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_frost_wisteria_tree", Feature.TREE.configure(Configs.FANCY_FROST_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_BOREAL_WISTERIA_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_boreal_wisteria_tree", Feature.TREE.configure(Configs.FANCY_BOREAL_WISTERIA_CONFIG).decorate(Decorators.HEIGHTMAP));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_SKYROOT_TREE = (ConfiguredFeature<TreeFeatureConfig, ?>) register("fancy_skyroot_tree", Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG).decorate(Decorators.HEIGHTMAP));

    public static final ConfiguredFeature<?, ?> SCATTERED_TREES = register("scattered_trees", Feature.RANDOM_SELECTOR.configure(Configs.SCATTERED_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(10)))).spreadHorizontally().repeatRandomly(7);
    public static final ConfiguredFeature<?, ?> SHIELD_TREES = register("shield_trees", Feature.RANDOM_SELECTOR.configure(Configs.SHIELD_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(6)))).spreadHorizontally().repeatRandomly(5);
    public static final ConfiguredFeature<?, ?> DENSE_SHIELD_TREES = register("dense_shield_trees", Feature.RANDOM_SELECTOR.configure(Configs.DENSE_SHIELD_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(16))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(10)))).repeatRandomly(3);
    public static final ConfiguredFeature<?, ?> MIXED_TREES = register("mixed_trees", Feature.RANDOM_SELECTOR.configure(Configs.MIXED_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1)).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(10)))));
    public static final ConfiguredFeature<?, ?> SPARSE_TREES = register("sparse_trees", Feature.RANDOM_SELECTOR.configure(Configs.SPARSE_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(14))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(50)))).repeatRandomly(2);
    public static final ConfiguredFeature<?, ?> THICKET_TREES = register("thicket_trees", Feature.RANDOM_SELECTOR.configure(Configs.THICKET_TREES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(16)))).spreadHorizontally().repeatRandomly(3);
    public static final ConfiguredFeature<?, ?> RAINBOW_FOREST_TREES = register("wisteria_woods_trees", Feature.RANDOM_SELECTOR.configure(Configs.RAINBOW_FOREST_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(17)))).spreadHorizontally().repeatRandomly(4);

    // Used in json
    public static final ConfiguredFeature<?, ?> GENERIC_BOULDER = register("generic_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(MOSSY_HOLYSTONE.getDefaultState(), 1).add(COBBLED_HOLYSTONE.getDefaultState(), 3).build()), ConstantIntProvider.create(4), UniformIntProvider.create(3, 6))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(15))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))).repeatRandomly(1));
    public static final ConfiguredFeature<?, ?> PLAINS_BOULDER = register("plains_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new SimpleBlockStateProvider(COBBLED_HOLYSTONE.getDefaultState()), ConstantIntProvider.create(3), UniformIntProvider.create(3, 5))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(8))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))));
    public static final ConfiguredFeature<?, ?> THICKET_BOULDER = register("thicket_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(MOSSY_HOLYSTONE.getDefaultState(), 4).add(COBBLED_HOLYSTONE.getDefaultState(), 1).build()), ConstantIntProvider.create(6), UniformIntProvider.create(2, 5))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(2))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))).repeatRandomly(2));
    public static final ConfiguredFeature<?, ?> GOLDEN_BOULDER = register("golden_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(GOLDEN_MOSSY_HOLYSTONE.getDefaultState(), 4).add(COBBLED_HOLYSTONE.getDefaultState(), 1).build()), ConstantIntProvider.create(4), UniformIntProvider.create(3, 5))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(30))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))));

    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = register("fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
    public static final ConfiguredFeature<?, ?> ALT_FALLEN_LEAVES = register("alt_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));
    public static final ConfiguredFeature<?, ?> FALLEN_RAINBOW_LEAVES = register("rainbow_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.RAINBOW_LEAVES_CONFIG).decorate(Decorators.SPREAD_32_ABOVE).decorate(Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))).repeat(3));

    public static final ConfiguredFeature<?, ?> SHIELD_FOLIAGE = register("shield_foliage", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5).build()), new SimpleBlockPlacer()).build()).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(5))).repeatRandomly(2));
    public static final ConfiguredFeature<?, ?> SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", Feature.RANDOM_PATCH.configure(Configs.FALLEN_LEAVES_CONFIG).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(4))).spreadHorizontally().repeatRandomly(3)));
    public static final ConfiguredFeature<?, ?> SHIELD_ROCKS = register("shield_rocks", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), 10).add(COBBLED_HOLYSTONE.getDefaultState(), 4).build()), new SimpleBlockPlacer()).whitelist(Set.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE)).spreadX(9).spreadZ(9).tries(96).cannotProject().build()).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(3)).repeatRandomly(3).spreadHorizontally()));

    final static List<BlockState> GENERIC_FLOOR_WHITELIST = List.of(AETHER_GRASS_BLOCK.getDefaultState(), COARSE_AETHER_DIRT.getDefaultState(), HOLYSTONE.getDefaultState(), COBBLED_HOLYSTONE.getDefaultState());

    public static final ConfiguredFeature<?, ?> SHIELD_FLAX = register("shield_flax", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(WILD_FLAX.getDefaultState()), new DoublePlantPlacer()).spreadX(12).spreadZ(12).spreadY(5).tries(96).whitelist(Set.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE)).build()).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5)).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)).repeatRandomly(4).spreadHorizontally())));
    public static final ConfiguredFeature<?, ?> SHIELD_NETTLES = register("shield_nettles", Configs.HONEY_NETTLE_FEATURE.configure(new DefaultFeatureConfig()).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(20)).repeatRandomly(12).spreadHorizontally()));

    public static final ConfiguredFeature<?, ?> THICKET_FALLEN_LOG = register("thicket_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 6), new SimpleBlockStateProvider(SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.MOSS_CARPET.getDefaultState()), new SimpleBlockStateProvider(Blocks.MOSS_CARPET.getDefaultState()), 0.5F, 0.35F, GENERIC_FLOOR_WHITELIST)).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(3))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2))).repeatRandomly(2).spreadHorizontally());

    public static final ConfiguredFeature<?, ?> THICKET_COARSE_DIRT = register("thicket_coarse_dirt", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new SimpleBlockStateProvider(Blocks.MOSS_BLOCK.getDefaultState()), UniformIntProvider.create(0, 3), UniformIntProvider.create(0, 2))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))).repeatRandomly(2).spreadHorizontally());

    public static final ConfiguredFeature<?, ?> MOTTLED_FALLEN_LOG = register("mottled_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 5), new SimpleBlockStateProvider(MOTTLED_SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(AETHER_GRASS.getDefaultState()), new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 0.3F, 0.15F, GENERIC_FLOOR_WHITELIST)).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(3))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))));
    public static final ConfiguredFeature<?, ?> MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 5), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(AETHER_GRASS_FLOWERING.getDefaultState()), new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 0.4F, 0.25F, GENERIC_FLOOR_WHITELIST)).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(3))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))));

    public static final ConfiguredFeature<?, ?> SHIELD_STUMPS = register("shield_stumps", Configs.PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(1, 2), new SimpleBlockStateProvider(MOTTLED_SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 0.1F, 0.225F, GENERIC_FLOOR_WHITELIST)).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1)).repeatRandomly(1).spreadHorizontally()));
    public static final ConfiguredFeature<?, ?> SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", Configs.PILLAR_FEATURE.configure(new LongFeatureConfig(ConstantIntProvider.create(1), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 0.015F, 0.3F, GENERIC_FLOOR_WHITELIST)).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1))));

    public static final ConfiguredFeature<?, ?> SHIELD_PONDS = register("shield_pond", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.WATER.getDefaultState(), COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(Properties.WATERLOGGED, true), UniformIntProvider.create(2, 7), UniformIntProvider.create(1, 2))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(30))));

    public static final ConfiguredFeature<?, ?> SHIELD_STONE = register("shield_stone", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HOLYSTONE.getDefaultState(), 7).add(COBBLED_HOLYSTONE.getDefaultState(), 5).add(MOSSY_HOLYSTONE.getDefaultState(), 2).build()), UniformIntProvider.create(1, 4), UniformIntProvider.create(0, 0))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)))).repeatRandomly(2).spreadHorizontally();
    public static final ConfiguredFeature<?, ?> SHIELD_PODZOL = register("shield_podzol", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new SimpleBlockStateProvider(AETHER_FROZEN_GRASS.getDefaultState()), UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 0))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(15))).repeatRandomly(2).spreadHorizontally();

    public static final ConfiguredFeature<?, ?> TUNDRA_FOLIAGE = register("tundra_foliage", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_SHORT_GRASS.getDefaultState(), 30).add(AETHER_GRASS.getDefaultState(), 10).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 3).build()), new SimpleBlockPlacer()).tries(32).build()).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(3))).spreadHorizontally().repeatRandomly(3));

    public static final ConfiguredFeature<?, ?> TUNDRA_SPIRES = register("tundra_spires", Configs.ICESTONE_SPIRE_FEATURE.configure(FeatureConfig.DEFAULT).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(9))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1)).repeatRandomly(10).spreadHorizontally()));

    public static final ConfiguredFeature<?, ?> TUNDRA_PONDS = register("tundra_pond", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(1)).repeatRandomly(1)));
    public static final ConfiguredFeature<?, ?> TUNDRA_SNOW = register("tundra_snow", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.POWDER_SNOW.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), UniformIntProvider.create(3, 8), UniformIntProvider.create(0, 1))).decorate(Decorator.COUNT_MULTILAYER.configure(new CountConfig(2)).repeatRandomly(2)));



    public static final ConfiguredFeature<?, ?> FREEZE_AETHER_TOP_LAYER = register("freeze_aether_top_layer", Configs.FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE.configure(FeatureConfig.DEFAULT));

    public static void init() {


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
        public static final TreeFeatureConfig MOTTLED_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MOTTLED_SKYROOT_LOG.getDefaultState()), new OvergrownTrunkPlacer(5, 10, 0, new SimpleBlockStateProvider(ROOTCAP.getDefaultState()), 1 / 14F), new SimpleBlockStateProvider(SKYROOT_LEAVES.getDefaultState()), new SimpleBlockStateProvider(SKYROOT_SAPLING.getDefaultState()), new BlobFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
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

        public static final Feature<BoulderFeatureConfig> BOULDER = register("boulder", new AetherBoulderFeature(BoulderFeatureConfig.CODEC));
        public static final Feature<LongFeatureConfig> PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
        public static final Feature<LongFeatureConfig> FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> HONEY_NETTLE_FEATURE = register("honey_nettle_feature", new HoneyNettleFeature(DefaultFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> ICESTONE_SPIRE_FEATURE = register("icestone_spire_feature", new IcestoneSpireFeature(DefaultFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE = register("freeze_aether_top_layer_feature", new FreezeAetherTopLayerFeature(DefaultFeatureConfig.CODEC));

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

        public static final RandomFeatureConfig MIXED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(DWARF_MOTTLED_SKYROOT_CONFIG).withChance(0.4F)),
                Feature.TREE.configure(SKYROOT_CONFIG)
        );


        private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, Aether.locate(name), feature);
        }
    }
}