package net.id.paradiselost.world.feature.configured_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.*;

public class ParadiseLostVegetationConfiguredFeatures extends ParadiseLostConfiguredFeatures {

    // Patches
    public static final RegistryKey<ConfiguredFeature<?, ?>> BUSH = of("patch_bush");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DENSE_BUSH = of("patch_dense_bush");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRASS_BUSH = of("patch_grass");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TALL_GRASS_BUSH = of("patch_tall_grass");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWERS = of("patch_flowers");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_BLACKCURRANT = of("patch_blackcurrant");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_BROWN_SPORECAP = of("patch_brown_sporecap");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_PINK_SPORECAP = of("patch_pink_sporecap");
    public static final RegistryKey<ConfiguredFeature<?, ?>> NATURAL_SWEDROOT = of("natural_swedroot");

    // Plateau
    public static final RegistryKey<ConfiguredFeature<?, ?>> PLATEAU_FOLIAGE = of("patch_plateau_foliage");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PLATEAU_FLOWERING_GRASS = of("patch_plateau_flowering_grass");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PLATEAU_SHAMROCK = of("patch_plateau_shamrock");

    // Shield
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_FOLIAGE = of("patch_shield_foliage");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_FLAX = of("patch_shield_flax");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_NETTLES = of("patch_shield_nettles");

    // Thicket
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_LIVERWORT_CARPET = of("patch_thicket_liverwort_carpet");
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_SHAMROCK = of("patch_thicket_shamrock");

    // Tundra
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUNDRA_FOLIAGE = of("patch_tundra_foliage");

    public static void init() {
    }

}
