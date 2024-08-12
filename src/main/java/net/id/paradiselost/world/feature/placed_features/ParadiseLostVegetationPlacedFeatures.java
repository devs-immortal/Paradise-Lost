package net.id.paradiselost.world.feature.placed_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.*;

public class ParadiseLostVegetationPlacedFeatures extends ParadiseLostPlacedFeatures {

    // Default
    public static final RegistryKey<PlacedFeature> BUSH = of("patch_bush");
    public static final RegistryKey<PlacedFeature> DENSE_BUSH = of("patch_dense_bush");
    public static final RegistryKey<PlacedFeature> FLOWERS = of("patch_flowers");
    public static final RegistryKey<PlacedFeature> GRASS = of("patch_grass");
    public static final RegistryKey<PlacedFeature> TALL_GRASS = of("patch_tall_grass");

    public static final RegistryKey<PlacedFeature> PATCH_BLACKCURRANT = of("patch_blackcurrant");
    public static final RegistryKey<PlacedFeature> PATCH_BROWN_SPORECAP = of("patch_brown_sporecap");
    public static final RegistryKey<PlacedFeature> PATCH_BROWN_SPORECAP_COMMON = of("patch_brown_sporecap_common");
    public static final RegistryKey<PlacedFeature> PATCH_PINK_SPORECAP = of("patch_pink_sporecap");
    public static final RegistryKey<PlacedFeature> NATURAL_SWEDROOT = of("natural_swedroot");

    // Plato
    public static final RegistryKey<PlacedFeature> PLATEAU_FOLIAGE = of("patch_plateau_foliage");
    public static final RegistryKey<PlacedFeature> PLATEAU_FLOWERING_GRASS = of("patch_plateau_flowering_grass");
    public static final RegistryKey<PlacedFeature> PLATEAU_SHAMROCK = of("patch_plateau_shamrock");

    // Shield
    public static final RegistryKey<PlacedFeature> SHIELD_FLAX = of("patch_shield_flax");
    public static final RegistryKey<PlacedFeature> SHIELD_NETTLES = of("patch_shield_nettles");
    public static final RegistryKey<PlacedFeature> SHIELD_FOLIAGE = of("patch_shield_foliage");

    // Tundra
    public static final RegistryKey<PlacedFeature> TUNDRA_FOLIAGE = of("patch_tundra_foliage");

    // Forest
    public static final RegistryKey<PlacedFeature> THICKET_LIVERWORT_CARPET = of("patch_thicket_liverwort_carpet");
    public static final RegistryKey<PlacedFeature> THICKET_SHAMROCK = of("patch_thicket_shamrock");

    public static void init() {}

}
