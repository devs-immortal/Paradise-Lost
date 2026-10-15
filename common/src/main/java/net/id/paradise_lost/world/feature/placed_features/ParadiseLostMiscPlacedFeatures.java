package net.id.paradise_lost.world.feature.placed_features;

import net.id.paradise_lost.world.feature.configured_features.ParadiseLostMiscConfiguredFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;


@SuppressWarnings("unused")
public class ParadiseLostMiscPlacedFeatures extends ParadiseLostPlacedFeatures {

    public static final ResourceKey<PlacedFeature> CALCITE_BLOB = of("calcite_blob");
    public static final ResourceKey<PlacedFeature> GENERIC_BOULDER = of("generic_boulder");
    public static final ResourceKey<PlacedFeature> GOLDEN_BOULDER = of("golden_boulder");
    public static final ResourceKey<PlacedFeature> HELIOLITH_BLOB = of("heliolith_blob");
    public static final ResourceKey<PlacedFeature> LEVITA_BLOB = of("levita_blob");
    public static final ResourceKey<PlacedFeature> ORE_CHERINE = of("ore_cherine");
    public static final ResourceKey<PlacedFeature> ORE_FLOESTONE_REDSTONE = of("ore_floestone_redstone");
    public static final ResourceKey<PlacedFeature> ORE_LEVITA = of("ore_levita");
    public static final ResourceKey<PlacedFeature> ORE_OLVITE = of("ore_olvite");
    public static final ResourceKey<PlacedFeature> PLAINS_BOULDER = of("plains_boulder");
    public static final ResourceKey<PlacedFeature> SHIELD_PONDS = of("shield_pond");
    public static final ResourceKey<PlacedFeature> SHIELD_ROCKS = of("shield_rocks");
    public static final ResourceKey<PlacedFeature> SURTRUM_METEORITE = of("surtrum_meteorite");
    public static final ResourceKey<PlacedFeature> THICKET_BOULDER = of("thicket_boulder");
    public static final ResourceKey<PlacedFeature> TUNDRA_PONDS = of("tundra_pond");
    public static final ResourceKey<PlacedFeature> TUNDRA_SNOW = of("tundra_snow");
    public static final ResourceKey<PlacedFeature> TUNDRA_SPIRES = of("tundra_spires");
    public static final ResourceKey<PlacedFeature> WATER_SPRING = of("spring_water");

    public static void init() {
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, CALCITE_BLOB, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.CALCITE_BLOB),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(110))),
                BiomeFilter.biome());
        register(context, GENERIC_BOULDER, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.GENERIC_BOULDER),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                RarityFilter.onAverageOnceEvery(8));
        register(context, GOLDEN_BOULDER, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.GOLDEN_BOULDER),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                RarityFilter.onAverageOnceEvery(15));
        register(context, HELIOLITH_BLOB, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.HELIOLITH_BLOB),
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(320))),
                BiomeFilter.biome());
        register(context, LEVITA_BLOB, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.LEVITA_BLOB),
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(320))),
                BiomeFilter.biome());
        register(context, ORE_CHERINE, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.ORE_CHERINE),
                CountPlacement.of(26),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(TrapezoidHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(200))),
                BiomeFilter.biome());
        register(context, ORE_FLOESTONE_REDSTONE, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.ORE_FLOESTONE_REDSTONE),
                CountPlacement.of(8),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(TrapezoidHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(100))),
                BiomeFilter.biome());
        register(context, ORE_LEVITA, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.ORE_LEVITA),
                CountPlacement.of(8),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(TrapezoidHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(384))),
                BiomeFilter.biome());
        register(context, ORE_OLVITE, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.ORE_OLVITE),
                CountPlacement.of(18),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(TrapezoidHeight.of(VerticalAnchor.absolute(64), VerticalAnchor.absolute(200))),
                BiomeFilter.biome());
        register(context, PLAINS_BOULDER, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.PLAINS_BOULDER),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                RarityFilter.onAverageOnceEvery(4));
        register(context, SHIELD_PONDS, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.SHIELD_PONDS),
                CountOnEveryLayerPlacement.of(19),
                BiomeFilter.biome());
        register(context, SHIELD_ROCKS, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.SHIELD_ROCKS),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                CountOnEveryLayerPlacement.of(1));
        register(context, WATER_SPRING, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.WATER_SPRING),
                CountPlacement.of(12),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(256))),
                BiomeFilter.biome());
        register(context, SURTRUM_METEORITE, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.SURTRUM_METEORITE),
                RarityFilter.onAverageOnceEvery(255),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(65), VerticalAnchor.absolute(85))),
                BiomeFilter.biome());
        register(context, THICKET_BOULDER, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.THICKET_BOULDER),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                RarityFilter.onAverageOnceEvery(1));
        register(context, TUNDRA_PONDS, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.TUNDRA_PONDS),
                CountOnEveryLayerPlacement.of(19),
                BiomeFilter.biome());
        register(context, TUNDRA_SNOW, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.TUNDRA_SNOW),
                CountOnEveryLayerPlacement.of(4),
                BiomeFilter.biome());
        register(context, TUNDRA_SPIRES, configuredFeatures.getOrThrow(ParadiseLostMiscConfiguredFeatures.TUNDRA_SPIRES),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                CountPlacement.of(5));
    }
}
