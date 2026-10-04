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
public class ParadiseLostVegetationPlacedFeatures extends ParadiseLostPlacedFeatures {

    public static final ResourceKey<PlacedFeature> BUSH = of("patch_bush");
    public static final ResourceKey<PlacedFeature> DENSE_BUSH = of("patch_dense_bush");
    public static final ResourceKey<PlacedFeature> FLOWERS = of("patch_flowers");
    public static final ResourceKey<PlacedFeature> FLOWERS_FOREST = of("patch_flowers_forest");
    public static final ResourceKey<PlacedFeature> FLOWERS_THICKET = of("patch_flowers_thicket");
    public static final ResourceKey<PlacedFeature> GRASS = of("patch_grass");
    public static final ResourceKey<PlacedFeature> NATURAL_SWEDROOT = of("natural_swedroot");
    public static final ResourceKey<PlacedFeature> PATCH_BLACKCURRANT = of("patch_blackcurrant");
    public static final ResourceKey<PlacedFeature> PATCH_BROWN_SPORECAP = of("patch_brown_sporecap");
    public static final ResourceKey<PlacedFeature> PATCH_BROWN_SPORECAP_COMMON = of("patch_brown_sporecap_common");
    public static final ResourceKey<PlacedFeature> PATCH_PINK_SPORECAP = of("patch_pink_sporecap");
    public static final ResourceKey<PlacedFeature> PLATEAU_FLOWERING_GRASS = of("patch_plateau_flowering_grass");
    public static final ResourceKey<PlacedFeature> PLATEAU_FOLIAGE = of("patch_plateau_foliage");
    public static final ResourceKey<PlacedFeature> PLATEAU_SHAMROCK = of("patch_plateau_shamrock");
    public static final ResourceKey<PlacedFeature> SHIELD_FLAX = of("patch_shield_flax");
    public static final ResourceKey<PlacedFeature> SHIELD_FOLIAGE = of("patch_shield_foliage");
    public static final ResourceKey<PlacedFeature> SHIELD_NETTLES = of("patch_shield_nettles");
    public static final ResourceKey<PlacedFeature> TALL_GRASS = of("patch_tall_grass");
    public static final ResourceKey<PlacedFeature> THICKET_LIVERWORT_CARPET = of("patch_thicket_liverwort_carpet");
    public static final ResourceKey<PlacedFeature> THICKET_SHAMROCK = of("patch_thicket_shamrock");
    public static final ResourceKey<PlacedFeature> TUNDRA_FOLIAGE = of("patch_tundra_foliage");

    public static void init() {
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, NATURAL_SWEDROOT, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.NATURAL_SWEDROOT),
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(80), VerticalAnchor.belowTop(0))),
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(BlockPos.ZERO), BlockPredicate.matchesBlocks(Blocks.AIR), 12),
                RandomOffsetPlacement.of(ConstantInt.of(0), ConstantInt.of(-1)));
        register(context, PATCH_BLACKCURRANT, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PATCH_BLACKCURRANT),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                RarityFilter.onAverageOnceEvery(8),
                BiomeFilter.biome());
        register(context, PATCH_BROWN_SPORECAP, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PATCH_BROWN_SPORECAP),
                RarityFilter.onAverageOnceEvery(10),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(1));
        register(context, PATCH_BROWN_SPORECAP_COMMON, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PATCH_BROWN_SPORECAP),
                RarityFilter.onAverageOnceEvery(3),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING));
        register(context, BUSH, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.BUSH),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(1));
        register(context, DENSE_BUSH, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.DENSE_BUSH),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(3));
        register(context, FLOWERS, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.FLOWERS),
                NoiseThresholdCountPlacement.of(-0.8D, 15, 4),
                RarityFilter.onAverageOnceEvery(24),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                BiomeFilter.biome());
        register(context, FLOWERS_FOREST, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.FLOWERS_FOREST),
                NoiseThresholdCountPlacement.of(-0.8D, 15, 4),
                RarityFilter.onAverageOnceEvery(32),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                BiomeFilter.biome());
        register(context, FLOWERS_THICKET, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.FLOWERS_THICKET),
                NoiseThresholdCountPlacement.of(-0.8D, 15, 4),
                RarityFilter.onAverageOnceEvery(48),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                BiomeFilter.biome());
        register(context, GRASS, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.GRASS_BUSH),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(6));
        register(context, PATCH_PINK_SPORECAP, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PATCH_PINK_SPORECAP),
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(256))),
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(BlockPos.ZERO), BlockPredicate.matchesBlocks(Blocks.AIR), 12),
                RandomOffsetPlacement.of(ConstantInt.of(0), ConstantInt.of(-1)));
        register(context, PLATEAU_FLOWERING_GRASS, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PLATEAU_FLOWERING_GRASS),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(5));
        register(context, PLATEAU_FOLIAGE, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PLATEAU_FOLIAGE),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(3));
        register(context, PLATEAU_SHAMROCK, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.PLATEAU_SHAMROCK),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                CountPlacement.of(2));
        register(context, SHIELD_FLAX, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.SHIELD_FLAX),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                RarityFilter.onAverageOnceEvery(30));
        register(context, SHIELD_FOLIAGE, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.SHIELD_FOLIAGE),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                CountPlacement.of(3));
        register(context, SHIELD_NETTLES, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.SHIELD_NETTLES),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                CountPlacement.of(10));
        register(context, TALL_GRASS, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.TALL_GRASS_BUSH),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(3));
        register(context, THICKET_LIVERWORT_CARPET, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.THICKET_LIVERWORT_CARPET),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                CountPlacement.of(1));
        register(context, THICKET_SHAMROCK, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.THICKET_SHAMROCK),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                CountPlacement.of(2));
        register(context, TUNDRA_FOLIAGE, configuredFeatures.getOrThrow(ParadiseLostVegetationConfiguredFeatures.TUNDRA_FOLIAGE),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(3));
    }
}
