package net.id.aether.world.feature;

import com.google.common.collect.ImmutableList;
import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.natural.tree.FruitingLeavesBlock;
import net.id.aether.mixin.world.SimpleBlockStateProviderAccessor;
import net.id.aether.world.IcestoneSpireFeature;
import net.id.aether.world.feature.config.*;
import net.id.aether.world.feature.decorators.ChancePlacementModifier;
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
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;

@SuppressWarnings("unused")
public class AetherPlacedFeatures {

    // todo all ".spreadHorizontally()"s have been commented out because they were shown to cause errors in 1.17.
    // if this is not the case anymore, add them back in (however that is done).

    public static final PlacementModifier SPREAD_32_ABOVE = HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.getTop());

    public static final PlacedFeature AETHER_BUSH = register("aether_bush", AetherConfiguredFeatures.AETHER_BUSH.withPlacement(SPREAD_32_ABOVE, CountMultilayerPlacementModifier.of(5), new ChancePlacementModifier(ConstantIntProvider.create(4))));

    // TODO: figure out whether to register placed features or configured features. Or both.
    public static final PlacedFeature SKYROOT_TREE = register("skyroot_tree", AetherConfiguredFeatures.SKYROOT_TREE.withPlacement());
    public static final PlacedFeature GOLDEN_OAK_TREE = register("golden_oak_tree", AetherConfiguredFeatures.GOLDEN_OAK_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature CRYSTAL_TREE = register("crystal_tree", AetherConfiguredFeatures.CRYSTAL_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature ORANGE_TREE = register("orange_tree", AetherConfiguredFeatures.ORANGE_TREE.withPlacement(SPREAD_32_ABOVE));

    public static final PlacedFeature ROSE_WISTERIA_TREE = register("rose_wisteria_tree", AetherConfiguredFeatures.ROSE_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature LAVENDER_WISTERIA_TREE = register("lavender_wisteria_tree", AetherConfiguredFeatures.LAVENDER_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature FROST_WISTERIA_TREE = register("frost_wisteria_tree", AetherConfiguredFeatures.FROST_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature BOREAL_WISTERIA_TREE = register("boreal_wisteria_tree", AetherConfiguredFeatures.BOREAL_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));

    public static final PlacedFeature FANCY_ROSE_WISTERIA_TREE = register("fancy_rose_wisteria_tree", AetherConfiguredFeatures.FANCY_ROSE_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature FANCY_LAVENDER_WISTERIA_TREE = register("fancy_lavender_wisteria_tree", AetherConfiguredFeatures.FANCY_LAVENDER_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature FANCY_FROST_WISTERIA_TREE = register("fancy_frost_wisteria_tree", AetherConfiguredFeatures.FANCY_FROST_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature FANCY_BOREAL_WISTERIA_TREE = register("fancy_boreal_wisteria_tree", AetherConfiguredFeatures.FANCY_BOREAL_WISTERIA_TREE.withPlacement(SPREAD_32_ABOVE));
    public static final PlacedFeature FANCY_SKYROOT_TREE = register("fancy_skyroot_tree", AetherConfiguredFeatures.FANCY_SKYROOT_TREE.withPlacement(SPREAD_32_ABOVE));

    // TCF_ese are here because if these are not here, it doesn't work.
    public static final ConfiguredFeature<?, ?> CF_SCATTERED_TREES = AetherConfiguredFeatures.register("scattered_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.SCATTERED_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_SHIELD_TREES = AetherConfiguredFeatures.register("shield_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.SHIELD_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_DENSE_SHIELD_TREES = AetherConfiguredFeatures.register("dense_shield_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.DENSE_SHIELD_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_PLATEAU_TREES = AetherConfiguredFeatures.register("plateau_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.PLATEAU_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_MIXED_TREES = AetherConfiguredFeatures.register("mixed_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.MIXED_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_SPARSE_TREES = AetherConfiguredFeatures.register("sparse_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.SPARSE_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_THICKET_TREES = AetherConfiguredFeatures.register("thicket_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.THICKET_TREES_CONFIG));
    public static final ConfiguredFeature<?, ?> CF_RAINBOW_FOREST_TREES = AetherConfiguredFeatures.register("wisteria_woods_trees", Feature.RANDOM_SELECTOR.configure(AetherPlacedFeatures.Configs.RAINBOW_FOREST_CONFIG));

    public static final PlacedFeature SCATTERED_TREES = register("scattered_trees", CF_SCATTERED_TREES.withPlacement(CountMultilayerPlacementModifier.of(10), CountPlacementModifier.of(UniformIntProvider.create(0, 7))))/*.spreadHorizontally()*/;
    public static final PlacedFeature SHIELD_TREES = register("shield_trees", CF_SHIELD_TREES.withPlacement(CountMultilayerPlacementModifier.of(6), CountPlacementModifier.of(UniformIntProvider.create(0, 5))))/*.spreadHorizontally()*/;
    public static final PlacedFeature DENSE_SHIELD_TREES = register("dense_shield_trees", CF_DENSE_SHIELD_TREES.withPlacement(CountMultilayerPlacementModifier.of(16), new ChancePlacementModifier(ConstantIntProvider.create(10))));
    public static final PlacedFeature PLATEAU_TREES = register("plateau_trees", CF_PLATEAU_TREES.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))))/*.spreadHorizontally()*/;
    public static final PlacedFeature MIXED_TREES = register("mixed_trees", CF_MIXED_TREES.withPlacement(CountMultilayerPlacementModifier.of(1), new ChancePlacementModifier(ConstantIntProvider.create(10))));
    public static final PlacedFeature SPARSE_TREES = register("sparse_trees", CF_SPARSE_TREES.withPlacement(CountMultilayerPlacementModifier.of(14), new ChancePlacementModifier(ConstantIntProvider.create(50)), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    public static final PlacedFeature THICKET_TREES = register("thicket_trees", CF_THICKET_TREES.withPlacement(CountMultilayerPlacementModifier.of(20), CountPlacementModifier.of(UniformIntProvider.create(0, 5))))/*.spreadHorizontally()*/;
    public static final PlacedFeature RAINBOW_FOREST_TREES = register("wisteria_woods_trees", CF_RAINBOW_FOREST_TREES.withPlacement(CountMultilayerPlacementModifier.of(17), CountPlacementModifier.of(UniformIntProvider.create(0, 4))))/*.spreadHorizontally()*/;

    // Used in json
    public static final PlacedFeature GENERIC_BOULDER = register("generic_boulder", AetherConfiguredFeatures.GENERIC_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(15)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))));
    public static final PlacedFeature PLAINS_BOULDER = register("plains_boulder", AetherConfiguredFeatures.PLAINS_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(8)), CountMultilayerPlacementModifier.of(1))); // TODO REPLACED WITH JSON //
    public static final PlacedFeature THICKET_BOULDER = register("thicket_boulder", AetherConfiguredFeatures.THICKET_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    public static final PlacedFeature GOLDEN_BOULDER = register("golden_boulder", AetherConfiguredFeatures.GOLDEN_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(30)), CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature FALLEN_LEAVES = register("fallen_leaves", AetherConfiguredFeatures.FALLEN_LEAVES.withPlacement(SPREAD_32_ABOVE, PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, CountPlacementModifier.of(3), new ChancePlacementModifier(ConstantIntProvider.create(5)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Configs.AETHER_GROUD))));
    public static final PlacedFeature ALT_FALLEN_LEAVES = register("alt_fallen_leaves", AetherConfiguredFeatures.ALT_FALLEN_LEAVES.withPlacement(SPREAD_32_ABOVE, PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, CountPlacementModifier.of(3), new ChancePlacementModifier(ConstantIntProvider.create(5)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Configs.AETHER_GROUD))));

    public static final PlacedFeature FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", AetherConfiguredFeatures.FALLEN_ROSE_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);
    public static final PlacedFeature FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", AetherConfiguredFeatures.FALLEN_LAVENDER_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);

    public static final PlacedFeature RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", AetherConfiguredFeatures.RAINBOW_MALT_SPRIGS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))/*.spreadHorizontally()*/));

    // These "single block" features might also need to be registered in configured features, but also maybe not.
    public static final PlacedFeature SHIELD_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5)))).withPlacement();
    public static final PlacedFeature SHIELD_FOLIAGE = register("shield_foliage", AetherConfiguredFeatures.SHIELD_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(5), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    public static final PlacedFeature SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", AetherConfiguredFeatures.SHIELD_FALLEN_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(2), new ChancePlacementModifier(ConstantIntProvider.create(4)), CountPlacementModifier.of(UniformIntProvider.create(0, 3)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Configs.AETHER_GROUD)))/*.spreadHorizontally()*/);
    public static final PlacedFeature SHIELD_ROCKS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), 10).add(COBBLED_HOLYSTONE.getDefaultState(), 4).build()))).withPlacement();
    public static final PlacedFeature SHIELD_ROCKS = register("shield_rocks", AetherConfiguredFeatures.SHIELD_ROCKS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);

    public static final PlacedFeature PLATEAU_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5)))).withPlacement();
    public static final PlacedFeature PLATEAU_FOLIAGE = register("plateau_foliage", AetherConfiguredFeatures.PLATEAU_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 4)))/*.spreadHorizontally()*/);
    public static final PlacedFeature PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", AetherConfiguredFeatures.PLATEAU_FLOWERING_GRASS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 5)))/*.spreadHorizontally()*/);
    public static final PlacedFeature PLATEAU_SHAMROCK = register("plateau_shamrock", AetherConfiguredFeatures.PLATEAU_SHAMROCK.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(6)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2))/*.spreadHorizontally()*/));

    final static List<BlockState> GENERIC_FLOOR_WHITELIST = List.of(AETHER_GRASS_BLOCK.getDefaultState(), COARSE_AETHER_DIRT.getDefaultState(), HOLYSTONE.getDefaultState(), COBBLED_HOLYSTONE.getDefaultState());

    public static final PlacedFeature SHIELD_FLAX_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(WILD_FLAX))).withPlacement();
    public static final PlacedFeature SHIELD_FLAX = register("shield_flax", AetherConfiguredFeatures.SHIELD_FLAX.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(5)), CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 4)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(List.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE))))/*.spreadHorizontally()*/);
    public static final PlacedFeature SHIELD_NETTLES = register("shield_nettles", AetherConfiguredFeatures.SHIELD_NETTLES.withPlacement(CountMultilayerPlacementModifier.of(20), CountPlacementModifier.of(UniformIntProvider.create(0, 12))/*.spreadHorizontally()*/));

    public static final PlacedFeature THICKET_FALLEN_LOG = register("thicket_fallen_log", AetherConfiguredFeatures.THICKET_FALLEN_LOG.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2)))/*.spreadHorizontally()*/);

    public static final PlacedFeature THICKET_LIVERWORT = register("thicket_liverwort", AetherConfiguredFeatures.THICKET_LIVERWORT.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2))/*.spreadHorizontally()*/));
    public static final PlacedFeature THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", AetherConfiguredFeatures.THICKET_LIVERWORT_CARPET.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(6)), CountMultilayerPlacementModifier.of(1)));
    public static final PlacedFeature THICKET_SHAMROCK = register("thicket_shamrock", AetherConfiguredFeatures.THICKET_SHAMROCK.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(8)), CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature MOTTLED_FALLEN_LOG = register("mottled_fallen_log", AetherConfiguredFeatures.MOTTLED_FALLEN_LOG.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(1)));
    public static final PlacedFeature MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", AetherConfiguredFeatures.MOTTLED_HOLLOW_FALLEN_LOG.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(3)), CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature SHIELD_STUMPS = register("shield_stumps", AetherConfiguredFeatures.SHIELD_STUMPS.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))/*.spreadHorizontally()*/));
    public static final PlacedFeature SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", AetherConfiguredFeatures.SHIELD_HOLLOW_STUMPS.withPlacement(CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature SHIELD_PONDS = register("shield_pond", AetherConfiguredFeatures.SHIELD_PONDS.withPlacement(CountMultilayerPlacementModifier.of(30)));

    public static final PlacedFeature SHIELD_STONE = register("shield_stone", AetherConfiguredFeatures.SHIELD_STONE.withPlacement(CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2))))/*.spreadHorizontally()*/;
    public static final PlacedFeature SHIELD_PODZOL = register("shield_podzol", AetherConfiguredFeatures.SHIELD_PODZOL.withPlacement(CountMultilayerPlacementModifier.of(2), new ChancePlacementModifier(ConstantIntProvider.create(15)), CountPlacementModifier.of(UniformIntProvider.create(0, 2))))/*.spreadHorizontally()*/;

    public static final PlacedFeature TUNDRA_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_SHORT_GRASS.getDefaultState(), 30).add(AETHER_GRASS.getDefaultState(), 10).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 3)))).withPlacement();
    public static final PlacedFeature TUNDRA_FOLIAGE = register("tundra_foliage", AetherConfiguredFeatures.TUNDRA_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);

    public static final PlacedFeature TUNDRA_SPIRES = register("tundra_spires", AetherConfiguredFeatures.TUNDRA_SPIRES.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(9)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 10))/*.spreadHorizontally()*/));

    public static final PlacedFeature TUNDRA_PONDS = register("tundra_pond", AetherConfiguredFeatures.TUNDRA_PONDS.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))));
    public static final PlacedFeature TUNDRA_SNOW = register("tundra_snow", AetherConfiguredFeatures.TUNDRA_SNOW.withPlacement(CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    
    private static PlacedFeature register(String id, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, locate(id), feature);
    }
    
    public static void init() {}

    public static class Configs {

        public static final BlockState ORANGE_LEAVES_BASIC = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true);
        public static final BlockState ORANGE_LEAVES_FLOWERING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 1);
        public static final BlockState ORANGE_LEAVES_FRUITING = ORANGE_LEAVES.getDefaultState().with(FruitingLeavesBlock.CAPPED, true).with(FruitingLeavesBlock.NATURAL, true).with(FruitingLeavesBlock.GROWTH, 2);

        // pre 1.18 : public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(PURPLE_FLOWER.getDefaultState(), 2).add(WHITE_FLOWER.getDefaultState(), 1)), new SimpleBlockPlacer())).tries(64).build();

        public static final List<Block> AETHER_GROUD = List.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE);

        public static final PlacedFeature AETHER_BUSH_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AetherBlocks.AETHER_BUSH.getDefaultState()))).withPlacement();

        public static final PlacedFeature AETHER_GRASS_PLANT_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AETHER_GRASS.getDefaultState()))).withPlacement();

        public static final PlacedFeature AETHER_TALL_GRASS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AETHER_TALL_GRASS.getDefaultState()))).withPlacement();

        public static final PlacedFeature FLUTEGRASS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(FLUTEGRASS.getDefaultState()))).withPlacement();

        public static final PlacedFeature FALLEN_LEAVES_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(SKYROOT_LEAF_PILE.getDefaultState(), 8).add(SKYROOT_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)))).withPlacement();

        public static final PlacedFeature RAINBOW_LEAVES_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 9).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 1)))).withPlacement();
        public static final RandomPatchFeatureConfig RAINBOW_LEAVES_CONFIG = (new RandomPatchFeatureConfig(256, 10, 7, () -> RAINBOW_LEAVES_SINGLE_BLOCK))/*.cannotProject()*/;

        //Skyroots
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(SKYROOT_LOG.getDefaultState()), new StraightTrunkPlacer(4, 2, 0), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()),  new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig FANCY_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(SKYROOT_LOG.getDefaultState()), new LargeOakTrunkPlacer(4, 11, 0), SimpleBlockStateProviderAccessor.callInit(SKYROOT_LEAVES.getDefaultState()),  new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build();
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

        public static final RandomFeatureConfig RAINBOW_FOREST_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(LAVENDER_WISTERIA_TREE, 0.33F),
                        new RandomFeatureEntry(ROSE_WISTERIA_TREE, 0.075F),
                        new RandomFeatureEntry(FANCY_LAVENDER_WISTERIA_TREE, 0.025F),
                        new RandomFeatureEntry(FANCY_ROSE_WISTERIA_TREE, 0.075F),
                        new RandomFeatureEntry(FROST_WISTERIA_TREE, 0.0001F),
                        new RandomFeatureEntry(SKYROOT_TREE, 0.2F),
                        new RandomFeatureEntry(placedTree(ORANGE_TREE_WILD_CONFIG), 0.0125F)),
                ROSE_WISTERIA_TREE
        );

        public static final RandomFeatureConfig SPARSE_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(FANCY_SKYROOT_TREE, 0.1F),
                        new RandomFeatureEntry(placedTree(ORANGE_TREE_WILD_CONFIG), 0.02F)
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
                        new RandomFeatureEntry(placedTree(SKYROOT_SHRUB_CONFIG), 0.15F), // convert to feature
                        new RandomFeatureEntry(SKYROOT_TREE, 0.75F)),
                FANCY_SKYROOT_TREE
        );

        public static final RandomFeatureConfig DENSE_SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(DWARF_MOTTLED_SKYROOT_CONFIG), 0.1F),
                        new RandomFeatureEntry(SKYROOT_TREE, 0.05F)),
                placedTree(Configs.MOTTLED_SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig SHIELD_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(DWARF_MOTTLED_SKYROOT_CONFIG), 0.15F)),
                placedTree(Configs.MOTTLED_SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig PLATEAU_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(ORANGE_TREE_WILD_CONFIG), 0.05F),
                        new RandomFeatureEntry(placedTree(DWARF_MOTTLED_SKYROOT_CONFIG), 0.225F)),
                placedTree(SKYROOT_SHRUB_CONFIG)
        );

        public static final RandomFeatureConfig MIXED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(
                        new RandomFeatureEntry(placedTree(DWARF_MOTTLED_SKYROOT_CONFIG), 0.4F)),
                SKYROOT_TREE
        );

        // Other Special Things
        public static final PlacedFeature QUICKSOIL = AetherConfiguredFeatures.QUICKSOIL.withPlacement();

        private static PlacedFeature placedTree(TreeFeatureConfig cfg) {
            return Feature.TREE.configure(cfg).withPlacement();
        }

        private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, Aether.locate(name), feature);
        }
    }
}
