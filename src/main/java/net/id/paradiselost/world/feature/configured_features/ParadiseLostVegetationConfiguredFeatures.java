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
    // public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> PLATEAU_FLOWERING_GRASS = register("patch_plateau_flowering_grass", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(GRASS_FLOWERING), UniformIntProvider.create(3, 10), ConstantIntProvider.create(5), UniformIntProvider.create(3, 6), 1.5));
    // public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> PLATEAU_SHAMROCK = register("patch_plateau_shamrock", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(MALT_SPRIG), UniformIntProvider.create(2, 6), ConstantIntProvider.create(5), UniformIntProvider.create(4, 7), 1.4));

    // Shield
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_FOLIAGE = of("patch_shield_foliage");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_FLAX = of("patch_shield_flax");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_NETTLES = of("patch_shield_nettles");

    // Thicket
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_LIVERWORT_CARPET = of("patch_thicket_liverwort_carpet");
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_SHAMROCK = of("patch_thicket_shamrock");
    //public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> THICKET_LIVERWORT_CARPET = register("patch_thicket_liverwort_carpet", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(LIVERWORT_CARPET), UniformIntProvider.create(1, 4), ConstantIntProvider.create(5), UniformIntProvider.create(5, 8), 1.3));
    //public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> THICKET_SHAMROCK = register("patch_thicket_shamrock", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(SHAMROCK), UniformIntProvider.create(2, 6), ConstantIntProvider.create(5), UniformIntProvider.create(4, 7), 1.3));

    // Tundra
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUNDRA_FOLIAGE = of("patch_tundra_foliage");

    public static void init() {
    }

}
