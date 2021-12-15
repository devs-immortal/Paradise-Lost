package net.id.aether.world.feature;

import java.util.List;
import java.util.OptionalInt;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.natural.tree.FruitingLeavesBlock;
import net.id.aether.mixin.world.SimpleBlockStateProviderAccessor;
import net.id.aether.world.IcestoneSpireFeature;
import net.id.aether.world.feature.config.*;
import net.id.aether.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.aether.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.aether.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.stateprovider.NoiseBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;
import static net.id.aether.world.feature.AetherPlacedFeatures.*;
import static net.id.aether.world.feature.AetherPlacedFeatures.Configs.*;

@SuppressWarnings("unused")
public class AetherConfiguredFeatures {

    // todo all ".spreadHorizontally()"s have been commented out because they were shown to cause errors in 1.17.
    // if this is not the case anymore, add them back in (however that is done).

    public static final PlacementModifier SPREAD_32_ABOVE = HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.getTop());

    public static final ConfiguredFeature<?, ?> AETHER_BUSH = register("aether_bush", Feature.RANDOM_PATCH.configure(AETHER_BUSH_CONFIG));
    public static final ConfiguredFeature<?, ?> AETHER_DENSE_BUSH = register("aether_dense_bush", Feature.RANDOM_PATCH.configure(AETHER_DENSE_BUSH_CONFIG));
    public static final ConfiguredFeature<?, ?> AETHER_GRASS_BUSH = register("aether_grass", Feature.RANDOM_PATCH.configure(AETHER_GRASS_BUSH_CONFIG));
    public static final ConfiguredFeature<?, ?> AETHER_TALL_GRASS_BUSH = register("aether_tall_grass", Feature.RANDOM_PATCH.configure(AETHER_TALL_GRASS_BUSH_CONFIG));
    
    public static final ConfiguredFeature<?, ?> AETHER_FLOWERS = register("aether_flowers", Feature.FLOWER.configure(Configs.AETHER_FLOWERS));
    
    public static final ConfiguredFeature<TreeFeatureConfig, ?> SKYROOT_TREE = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG)); // TODO REPLACED WITH JSON //
    public static final ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK_TREE = register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CRYSTAL_TREE = register("crystal_tree", Feature.TREE.configure(Configs.CRYSTAL_TREE_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> ORANGE_TREE = register("orange_tree", Feature.TREE.configure(Configs.ORANGE_TREE_SAPLING_CONFIG));

    public static final ConfiguredFeature<TreeFeatureConfig, ?> ROSE_WISTERIA_TREE = register("rose_wisteria_tree", Feature.TREE.configure(Configs.ROSE_WISTERIA_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LAVENDER_WISTERIA_TREE = register("lavender_wisteria_tree", Feature.TREE.configure(Configs.LAVENDER_WISTERIA_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> FROST_WISTERIA_TREE = register("frost_wisteria_tree", Feature.TREE.configure(Configs.FROST_WISTERIA_CONFIG));
    public static final ConfiguredFeature<TreeFeatureConfig, ?> BOREAL_WISTERIA_TREE = register("boreal_wisteria_tree", Feature.TREE.configure(Configs.BOREAL_WISTERIA_CONFIG));

    public static final ConfiguredFeature<?, ?> FANCY_ROSE_WISTERIA_TREE = register("fancy_rose_wisteria_tree", Feature.TREE.configure(Configs.FANCY_ROSE_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_LAVENDER_WISTERIA_TREE = register("fancy_lavender_wisteria_tree", Feature.TREE.configure(Configs.FANCY_LAVENDER_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_FROST_WISTERIA_TREE = register("fancy_frost_wisteria_tree", Feature.TREE.configure(Configs.FANCY_FROST_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_BOREAL_WISTERIA_TREE = register("fancy_boreal_wisteria_tree", Feature.TREE.configure(Configs.FANCY_BOREAL_WISTERIA_CONFIG));
    public static final ConfiguredFeature<?, ?> FANCY_SKYROOT_TREE = register("fancy_skyroot_tree", Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG));

    // Used in json
    public static final ConfiguredFeature<?, ?> GENERIC_BOULDER = register("generic_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(MOSSY_HOLYSTONE.getDefaultState(), 1).add(COBBLED_HOLYSTONE.getDefaultState(), 3).build()), ConstantIntProvider.create(4), UniformIntProvider.create(3, 6))));
    public static final ConfiguredFeature<?, ?> PLAINS_BOULDER = register("plains_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(SimpleBlockStateProviderAccessor.callInit(COBBLED_HOLYSTONE.getDefaultState()), ConstantIntProvider.create(3), UniformIntProvider.create(3, 5)))); // TODO REPLACED WITH JSON //
    public static final ConfiguredFeature<?, ?> THICKET_BOULDER = register("thicket_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(MOSSY_HOLYSTONE.getDefaultState(), 4).add(COBBLED_HOLYSTONE.getDefaultState(), 1).build()), ConstantIntProvider.create(6), UniformIntProvider.create(2, 5))));
    public static final ConfiguredFeature<?, ?> GOLDEN_BOULDER = register("golden_boulder", Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(GOLDEN_MOSSY_HOLYSTONE.getDefaultState(), 4).add(COBBLED_HOLYSTONE.getDefaultState(), 1).build()), ConstantIntProvider.create(4), UniformIntProvider.create(3, 5))));

    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = register("fallen_leaves", Feature.RANDOM_PATCH.configure(FALLEN_LEAVES_CONFIG));
    public static final ConfiguredFeature<?, ?> ALT_FALLEN_LEAVES = register("alt_fallen_leaves", Feature.RANDOM_PATCH.configure(FALLEN_LEAVES_CONFIG));

    public static final ConfiguredFeature<?, ?> FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2)));
    public static final ConfiguredFeature<?, ?> FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2)));

    public static final ConfiguredFeature<?, ?> RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(SimpleBlockStateProviderAccessor.callInit(MALT_SPRIG.getDefaultState()), UniformIntProvider.create(3, 13), ConstantIntProvider.create(5), UniformIntProvider.create(3, 4), 1.4)));

    public static final ConfiguredFeature<?, ?> SHIELD_FOLIAGE = register("shield_foliage", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(64, 7, 3, () -> SHIELD_FOLIAGE_SINGLE_BLOCK)));
    public static final ConfiguredFeature<?, ?> SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", Feature.RANDOM_PATCH.configure(FALLEN_LEAVES_CONFIG));
    public static final ConfiguredFeature<?, ?> SHIELD_ROCKS = register("shield_rocks", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(96, 9, 3, () -> SHIELD_ROCKS_SINGLE_BLOCK)/*.cannotProject()*/));

    public static final ConfiguredFeature<?, ?> PLATEAU_FOLIAGE = register("plateau_foliage", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(96, 7, 3, () -> PLATEAU_FOLIAGE_SINGLE_BLOCK)));
    public static final ConfiguredFeature<?, ?> PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(SimpleBlockStateProviderAccessor.callInit(AETHER_GRASS_FLOWERING.getDefaultState()), UniformIntProvider.create(3, 10), ConstantIntProvider.create(5), UniformIntProvider.create(3, 6), 1.5)));
    public static final ConfiguredFeature<?, ?> PLATEAU_SHAMROCK = register("plateau_shamrock", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(SimpleBlockStateProviderAccessor.callInit(MALT_SPRIG.getDefaultState()), UniformIntProvider.create(2, 6), ConstantIntProvider.create(5), UniformIntProvider.create(4, 7), 1.4)));

    final static List<BlockState> GENERIC_FLOOR_WHITELIST = List.of(AETHER_GRASS_BLOCK.getDefaultState(), COARSE_AETHER_DIRT.getDefaultState(), HOLYSTONE.getDefaultState(), COBBLED_HOLYSTONE.getDefaultState());

    public static final ConfiguredFeature<?, ?> SHIELD_FLAX = register("shield_flax", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(96, 12, 5, () -> SHIELD_FLAX_SINGLE_BLOCK)));
    public static final ConfiguredFeature<?, ?> SHIELD_NETTLES = register("shield_nettles", Configs.HONEY_NETTLE_FEATURE.configure(new DefaultFeatureConfig()));

    public static final ConfiguredFeature<?, ?> THICKET_FALLEN_LOG = register("thicket_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 6), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(LIVERWORT_CARPET.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(LIVERWORT_CARPET.getDefaultState()), 0.5F, 0.35F, GENERIC_FLOOR_WHITELIST)));

    public static final ConfiguredFeature<?, ?> THICKET_LIVERWORT = register("thicket_liverwort", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(SimpleBlockStateProviderAccessor.callInit(LIVERWORT.getDefaultState()), UniformIntProvider.create(0, 2), UniformIntProvider.create(0, 1))));
    public static final ConfiguredFeature<?, ?> THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(SimpleBlockStateProviderAccessor.callInit(LIVERWORT_CARPET.getDefaultState()), UniformIntProvider.create(1, 4), ConstantIntProvider.create(5), UniformIntProvider.create(5, 8), 1.3)));
    public static final ConfiguredFeature<?, ?> THICKET_SHAMROCK = register("thicket_shamrock", Configs.ORGANIC_GROUNDCOVER_FEATURE.configure(new ProjectedOrganicCoverConfig(SimpleBlockStateProviderAccessor.callInit(SHAMROCK.getDefaultState()), UniformIntProvider.create(2, 6), ConstantIntProvider.create(5), UniformIntProvider.create(4, 7), 1.3)));

    public static final ConfiguredFeature<?, ?> MOTTLED_FALLEN_LOG = register("mottled_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 5), SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(AETHER_GRASS.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(ROOTCAP.getDefaultState()), 0.3F, 0.15F, GENERIC_FLOOR_WHITELIST)));
    public static final ConfiguredFeature<?, ?> MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", Configs.FALLEN_PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(3, 5), SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(AETHER_GRASS_FLOWERING.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(ROOTCAP.getDefaultState()), 0.4F, 0.25F, GENERIC_FLOOR_WHITELIST)));

    public static final ConfiguredFeature<?, ?> SHIELD_STUMPS = register("shield_stumps", Configs.PILLAR_FEATURE.configure(new LongFeatureConfig(UniformIntProvider.create(1, 2), SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(ROOTCAP.getDefaultState()), 0.1F, 0.225F, GENERIC_FLOOR_WHITELIST)));
    public static final ConfiguredFeature<?, ?> SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", Configs.PILLAR_FEATURE.configure(new LongFeatureConfig(ConstantIntProvider.create(1), SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_FALLEN_LOG.getDefaultState()), SimpleBlockStateProviderAccessor.callInit(ROOTCAP.getDefaultState()), 0.015F, 0.3F, GENERIC_FLOOR_WHITELIST)));

    public static final ConfiguredFeature<?, ?> SHIELD_PONDS = register("shield_pond", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.WATER.getDefaultState(), COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(Properties.WATERLOGGED, true), UniformIntProvider.create(2, 7), UniformIntProvider.create(1, 2))));

    public static final ConfiguredFeature<?, ?> SHIELD_STONE = register("shield_stone", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HOLYSTONE.getDefaultState(), 7).add(COBBLED_HOLYSTONE.getDefaultState(), 5).add(MOSSY_HOLYSTONE.getDefaultState(), 2).build()), UniformIntProvider.create(1, 4), UniformIntProvider.create(0, 0))));
    public static final ConfiguredFeature<?, ?> SHIELD_PODZOL = register("shield_podzol", Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AETHER_FROZEN_GRASS.getDefaultState()), UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 0))));

    public static final ConfiguredFeature<?, ?> TUNDRA_FOLIAGE = register("tundra_foliage", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(32, 7, 3, () -> TUNDRA_FOLIAGE_SINGLE_BLOCK)));

    public static final ConfiguredFeature<?, ?> TUNDRA_SPIRES = register("tundra_spires", Configs.ICESTONE_SPIRE_FEATURE.configure(FeatureConfig.DEFAULT));

    public static final ConfiguredFeature<?, ?> TUNDRA_PONDS = register("tundra_pond", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1))));
    public static final ConfiguredFeature<?, ?> TUNDRA_SNOW = register("tundra_snow", Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.POWDER_SNOW.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), UniformIntProvider.create(3, 8), UniformIntProvider.create(0, 1))));

    public static final ConfiguredFeature<?, ?> FREEZE_AETHER_TOP_LAYER = register("freeze_aether_top_layer", Configs.FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE.configure(FeatureConfig.DEFAULT));
    
    public static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, locate(id), configuredFeature);
    }
    
    public static void init() {
    }

    public static class Configs {

        public static final BlockState ORANGE_LEAVES_BASIC = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true);
        public static final BlockState ORANGE_LEAVES_FLOWERING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 1);
        public static final BlockState ORANGE_LEAVES_FRUITING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 2);

        // pre 1.18 : public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(PURPLE_FLOWER.getDefaultState(), 2).add(WHITE_FLOWER.getDefaultState(), 1)), new SimpleBlockPlacer())).tries(64).build();

        public static final List<Block> AETHER_GROUD = List.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE);
    
        public static final RandomPatchFeatureConfig AETHER_FLOWERS = new RandomPatchFeatureConfig(64, 7, 3, () -> Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(
            new DataPool.Builder<BlockState>()
                .add(ATARAXIA.getDefaultState(), 20)
                .add(CLOUDSBLUFF.getDefaultState(), 20)
                .add(DRIGEAN.getDefaultState(), 3)
                .add(LUMINAR.getDefaultState(), 8)
                .add(ANCIENT_FLOWER.getDefaultState(), 1)
                .build()
        ))).withInAirFilter());
        //Skyroots
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(SKYROOT_LOG.getDefaultState()), new StraightTrunkPlacer(4, 2, 0), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build(); // TODO REPLACED WITH JSON //
        public static final TreeFeatureConfig FANCY_SKYROOT_CONFIG = (
                new TreeFeatureConfig.Builder(
                        SimpleBlockStateProviderAccessor.callInit(SKYROOT_LOG.getDefaultState()),
                        new LargeOakTrunkPlacer(4, 11, 0),
                        SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()),
                        new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
                        new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build();
        public static final TreeFeatureConfig SKYROOT_SHRUB_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(SKYROOT_LOG.getDefaultState()), new StraightTrunkPlacer(1, 1, 0), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()),  new BlobFoliagePlacer(UniformIntProvider.create(2, 4), ConstantIntProvider.create(0), 2), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig MOTTLED_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_LOG.getDefaultState()), new OvergrownTrunkPlacer(5, 10, 0, SimpleBlockStateProviderAccessor.callInit(ROOTCAP.getDefaultState()), 1 / 14F), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()),  new BlobFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig DWARF_MOTTLED_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(MOTTLED_SKYROOT_LOG.getDefaultState()), new BendingTrunkPlacer(5, 3, 2, 4, UniformIntProvider.create(1, 3)), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()),  new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 68), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();

        //Fruit trees
        public static final TreeFeatureConfig CRYSTAL_TREE_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(CRYSTAL_LOG.getDefaultState()), new StraightTrunkPlacer(5, 2, 2), SimpleBlockStateProviderAccessor.callInit(CRYSTAL_LEAVES.getDefaultState()),  new SpruceFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 1)), new TwoLayersFeatureSize(2, 0, 2))).ignoreVines().build();
        public static final TreeFeatureConfig ORANGE_TREE_SAPLING_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(ORANGE_LOG.getDefaultState()), new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)), SimpleBlockStateProviderAccessor.callInit(ORANGE_LEAVES.getDefaultState()), new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63), new TwoLayersFeatureSize(1, 0, 1))).build();
        public static final TreeFeatureConfig ORANGE_TREE_WILD_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(ORANGE_LOG.getDefaultState()), new BendingTrunkPlacer(3, 2, 1, 3, UniformIntProvider.create(1, 2)), new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ORANGE_LEAVES_BASIC, 2).add(ORANGE_LEAVES_FLOWERING, 2).add(ORANGE_LEAVES_FRUITING, 1)), new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 4), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 63), new TwoLayersFeatureSize(1, 0, 1))).build();

        //Wisterias
        public static final TreeFeatureConfig ROSE_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(5, 9), UniformIntProvider.create(1, 3), UniformIntProvider.create(2, 5), UniformIntProvider.create(1, 3), ConstantFloatProvider.create(0.334F), 3, 3, 2), SimpleBlockStateProviderAccessor.callInit(ROSE_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 4), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig LAVENDER_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(5, 9), UniformIntProvider.create(1, 3), UniformIntProvider.create(2, 5), UniformIntProvider.create(1, 3), ConstantFloatProvider.create(0.334F), 3, 3, 2), SimpleBlockStateProviderAccessor.callInit(LAVENDER_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 4), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FROST_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(5, 9), UniformIntProvider.create(1, 3), UniformIntProvider.create(2, 5), UniformIntProvider.create(1, 3), ConstantFloatProvider.create(0.334F), 3, 3, 2), SimpleBlockStateProviderAccessor.callInit(FROST_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 4), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig BOREAL_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(7, 11), UniformIntProvider.create(2, 4), UniformIntProvider.create(2, 5), UniformIntProvider.create(2, 4), ConstantFloatProvider.create(0.334F), 4, 5, 2), SimpleBlockStateProviderAccessor.callInit(BOREAL_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_ROSE_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(10, 17), UniformIntProvider.create(3, 7), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 7), ConstantFloatProvider.create(0.334F), 6, 5, 3), SimpleBlockStateProviderAccessor.callInit(ROSE_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_LAVENDER_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(10, 17), UniformIntProvider.create(3, 7), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 7), ConstantFloatProvider.create(0.334F), 6, 5, 3), SimpleBlockStateProviderAccessor.callInit(LAVENDER_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_FROST_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(10, 17), UniformIntProvider.create(3, 7), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 7), ConstantFloatProvider.create(0.334F), 6, 5, 3), SimpleBlockStateProviderAccessor.callInit(FROST_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 5), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_BOREAL_WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(WISTERIA_LOG.getDefaultState()), new WisteriaTrunkPlacer(UniformIntProvider.create(13, 21), UniformIntProvider.create(3, 9), UniformIntProvider.create(4, 10), UniformIntProvider.create(3, 9), ConstantFloatProvider.create(0.334F), 7, 6, 3), SimpleBlockStateProviderAccessor.callInit(BOREAL_WISTERIA_LEAVES.getDefaultState()), new WisteriaFoliagePlacer(UniformIntProvider.create(3, 6), UniformIntProvider.create(0, 1)), new TwoLayersFeatureSize(3, 0, 3))).ignoreVines().build();

        //Special trees
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(GOLDEN_OAK_LOG.getDefaultState()), new LargeOakTrunkPlacer(4, 8, 0), SimpleBlockStateProviderAccessor.callInit(GOLDEN_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(3), 3), new TwoLayersFeatureSize(3, 0, 3, OptionalInt.of(2)))).ignoreVines().build();

        //Misc
        public static final Feature<DeltaFeatureConfig> AETHER_DELTA_FEATURE = register("aether_delta_feature", new AetherDeltaFeature(DeltaFeatureConfig.CODEC));
        public static final Feature<GroundcoverFeatureConfig> GROUNDCOVER_FEATURE = register("groundcover_feature", new GroundcoverFeature(GroundcoverFeature.CODEC));

        public static final Feature<BoulderFeatureConfig> BOULDER = register("boulder", new AetherBoulderFeature(BoulderFeatureConfig.CODEC));
        public static final Feature<LongFeatureConfig> PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
        public static final Feature<LongFeatureConfig> FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> HONEY_NETTLE_FEATURE = register("honey_nettle_feature", new HoneyNettleFeature(DefaultFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> ICESTONE_SPIRE_FEATURE = register("icestone_spire_feature", new IcestoneSpireFeature(DefaultFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE = register("freeze_aether_top_layer_feature", new FreezeAetherTopLayerFeature(DefaultFeatureConfig.CODEC));

        public static final ProjectedOrganicCoverFeature ORGANIC_GROUNDCOVER_FEATURE = register("organic_groundcover_feature", new ProjectedOrganicCoverFeature(ProjectedOrganicCoverConfig.CODEC));
    
        private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, locate(name), feature);
        }
    }
}
