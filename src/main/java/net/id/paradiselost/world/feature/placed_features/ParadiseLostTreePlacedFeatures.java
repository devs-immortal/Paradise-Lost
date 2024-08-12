package net.id.paradiselost.world.feature.placed_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.*;

public class ParadiseLostTreePlacedFeatures extends ParadiseLostPlacedFeatures {

    // Tree Assortments
    public static final RegistryKey<PlacedFeature> SCATTERED_TREES = of("trees_scattered");
    public static final RegistryKey<PlacedFeature> SHIELD_TREES = of("trees_shield");
    public static final RegistryKey<PlacedFeature> DENSE_SHIELD_TREES = of("trees_dense_shield");
    public static final RegistryKey<PlacedFeature> PLATEAU_TREES = of("trees_plateau");
    public static final RegistryKey<PlacedFeature> MIXED_TREES = of("trees_mixed");
    public static final RegistryKey<PlacedFeature> SPARSE_TREES = of("trees_sparse");
    public static final RegistryKey<PlacedFeature> THICKET_TREES = of("trees_thicket");
    public static final RegistryKey<PlacedFeature> THICKET_MOTHER_AUREL_TREES = of("trees_thicket_mother_aurel");
    public static final RegistryKey<PlacedFeature> RAINBOW_FOREST_TREES = of("trees_rainbow_forest");

    // Fallen leaves
    public static final RegistryKey<PlacedFeature> FALLEN_LEAVES = of("fallen_leaves");
    public static final RegistryKey<PlacedFeature> FALLEN_ROSE_LEAVES = of("fallen_rose_leaves");
    public static final RegistryKey<PlacedFeature> FALLEN_LAVENDER_LEAVES = of("fallen_lavender_leaves");

    // Logs & Stumps
    public static final RegistryKey<PlacedFeature> THICKET_FALLEN_LOG = of("thicket_fallen_log");
    public static final RegistryKey<PlacedFeature> MOTTLED_FALLEN_LOG = of("mottled_fallen_log");
    public static final RegistryKey<PlacedFeature> MOTTLED_HOLLOW_FALLEN_LOG = of("mottled_hollow_fallen_log");

    public static final RegistryKey<PlacedFeature> SHIELD_STUMPS = of("shield_stumps");
    public static final RegistryKey<PlacedFeature> SHIELD_HOLLOW_STUMPS = of("shield_hollow_stumps");

    public static void init() {}

}
