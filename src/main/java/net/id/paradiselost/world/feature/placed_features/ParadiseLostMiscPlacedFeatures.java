package net.id.paradiselost.world.feature.placed_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ParadiseLostMiscPlacedFeatures extends ParadiseLostPlacedFeatures {

    // Default
    public static final RegistryKey<PlacedFeature> GENERIC_BOULDER = of("generic_boulder");
    public static final RegistryKey<PlacedFeature> PLAINS_BOULDER = of("plains_boulder");
    public static final RegistryKey<PlacedFeature> THICKET_BOULDER = of("thicket_boulder");
    public static final RegistryKey<PlacedFeature> GOLDEN_BOULDER = of("golden_boulder");

    public static final RegistryKey<PlacedFeature> WATER_SPRING = of("spring_water");

    public static final RegistryKey<PlacedFeature> HELIOLITH_BLOB = of("heliolith_blob");
    public static final RegistryKey<PlacedFeature> LEVITA_BLOB = of("levita_blob");

    public static final RegistryKey<PlacedFeature> ORE_CHERINE = of("ore_cherine");
    public static final RegistryKey<PlacedFeature> ORE_OLVITE = of("ore_olvite");
    public static final RegistryKey<PlacedFeature> ORE_LEVITA = of("ore_levita");
    public static final RegistryKey<PlacedFeature> SURTRUM_METEORITE = of("surtrum_meteorite");

    // Shield
    public static final RegistryKey<PlacedFeature> SHIELD_ROCKS = of("shield_rocks");
    public static final RegistryKey<PlacedFeature> SHIELD_PONDS = of("shield_pond");

    // Tundra
    public static final RegistryKey<PlacedFeature> TUNDRA_SPIRES = of("tundra_spires");
    public static final RegistryKey<PlacedFeature> TUNDRA_PONDS = of("tundra_pond");
    public static final RegistryKey<PlacedFeature> TUNDRA_SNOW = of("tundra_snow");

    public static void init() {
    }

}
