package net.id.paradiselost.world.feature.configured_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.*;

public class ParadiseLostTreeConfiguredFeatures extends ParadiseLostConfiguredFeatures {

    // Normal
    public static final RegistryKey<ConfiguredFeature<?, ?>> AUREL_TREE = of("tree_aurel");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOTTLED_AUREL = of("tree_mottled_aurel");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DWARF_MOTTLED_AUREL = of("tree_dwarf_mottled_aurel");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOTHER_AUREL_TREE = of("tree_mother_aurel");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORANGE_TREE = of("tree_orange");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WILD_ORANGE_TREE = of("tree_wild_orange");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FANCY_AUREL_TREE = of("tree_fancy_aurel");
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_AUREL_TREE = of("tree_thicket_aurel");

    // Wisteria
    public static final RegistryKey<ConfiguredFeature<?, ?>> ROSE_WISTERIA_TREE = of("tree_rose_wisteria");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LAVENDER_WISTERIA_TREE = of("tree_lavender_wisteria");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROST_WISTERIA_TREE = of("tree_frost_wisteria");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FANCY_ROSE_WISTERIA_TREE = of("tree_fancy_rose_wisteria");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FANCY_LAVENDER_WISTERIA_TREE = of("tree_fancy_lavender_wisteria");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FANCY_FROST_WISTERIA_TREE = of("tree_fancy_frost_wisteria");
    public static final RegistryKey<ConfiguredFeature<?, ?>> AUREL_SHRUB = of("aurel_shrub");

    // Fallen leaves
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_LEAVES = of("fallen_leaves");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_ROSE_LEAVES = of("fallen_rose_leaves");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_LAVENDER_LEAVES = of("fallen_lavender_leaves");
    //public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ROSE_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(ROSE_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2));
    //public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(LAVENDER_WISTERIA_LEAF_PILE.getDefaultState(), 10).add(LAVENDER_WISTERIA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 2).build()), UniformIntProvider.create(4, 10), ConstantIntProvider.create(7), UniformIntProvider.create(3, 6), 1.2));

    // Logs
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_FALLEN_LOG = of("thicket_fallen_log");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOTTLED_FALLEN_LOG = of("mottled_fallen_log");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOTTLED_HOLLOW_FALLEN_LOG = of("mottled_hollow_fallen_log");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_STUMPS = of("shield_stumps");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_HOLLOW_STUMPS = of("shield_hollow_stumps");

    // Assortments
    public static final RegistryKey<ConfiguredFeature<?, ?>> SCATTERED_TREES = of("trees_scattered");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SHIELD_TREES = of("trees_shield");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DENSE_SHIELD_TREES = of("trees_dense_shield");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PLATEAU_TREES = of("trees_plateau");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MIXED_TREES = of("trees_mixed");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPARSE_TREES = of("trees_sparse");
    public static final RegistryKey<ConfiguredFeature<?, ?>> THICKET_TREES = of("trees_thicket");
    public static final RegistryKey<ConfiguredFeature<?, ?>> RAINBOW_FOREST_TREES = of("trees_rainbow_forest");

    public static void init() {}

}
