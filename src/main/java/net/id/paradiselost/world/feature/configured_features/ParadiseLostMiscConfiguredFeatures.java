package net.id.paradiselost.world.feature.configured_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.*;

public class ParadiseLostMiscConfiguredFeatures extends ParadiseLostConfiguredFeatures {
    /*
    Highlands
     */
    // Default
    public static final RegistryKey<ConfiguredFeature<?, ?>> WATER_SPRING = of("spring_water");

    public static final RegistryKey<ConfiguredFeature<?, ?>> GENERIC_BOULDER = of("generic_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PLAINS_BOULDER = of("plains_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_BOULDER = of("thicket_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GOLDEN_BOULDER = of("golden_boulder");

    public static final RegistryKey<ConfiguredFeature<?, ?>> HELIOLITH_BLOB = of("heliolith_blob");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LEVITA_BLOB = of("levita_blob");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_CHERINE = of("ore_cherine");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_OLVITE = of("ore_olvite");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_LEVITA = of("ore_levita");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SURTRUM_METEORITE = of("surtrum_meteorite");

    // Shield
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_ROCKS = of("shield_rocks");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_PONDS = of("shield_pond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUNDRA_SPIRES = of("tundra_spires");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUNDRA_PONDS = of("tundra_pond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUNDRA_SNOW = of("tundra_snow");

    public static void init() {
    }

}
