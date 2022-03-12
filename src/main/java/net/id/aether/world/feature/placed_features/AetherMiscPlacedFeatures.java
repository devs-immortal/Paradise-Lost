package net.id.aether.world.feature.placed_features;

import net.id.aether.world.feature.configured_features.AetherMiscConfiguredFeatures;
import net.id.aether.world.feature.placement_modifiers.ChancePlacementModifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

public class AetherMiscPlacedFeatures extends AetherPlacedFeatures{
    /*
    Highlands
     */
    // Default
    public static final RegistryEntry<PlacedFeature> GENERIC_BOULDER = register("generic_boulder", AetherMiscConfiguredFeatures.GENERIC_BOULDER, ChancePlacementModifier.of(15), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1)));
    public static final RegistryEntry<PlacedFeature> PLAINS_BOULDER = register("plains_boulder", AetherMiscConfiguredFeatures.PLAINS_BOULDER, ChancePlacementModifier.of(8), CountMultilayerPlacementModifier.of(1));
    public static final RegistryEntry<PlacedFeature> THICKET_BOULDER = register("thicket_boulder", AetherMiscConfiguredFeatures.THICKET_BOULDER, ChancePlacementModifier.of(2), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2)));
    public static final RegistryEntry<PlacedFeature> GOLDEN_BOULDER = register("golden_boulder", AetherMiscConfiguredFeatures.GOLDEN_BOULDER, ChancePlacementModifier.of(30), CountMultilayerPlacementModifier.of(1));

    public static final RegistryEntry<PlacedFeature> QUICKSOIL = register("quicksoil", AetherMiscConfiguredFeatures.QUICKSOIL);
    public static final RegistryEntry<PlacedFeature> WATER_SPRING = register("water_spring", AetherMiscConfiguredFeatures.WATER_SPRING);

    public static final RegistryEntry<PlacedFeature> ORE_AMBROSIUM = register("ore_ambrosium", AetherMiscConfiguredFeatures.ORE_AMBROSIUM, CountPlacementModifier.of(20), SquarePlacementModifier.of(), HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(127)), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> ORE_GRAVITITE = register("ore_gravitite", AetherMiscConfiguredFeatures.ORE_GRAVITITE, CountPlacementModifier.of(3), SquarePlacementModifier.of(), HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(32), YOffset.aboveBottom(79)), BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> ORE_ZANITE = register("ore_zanite", AetherMiscConfiguredFeatures.ORE_ZANITE, CountPlacementModifier.of(16), SquarePlacementModifier.of(), HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(128)), BiomePlacementModifier.of());
    // Plato
    // Shield
    public static final RegistryEntry<PlacedFeature> SHIELD_ROCKS = register("shield_rocks", AetherMiscConfiguredFeatures.SHIELD_ROCKS, PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2)));

    public static final RegistryEntry<PlacedFeature> SHIELD_PONDS = register("shield_pond", AetherMiscConfiguredFeatures.SHIELD_PONDS, CountMultilayerPlacementModifier.of(19));

    public static final RegistryEntry<PlacedFeature> SHIELD_STONE = register("shield_stone", AetherMiscConfiguredFeatures.SHIELD_STONE, CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 2)));
    public static final RegistryEntry<PlacedFeature> SHIELD_PODZOL = register("shield_podzol", AetherMiscConfiguredFeatures.SHIELD_PODZOL, CountMultilayerPlacementModifier.of(2), ChancePlacementModifier.of(15), CountPlacementModifier.of(UniformIntProvider.create(0, 2)));
    // Tundra
    public static final RegistryEntry<PlacedFeature> TUNDRA_SPIRES = register("tundra_spires", AetherMiscConfiguredFeatures.TUNDRA_SPIRES, ChancePlacementModifier.of(14), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 10)));

    public static final RegistryEntry<PlacedFeature> TUNDRA_PONDS = register("tundra_pond", AetherMiscConfiguredFeatures.TUNDRA_PONDS, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1)));
    public static final RegistryEntry<PlacedFeature> TUNDRA_SNOW = register("tundra_snow", AetherMiscConfiguredFeatures.TUNDRA_SNOW, CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2)));

    public static void init(){}
}
