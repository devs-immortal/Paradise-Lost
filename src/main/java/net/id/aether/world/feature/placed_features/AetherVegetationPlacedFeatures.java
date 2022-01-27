package net.id.aether.world.feature.placed_features;

import net.id.aether.world.feature.decorators.ChancePlacementModifier;
import net.id.aether.world.feature.configured_features.AetherVegetationConfiguredFeatures;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;

import java.util.List;

import static net.id.aether.blocks.AetherBlocks.*;
import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER;

public class AetherVegetationPlacedFeatures extends AetherPlacedFeatures{
    /*
    Highlands
     */
    // Default
    public static final PlacedFeature AETHER_BUSH = register("aether_bush", AetherVegetationConfiguredFeatures.AETHER_BUSH.withPlacement(SPREAD_32_ABOVE, CountMultilayerPlacementModifier.of(2), ChancePlacementModifier.of(4)));
    public static final PlacedFeature AETHER_DENSE_BUSH = register("aether_dense_bush", AetherVegetationConfiguredFeatures.AETHER_DENSE_BUSH.withPlacement(CountMultilayerPlacementModifier.of(3), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_FLOWERS = register("aether_flowers", AetherVegetationConfiguredFeatures.AETHER_FLOWERS.withPlacement(CountPlacementModifier.of(3), RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_GRASS = register("aether_grass", AetherVegetationConfiguredFeatures.AETHER_GRASS_BUSH.withPlacement(CountPlacementModifier.of(10), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_TALL_GRASS = register("aether_tall_grass", AetherVegetationConfiguredFeatures.AETHER_TALL_GRASS_BUSH.withPlacement(CountPlacementModifier.of(3), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));

    public static final PlacedFeature FLUTEGRASS = register("flutegrass", AetherVegetationConfiguredFeatures.FLUTEGRASS.withPlacement(CountPlacementModifier.of(30), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));

    public static final PlacedFeature PATCH_BLUEBERRY = register("patch_blueberry", AetherVegetationConfiguredFeatures.PATCH_BLUEBERRY.withPlacement(NOT_IN_SURFACE_WATER_MODIFIER, CountPlacementModifier.of(10), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), BiomePlacementModifier.of()));
    // Plato
    public static final PlacedFeature PLATEAU_FOLIAGE = register("plateau_foliage", AetherVegetationConfiguredFeatures.PLATEAU_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 4))));
    public static final PlacedFeature PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", AetherVegetationConfiguredFeatures.PLATEAU_FLOWERING_GRASS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 5)), ON_SOLID_GROUND));
    public static final PlacedFeature PLATEAU_SHAMROCK = register("plateau_shamrock", AetherVegetationConfiguredFeatures.PLATEAU_SHAMROCK.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(6), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2)), ON_SOLID_GROUND));
    // Shield
    public static final PlacedFeature SHIELD_FLAX = register("shield_flax", AetherVegetationConfiguredFeatures.SHIELD_FLAX.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(5), CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 4)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(List.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE)))));
    public static final PlacedFeature SHIELD_NETTLES = register("shield_nettles", AetherVegetationConfiguredFeatures.SHIELD_NETTLES.withPlacement(CountMultilayerPlacementModifier.of(20), CountPlacementModifier.of(UniformIntProvider.create(0, 12)), ON_SOLID_GROUND));
    public static final PlacedFeature SHIELD_FOLIAGE = register("shield_foliage", AetherVegetationConfiguredFeatures.SHIELD_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(5), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    // Tundra
    public static final PlacedFeature TUNDRA_FOLIAGE = register("tundra_foliage", AetherVegetationConfiguredFeatures.TUNDRA_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 3))));
    // Forest
    public static final PlacedFeature THICKET_LIVERWORT = register("thicket_liverwort", AetherVegetationConfiguredFeatures.THICKET_LIVERWORT.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2)), ON_SOLID_GROUND));
    public static final PlacedFeature THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", AetherVegetationConfiguredFeatures.THICKET_LIVERWORT_CARPET.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(6), CountMultilayerPlacementModifier.of(1), ON_SOLID_GROUND));
    public static final PlacedFeature THICKET_SHAMROCK = register("thicket_shamrock", AetherVegetationConfiguredFeatures.THICKET_SHAMROCK.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(8), CountMultilayerPlacementModifier.of(1), ON_SOLID_GROUND));

    // ?
    public static final PlacedFeature RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", AetherVegetationConfiguredFeatures.RAINBOW_MALT_SPRIGS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(2), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1)), ON_SOLID_GROUND));

    public static void init(){}
}
