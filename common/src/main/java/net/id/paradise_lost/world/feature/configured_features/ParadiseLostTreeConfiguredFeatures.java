package net.id.paradise_lost.world.feature.configured_features;

import net.id.paradise_lost.world.feature.ParadiseLostFeatures;
import net.id.paradise_lost.world.feature.configs.LongFeatureConfig;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostTreePlacedFeatures;
import net.id.paradise_lost.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.paradise_lost.world.feature.tree.placers.PointedBallFoliagePlacer;
import net.id.paradise_lost.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.paradise_lost.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.OptionalInt;

@SuppressWarnings("unused")
public class ParadiseLostTreeConfiguredFeatures extends ParadiseLostConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> AUREL_SHRUB = of("aurel_shrub");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AUREL_TREE = of("tree_aurel");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CRAGLANDS_TREES = of("trees_craglands");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DENSE_SHIELD_TREES = of("trees_dense_shield");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DWARF_MOTTLED_AUREL = of("tree_dwarf_mottled_aurel");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_LAVENDER_LEAVES = of("fallen_lavender_leaves");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_LEAVES = of("fallen_leaves");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_ROSE_LEAVES = of("fallen_rose_leaves");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_AUREL_TREE = of("tree_fancy_aurel");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_FROST_WISTERIA_TREE = of("tree_fancy_frost_wisteria");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_LAVENDER_WISTERIA_TREE = of("tree_fancy_lavender_wisteria");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_ROSE_WISTERIA_TREE = of("tree_fancy_rose_wisteria");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FROST_WISTERIA_TREE = of("tree_frost_wisteria");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_BROWN_SPORECAP = of("huge_brown_sporecap");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LAVENDER_WISTERIA_TREE = of("tree_lavender_wisteria");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MENTH_TREE = of("tree_menth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MIXED_TREES = of("trees_mixed");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOTHER_AUREL_TREE = of("tree_mother_aurel");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOTTLED_AUREL = of("tree_mottled_aurel");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOTTLED_FALLEN_LOG = of("mottled_fallen_log");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOTTLED_HOLLOW_FALLEN_LOG = of("mottled_hollow_fallen_log");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLATEAU_TREES = of("trees_plateau");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_FOREST_TREES = of("trees_rainbow_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROSE_WISTERIA_TREE = of("tree_rose_wisteria");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SCATTERED_TREES = of("trees_scattered");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_HOLLOW_STUMPS = of("shield_hollow_stumps");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_STUMPS = of("shield_stumps");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_TREES = of("trees_shield");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPARSE_TREES = of("trees_sparse");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICKET_AUREL_TREE = of("tree_thicket_aurel");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICKET_FALLEN_LOG = of("thicket_fallen_log");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICKET_TREES = of("trees_thicket");

    public static void init() {
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        register(context, AUREL_SHRUB, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState()), new StraightTrunkPlacer(1, 1, 0), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new BlobFoliagePlacer(UniformInt.of(1, 3), ConstantInt.of(0), 1), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, FALLEN_LAVENDER_LEAVES, Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 10, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.LAVENDER_WISTERIA_LEAF_PILE.get().defaultBlockState(), 10).add(BlockRegistry.LAVENDER_WISTERIA_LEAVES.get().defaultBlockState(), 1).build())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), BlockRegistry.HIGHLANDS_GRASS.get()))))));
        register(context, FALLEN_LEAVES, Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 10, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.AUREL_LEAF_PILE.get().defaultBlockState(), 8).add(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState(), 1).build())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), BlockRegistry.HIGHLANDS_GRASS.get()))))));
        register(context, FALLEN_ROSE_LEAVES, Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 10, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.ROSE_WISTERIA_LEAF_PILE.get().defaultBlockState(), 10).add(BlockRegistry.ROSE_WISTERIA_LEAVES.get().defaultBlockState(), 1).build())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), BlockRegistry.HIGHLANDS_GRASS.get()))))));
        register(context, HUGE_BROWN_SPORECAP, ParadiseLostFeatures.BROWN_SPORECAP_FEATURE, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BlockRegistry.BROWN_SPORECAP_BLOCK.get().defaultBlockState()), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState()), 3));
        register(context, MOTTLED_HOLLOW_FALLEN_LOG, ParadiseLostFeatures.FALLEN_PILLAR_FEATURE, new LongFeatureConfig(UniformInt.of(3, 5), BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_FALLEN_LOG.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.GRASS_FLOWERING.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.ROOTCAP.get().defaultBlockState()), 0.4f, 0.25f, HolderSet.direct(Block::builtInRegistryHolder, BlockRegistry.HIGHLANDS_GRASS.get(), BlockRegistry.COARSE_DIRT.get(), BlockRegistry.FLOESTONE.get(), BlockRegistry.COBBLED_FLOESTONE.get())));
        register(context, SHIELD_HOLLOW_STUMPS, ParadiseLostFeatures.PILLAR_FEATURE, new LongFeatureConfig(ConstantInt.of(1), BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_FALLEN_LOG.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_FALLEN_LOG.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.ROOTCAP.get().defaultBlockState()), 0.015f, 0.3f, HolderSet.direct(Block::builtInRegistryHolder, BlockRegistry.HIGHLANDS_GRASS.get(), BlockRegistry.COARSE_DIRT.get(), BlockRegistry.FLOESTONE.get(), BlockRegistry.COBBLED_FLOESTONE.get())));
        register(context, SHIELD_STUMPS, ParadiseLostFeatures.PILLAR_FEATURE, new LongFeatureConfig(UniformInt.of(1, 2), BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_LOG.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_FALLEN_LOG.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.ROOTCAP.get().defaultBlockState()), 0.1f, 0.225f, HolderSet.direct(Block::builtInRegistryHolder, BlockRegistry.HIGHLANDS_GRASS.get(), BlockRegistry.COARSE_DIRT.get(), BlockRegistry.FLOESTONE.get(), BlockRegistry.COBBLED_FLOESTONE.get())));
        register(context, THICKET_FALLEN_LOG, ParadiseLostFeatures.FALLEN_PILLAR_FEATURE, new LongFeatureConfig(UniformInt.of(3, 6), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.LIVERWORT_CARPET.get().defaultBlockState()), BlockStateProvider.simple(BlockRegistry.LIVERWORT_CARPET.get().defaultBlockState()), 0.5f, 0.35f, HolderSet.direct(Block::builtInRegistryHolder, BlockRegistry.HIGHLANDS_GRASS.get(), BlockRegistry.COARSE_DIRT.get(), BlockRegistry.FLOESTONE.get(), BlockRegistry.COBBLED_FLOESTONE.get())));
        register(context, AUREL_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState()), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new PointedBallFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1)), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, DWARF_MOTTLED_AUREL, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_LOG.get().defaultBlockState()), new OvergrownTrunkPlacer(7, 2, 0, BlockStateProvider.simple(BlockRegistry.ROOTCAP.get().defaultBlockState()), 0.12f), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new PointedBallFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1)), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.MOSSY_FLOESTONE.get().defaultBlockState())).build());
        register(context, FANCY_AUREL_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState()), new StraightTrunkPlacer(6, 3, 0), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new PointedBallFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2)), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, FANCY_FROST_WISTERIA_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.5f, 3.0f), UniformFloat.of(6.0f, 10.0f), 4, 3, 2), BlockStateProvider.simple(BlockRegistry.FROST_WISTERIA_LEAVES.get().defaultBlockState()), new WisteriaFoliagePlacer(UniformInt.of(3, 5), ConstantInt.of(0)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, FANCY_LAVENDER_WISTERIA_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.5f, 3.0f), UniformFloat.of(6.0f, 10.0f), 4, 3, 2), BlockStateProvider.simple(BlockRegistry.LAVENDER_WISTERIA_LEAVES.get().defaultBlockState()), new WisteriaFoliagePlacer(UniformInt.of(3, 5), ConstantInt.of(0)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, FANCY_ROSE_WISTERIA_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.5f, 3.0f), UniformFloat.of(6.0f, 10.0f), 4, 3, 2), BlockStateProvider.simple(BlockRegistry.ROSE_WISTERIA_LEAVES.get().defaultBlockState()), new WisteriaFoliagePlacer(UniformInt.of(3, 5), ConstantInt.of(0)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, FROST_WISTERIA_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.5f, 3.0f), UniformFloat.of(6.0f, 10.0f), 4, 3, 2), BlockStateProvider.simple(BlockRegistry.FROST_WISTERIA_LEAVES.get().defaultBlockState()), new WisteriaFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, LAVENDER_WISTERIA_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.5f, 3.0f), UniformFloat.of(6.0f, 10.0f), 4, 3, 2), BlockStateProvider.simple(BlockRegistry.LAVENDER_WISTERIA_LEAVES.get().defaultBlockState()), new WisteriaFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, MENTH_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.MENTH_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.2f, 2.0f), UniformFloat.of(6.0f, 10.0f), 6, 3, 2), BlockStateProvider.simple(BlockRegistry.MENTH_WOODSTUFF.leaves().get().defaultBlockState()), new AcaciaFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, MOTHER_AUREL_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get().defaultBlockState()), new FancyTrunkPlacer(6, 10, 1), BlockStateProvider.simple(BlockRegistry.MOTHER_AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(4), 3), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, MOTTLED_AUREL, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.MOTTLED_AUREL_LOG.get().defaultBlockState()), new OvergrownTrunkPlacer(10, 7, 0, BlockStateProvider.simple(BlockRegistry.ROOTCAP.get().defaultBlockState()), 0.07143f), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new PointedBallFoliagePlacer(ConstantInt.of(3), UniformInt.of(2, 3)), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.MOSSY_FLOESTONE.get().defaultBlockState())).build());
        register(context, ROSE_WISTERIA_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState()), new WisteriaTrunkPlacer(UniformInt.of(3, 4), UniformInt.of(3, 4), UniformFloat.of(1.5f, 3.0f), UniformFloat.of(6.0f, 10.0f), 4, 3, 2), BlockStateProvider.simple(BlockRegistry.ROSE_WISTERIA_LEAVES.get().defaultBlockState()), new WisteriaFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0)), new TwoLayersFeatureSize(3, 0, 3)).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())).build());
        register(context, THICKET_AUREL_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState()), new FancyTrunkPlacer(4, 12, 1), BlockStateProvider.simple(BlockRegistry.AUREL_WOODSTUFF.leaves().get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(4), 3), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))).ignoreVines().dirt(BlockStateProvider.simple(BlockRegistry.LIVERWORT.get().defaultBlockState())).build());
        register(context, CRAGLANDS_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_TREE), 0.05f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.MENTH_TREE)));
        register(context, DENSE_SHIELD_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.DWARF_MOTTLED_AUREL), 0.1f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_TREE), 0.05f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.MOTTLED_AUREL)));
        register(context, MIXED_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.DWARF_MOTTLED_AUREL), 0.4f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_TREE)));
        register(context, PLATEAU_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.DWARF_MOTTLED_AUREL), 0.225f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_SHRUB)));
        register(context, RAINBOW_FOREST_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.LAVENDER_WISTERIA_TREE), 0.33f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.FANCY_LAVENDER_WISTERIA_TREE), 0.025f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.FANCY_ROSE_WISTERIA_TREE), 0.075f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.FROST_WISTERIA_TREE), 0.005f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_TREE), 0.2f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.ROSE_WISTERIA_TREE)));
        register(context, SCATTERED_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.FANCY_AUREL_TREE), 0.05f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.FANCY_ROSE_WISTERIA_TREE), 0.0002f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_TREE)));
        register(context, SHIELD_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.DWARF_MOTTLED_AUREL), 0.05f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.MOTTLED_AUREL)));
        register(context, SPARSE_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.FANCY_AUREL_TREE), 0.1f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_TREE)));
        register(context, THICKET_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.AUREL_SHRUB), 0.1f), new WeightedPlacedFeature(placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.MOTHER_AUREL_TREE), 0.01f)), placedFeatures.getOrThrow(ParadiseLostTreePlacedFeatures.THICKET_AUREL_TREE)));
    }
}
