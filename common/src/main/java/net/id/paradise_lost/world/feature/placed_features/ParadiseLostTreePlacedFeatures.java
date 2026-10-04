package net.id.paradise_lost.world.feature.placed_features;

import net.id.paradise_lost.world.feature.configured_features.ParadiseLostMiscConfiguredFeatures;
import net.id.paradise_lost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.id.paradise_lost.world.feature.configured_features.ParadiseLostVegetationConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

import java.util.List;

@SuppressWarnings("unused")
public class ParadiseLostTreePlacedFeatures extends ParadiseLostPlacedFeatures {

    public static final ResourceKey<PlacedFeature> AUREL_SHRUB = of("aurel_shrub");
    public static final ResourceKey<PlacedFeature> AUREL_TREE = of("tree_aurel");
    public static final ResourceKey<PlacedFeature> CRAGLANDS_TREES = of("trees_craglands");
    public static final ResourceKey<PlacedFeature> DENSE_SHIELD_TREES = of("trees_dense_shield");
    public static final ResourceKey<PlacedFeature> DWARF_MOTTLED_AUREL = of("tree_dwarf_mottled_aurel");
    public static final ResourceKey<PlacedFeature> FALLEN_LAVENDER_LEAVES = of("fallen_lavender_leaves");
    public static final ResourceKey<PlacedFeature> FALLEN_LEAVES = of("fallen_leaves");
    public static final ResourceKey<PlacedFeature> FALLEN_ROSE_LEAVES = of("fallen_rose_leaves");
    public static final ResourceKey<PlacedFeature> FANCY_AUREL_TREE = of("tree_fancy_aurel");
    public static final ResourceKey<PlacedFeature> FANCY_FROST_WISTERIA_TREE = of("tree_fancy_frost_wisteria");
    public static final ResourceKey<PlacedFeature> FANCY_LAVENDER_WISTERIA_TREE = of("tree_fancy_lavender_wisteria");
    public static final ResourceKey<PlacedFeature> FANCY_ROSE_WISTERIA_TREE = of("tree_fancy_rose_wisteria");
    public static final ResourceKey<PlacedFeature> FROST_WISTERIA_TREE = of("tree_frost_wisteria");
    public static final ResourceKey<PlacedFeature> HUGE_BROWN_SPORECAP = of("huge_brown_sporecap");
    public static final ResourceKey<PlacedFeature> LAVENDER_WISTERIA_TREE = of("tree_lavender_wisteria");
    public static final ResourceKey<PlacedFeature> MENTH_TREE = of("tree_menth");
    public static final ResourceKey<PlacedFeature> MIXED_TREES = of("trees_mixed");
    public static final ResourceKey<PlacedFeature> MOTHER_AUREL_TREE = of("tree_mother_aurel");
    public static final ResourceKey<PlacedFeature> MOTTLED_AUREL = of("tree_mottled_aurel");
    public static final ResourceKey<PlacedFeature> MOTTLED_FALLEN_LOG = of("mottled_fallen_log");
    public static final ResourceKey<PlacedFeature> MOTTLED_HOLLOW_FALLEN_LOG = of("mottled_hollow_fallen_log");
    public static final ResourceKey<PlacedFeature> PLATEAU_TREES = of("trees_plateau");
    public static final ResourceKey<PlacedFeature> RAINBOW_FOREST_TREES = of("trees_rainbow_forest");
    public static final ResourceKey<PlacedFeature> ROSE_WISTERIA_TREE = of("tree_rose_wisteria");
    public static final ResourceKey<PlacedFeature> SCATTERED_TREES = of("trees_scattered");
    public static final ResourceKey<PlacedFeature> SHIELD_HOLLOW_STUMPS = of("shield_hollow_stumps");
    public static final ResourceKey<PlacedFeature> SHIELD_STUMPS = of("shield_stumps");
    public static final ResourceKey<PlacedFeature> SHIELD_TREES = of("trees_shield");
    public static final ResourceKey<PlacedFeature> SPARSE_TREES = of("trees_sparse");
    public static final ResourceKey<PlacedFeature> THICKET_AUREL_TREE = of("tree_thicket_aurel");
    public static final ResourceKey<PlacedFeature> THICKET_FALLEN_LOG = of("thicket_fallen_log");
    public static final ResourceKey<PlacedFeature> THICKET_TREES = of("trees_thicket");

    public static void init() {
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, AUREL_SHRUB, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.AUREL_SHRUB),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, FALLEN_LAVENDER_LEAVES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FALLEN_LAVENDER_LEAVES),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                CountPlacement.of(2));
        register(context, FALLEN_LEAVES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FALLEN_LEAVES),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                CountPlacement.of(3));
        register(context, FALLEN_ROSE_LEAVES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FALLEN_ROSE_LEAVES),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                CountPlacement.of(2));
        register(context, MOTTLED_HOLLOW_FALLEN_LOG, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.MOTTLED_HOLLOW_FALLEN_LOG),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                RarityFilter.onAverageOnceEvery(2));
        register(context, SHIELD_HOLLOW_STUMPS, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.SHIELD_HOLLOW_STUMPS),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                RarityFilter.onAverageOnceEvery(4));
        register(context, SHIELD_STUMPS, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.SHIELD_STUMPS),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                RarityFilter.onAverageOnceEvery(2));
        register(context, THICKET_FALLEN_LOG, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.THICKET_FALLEN_LOG),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                CountPlacement.of(2));
        register(context, AUREL_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.AUREL_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, DWARF_MOTTLED_AUREL, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.DWARF_MOTTLED_AUREL),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, FANCY_AUREL_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FANCY_AUREL_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, FANCY_FROST_WISTERIA_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FANCY_FROST_WISTERIA_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.FROST_WISTERIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)));
        register(context, FANCY_LAVENDER_WISTERIA_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FANCY_LAVENDER_WISTERIA_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.LAVENDER_WISTERIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)));
        register(context, FANCY_ROSE_WISTERIA_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FANCY_ROSE_WISTERIA_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.ROSE_WISTERIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)));
        register(context, FROST_WISTERIA_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.FROST_WISTERIA_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.FROST_WISTERIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)));
        register(context, LAVENDER_WISTERIA_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.LAVENDER_WISTERIA_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.LAVENDER_WISTERIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)));
        register(context, MENTH_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.MENTH_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.MENTH_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, MOTHER_AUREL_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.MOTHER_AUREL_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, MOTTLED_AUREL, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.MOTTLED_AUREL),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, ROSE_WISTERIA_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.ROSE_WISTERIA_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.ROSE_WISTERIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)));
        register(context, THICKET_AUREL_TREE, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.THICKET_AUREL_TREE),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockRegistry.AUREL_WOODSTUFF.sapling().get().defaultBlockState(), BlockPos.ZERO)));
        register(context, CRAGLANDS_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.CRAGLANDS_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 9).add(ConstantInt.of(3), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, DENSE_SHIELD_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.DENSE_SHIELD_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(4), 9).add(ConstantInt.of(6), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, MIXED_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.MIXED_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(0), 15).add(ConstantInt.of(1), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, PLATEAU_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.PLATEAU_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(0), 10).add(ConstantInt.of(1), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, RAINBOW_FOREST_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.RAINBOW_FOREST_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(11), 9).add(ConstantInt.of(12), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, SCATTERED_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.SCATTERED_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(5), 9).add(ConstantInt.of(6), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, SHIELD_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.SHIELD_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(0), 6).add(ConstantInt.of(1), 6).add(ConstantInt.of(10), 3).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, SPARSE_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.SPARSE_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(0), 15).add(ConstantInt.of(1), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
        register(context, THICKET_TREES, configuredFeatures.getOrThrow(ParadiseLostTreeConfiguredFeatures.THICKET_TREES),
                CountPlacement.of(new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(6), 7).add(ConstantInt.of(8), 1).build())),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome());
    }
}
