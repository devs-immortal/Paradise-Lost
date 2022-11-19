package net.id.paradiselost.world.feature.placed_features;

import net.id.paradiselost.world.feature.configured_features.ParadiseLostVegetationConfiguredFeatures;
import net.id.paradiselost.world.feature.placement_modifiers.ChancePlacementModifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER;

public class ParadiseLostVegetationPlacedFeatures extends ParadiseLostPlacedFeatures {
    /*
    Highlands
     */
    // Default
    public static final RegistryEntry<PlacedFeature> BUSH = register("bush", ParadiseLostVegetationConfiguredFeatures.BUSH, SPREAD_32_ABOVE, CountMultilayerPlacementModifier.of(2), ChancePlacementModifier.of(4));
    public static final RegistryEntry<PlacedFeature> DENSE_BUSH = register("dense_bush", ParadiseLostVegetationConfiguredFeatures.DENSE_BUSH, CountMultilayerPlacementModifier.of(3), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> FLOWERS = register("flowers", ParadiseLostVegetationConfiguredFeatures.FLOWERS, CountPlacementModifier.of(3), RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> GRASS = register("grass", ParadiseLostVegetationConfiguredFeatures.GRASS_BUSH, CountPlacementModifier.of(10), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> GRASS_BONEMEAL = register("grass_bonemeal", ParadiseLostVegetationConfiguredFeatures.GRASS_BONEMEAL, PlacedFeatures.isAir());
    public static final RegistryEntry<PlacedFeature> TALL_GRASS = register("tall_grass", ParadiseLostVegetationConfiguredFeatures.TALL_GRASS_BUSH, CountPlacementModifier.of(3), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of());

    public static final RegistryEntry<PlacedFeature> FLUTEGRASS = register("flutegrass", ParadiseLostVegetationConfiguredFeatures.FLUTEGRASS, CountPlacementModifier.of(30), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> FLUTEGRASS_BONEMEAL = register("flutegrass_bonemeal", ParadiseLostVegetationConfiguredFeatures.FLUTEGRASS_BONEMEAL, PlacedFeatures.isAir());

    public static final RegistryEntry<PlacedFeature> PATCH_BLUEBERRY = register("patch_blueberry", ParadiseLostVegetationConfiguredFeatures.PATCH_BLUEBERRY, NOT_IN_SURFACE_WATER_MODIFIER, CountPlacementModifier.of(10), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_BROWN_SPORECAP = register("patch_brown_sporecap", ParadiseLostVegetationConfiguredFeatures.PATCH_BROWN_SPORECAP, CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 2)), ON_SOLID_GROUND);
    public static final RegistryEntry<PlacedFeature> PATCH_PINK_SPORECAP = register("patch_pink_sporecap", ParadiseLostVegetationConfiguredFeatures.PATCH_PINK_SPORECAP, CountPlacementModifier.of(3), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_TOP_RANGE, EnvironmentScanPlacementModifier.of(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.IS_AIR, 12), RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(-1)), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> NATURAL_SWEDROOT = register("natural_swedroot", ParadiseLostVegetationConfiguredFeatures.NATURAL_SWEDROOT, CountPlacementModifier.of(1), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_TOP_RANGE, EnvironmentScanPlacementModifier.of(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.IS_AIR, 12), BiomePlacementModifier.of());

    // Plato
    public static final RegistryEntry<PlacedFeature> PLATEAU_FOLIAGE = register("plateau_foliage", ParadiseLostVegetationConfiguredFeatures.PLATEAU_FOLIAGE, CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 4)));
    public static final RegistryEntry<PlacedFeature> PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", ParadiseLostVegetationConfiguredFeatures.PLATEAU_FLOWERING_GRASS, PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 5)), ON_SOLID_GROUND);
    public static final RegistryEntry<PlacedFeature> PLATEAU_SHAMROCK = register("plateau_shamrock", ParadiseLostVegetationConfiguredFeatures.PLATEAU_SHAMROCK, PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(6), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2)), ON_SOLID_GROUND);
    // Shield
    public static final RegistryEntry<PlacedFeature> SHIELD_FLAX = register("shield_flax", ParadiseLostVegetationConfiguredFeatures.SHIELD_FLAX, SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), ChancePlacementModifier.of(3), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> SHIELD_NETTLES = register("shield_nettles", ParadiseLostVegetationConfiguredFeatures.SHIELD_NETTLES, CountMultilayerPlacementModifier.of(30), CountPlacementModifier.of(UniformIntProvider.create(0, 12)), ON_SOLID_GROUND);
    public static final RegistryEntry<PlacedFeature> SHIELD_FOLIAGE = register("shield_foliage", ParadiseLostVegetationConfiguredFeatures.SHIELD_FOLIAGE, CountMultilayerPlacementModifier.of(5), CountPlacementModifier.of(UniformIntProvider.create(0, 2)), ON_SOLID_GROUND);
    // Tundra
    public static final RegistryEntry<PlacedFeature> TUNDRA_FOLIAGE = register("tundra_foliage", ParadiseLostVegetationConfiguredFeatures.TUNDRA_FOLIAGE, CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 3)));
    // Forest
    public static final RegistryEntry<PlacedFeature> THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", ParadiseLostVegetationConfiguredFeatures.THICKET_LIVERWORT_CARPET, PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(6), CountMultilayerPlacementModifier.of(1), ON_SOLID_GROUND);
    public static final RegistryEntry<PlacedFeature> THICKET_SHAMROCK = register("thicket_shamrock", ParadiseLostVegetationConfiguredFeatures.THICKET_SHAMROCK, PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(8), CountMultilayerPlacementModifier.of(1), ON_SOLID_GROUND);

    // ?
    public static final RegistryEntry<PlacedFeature> RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", ParadiseLostVegetationConfiguredFeatures.RAINBOW_MALT_SPRIGS, PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(2), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1)), ON_SOLID_GROUND);

    public static void init(){}
}
