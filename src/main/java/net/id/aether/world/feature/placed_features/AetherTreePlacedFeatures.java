package net.id.aether.world.feature.placed_features;

import net.id.aether.world.feature.placement_modifiers.ChancePlacementModifier;
import net.id.aether.world.feature.configured_features.AetherTreeConfiguredFeatures;
import net.id.aether.world.feature.placement_modifiers.CrystalTreeIslandPlacementModifier;
import net.minecraft.block.Block;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;

import java.util.List;

import static net.id.aether.blocks.AetherBlocks.*;

public class AetherTreePlacedFeatures extends AetherPlacedFeatures{
    public static final List<Block> AETHER_GROUD = List.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE, AETHER_DIRT, COARSE_AETHER_DIRT, PERMAFROST);

    /*
    Highlands
     */
    // Default
    public static final PlacedFeature SKYROOT_TREE = register("skyroot_tree", placed(AetherTreeConfiguredFeatures.SKYROOT_TREE, SKYROOT_SAPLING));
    public static final PlacedFeature GOLDEN_OAK_TREE = register("golden_oak_tree", placed(AetherTreeConfiguredFeatures.GOLDEN_OAK_TREE, GOLDEN_OAK_SAPLING));
    public static final PlacedFeature CRYSTAL_TREE = register("crystal_tree", placed(AetherTreeConfiguredFeatures.CRYSTAL_TREE, CRYSTAL_SAPLING));
    public static final PlacedFeature ORANGE_TREE = register("orange_tree", placed(AetherTreeConfiguredFeatures.ORANGE_TREE, ORANGE_SAPLING));
    // Wisteria
    public static final PlacedFeature ROSE_WISTERIA_TREE = register("rose_wisteria_tree", placed(AetherTreeConfiguredFeatures.ROSE_WISTERIA_TREE, ROSE_WISTERIA_SAPLING));
    public static final PlacedFeature LAVENDER_WISTERIA_TREE = register("lavender_wisteria_tree", placed(AetherTreeConfiguredFeatures.LAVENDER_WISTERIA_TREE, LAVENDER_WISTERIA_SAPLING));
    public static final PlacedFeature FROST_WISTERIA_TREE = register("frost_wisteria_tree", placed(AetherTreeConfiguredFeatures.FROST_WISTERIA_TREE, FROST_WISTERIA_SAPLING));
    public static final PlacedFeature BOREAL_WISTERIA_TREE = register("boreal_wisteria_tree", placed(AetherTreeConfiguredFeatures.BOREAL_WISTERIA_TREE, BOREAL_WISTERIA_SAPLING));
    // Fancy
    public static final PlacedFeature FANCY_ROSE_WISTERIA_TREE = register("fancy_rose_wisteria_tree", placed(AetherTreeConfiguredFeatures.FANCY_ROSE_WISTERIA_TREE, ROSE_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_LAVENDER_WISTERIA_TREE = register("fancy_lavender_wisteria_tree", placed(AetherTreeConfiguredFeatures.FANCY_LAVENDER_WISTERIA_TREE, LAVENDER_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_FROST_WISTERIA_TREE = register("fancy_frost_wisteria_tree", placed(AetherTreeConfiguredFeatures.FANCY_FROST_WISTERIA_TREE, FROST_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_BOREAL_WISTERIA_TREE = register("fancy_boreal_wisteria_tree", placed(AetherTreeConfiguredFeatures.FANCY_BOREAL_WISTERIA_TREE, BOREAL_WISTERIA_SAPLING));
    public static final PlacedFeature FANCY_SKYROOT_TREE = register("fancy_skyroot_tree", placed(AetherTreeConfiguredFeatures.FANCY_SKYROOT_TREE, SKYROOT_SAPLING));
    // Tree Assortments
    public static final PlacedFeature SCATTERED_TREES = register("scattered_trees", AetherTreeConfiguredFeatures.SCATTERED_TREES.withPlacement(CountMultilayerPlacementModifier.of(10), CountPlacementModifier.of(UniformIntProvider.create(0, 7))));
    public static final PlacedFeature SHIELD_TREES = register("shield_trees", AetherTreeConfiguredFeatures.SHIELD_TREES.withPlacement(CountMultilayerPlacementModifier.of(6), CountPlacementModifier.of(UniformIntProvider.create(0, 5))));
    public static final PlacedFeature DENSE_SHIELD_TREES = register("dense_shield_trees", AetherTreeConfiguredFeatures.DENSE_SHIELD_TREES.withPlacement(CountMultilayerPlacementModifier.of(16), ChancePlacementModifier.of(10)));
    public static final PlacedFeature PLATEAU_TREES = register("plateau_trees", AetherTreeConfiguredFeatures.PLATEAU_TREES.withPlacement(ChancePlacementModifier.of(3), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))));
    public static final PlacedFeature MIXED_TREES = register("mixed_trees", AetherTreeConfiguredFeatures.MIXED_TREES.withPlacement(CountMultilayerPlacementModifier.of(1), ChancePlacementModifier.of(10)));
    public static final PlacedFeature SPARSE_TREES = register("sparse_trees", AetherTreeConfiguredFeatures.SPARSE_TREES.withPlacement(CountMultilayerPlacementModifier.of(14), ChancePlacementModifier.of(50), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    public static final PlacedFeature THICKET_TREES = register("thicket_trees", AetherTreeConfiguredFeatures.THICKET_TREES.withPlacement(CountMultilayerPlacementModifier.of(20), CountPlacementModifier.of(UniformIntProvider.create(0, 5))));
    public static final PlacedFeature RAINBOW_FOREST_TREES = register("wisteria_woods_trees", AetherTreeConfiguredFeatures.RAINBOW_FOREST_TREES.withPlacement(CountMultilayerPlacementModifier.of(17), CountPlacementModifier.of(UniformIntProvider.create(0, 4))));
    // Fallen leaves
    public static final PlacedFeature FALLEN_LEAVES = register("fallen_leaves", AetherTreeConfiguredFeatures.FALLEN_LEAVES.withPlacement(SPREAD_32_ABOVE, PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, CountPlacementModifier.of(3), ChancePlacementModifier.of(5), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(AETHER_GROUD))));
    public static final PlacedFeature ALT_FALLEN_LEAVES = register("alt_fallen_leaves", AetherTreeConfiguredFeatures.ALT_FALLEN_LEAVES.withPlacement(SPREAD_32_ABOVE, PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, CountPlacementModifier.of(3), ChancePlacementModifier.of(5), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(AETHER_GROUD))));

    public static final PlacedFeature FALLEN_ROSE_LEAVES = register("fallen_rose_leaves", AetherTreeConfiguredFeatures.FALLEN_ROSE_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(2), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 3))));
    public static final PlacedFeature FALLEN_LAVENDER_LEAVES = register("fallen_lavender_leaves", AetherTreeConfiguredFeatures.FALLEN_LAVENDER_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, ChancePlacementModifier.of(2), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 3))));
    // Logs & Stumps
    public static final PlacedFeature THICKET_FALLEN_LOG = register("thicket_fallen_log", AetherTreeConfiguredFeatures.THICKET_FALLEN_LOG.withPlacement(ChancePlacementModifier.of(3), CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));

    public static final PlacedFeature MOTTLED_FALLEN_LOG = register("mottled_fallen_log", AetherTreeConfiguredFeatures.MOTTLED_FALLEN_LOG.withPlacement(ChancePlacementModifier.of(3), CountMultilayerPlacementModifier.of(1)));
    public static final PlacedFeature MOTTLED_HOLLOW_FALLEN_LOG = register("mottled_hollow_fallen_log", AetherTreeConfiguredFeatures.MOTTLED_HOLLOW_FALLEN_LOG.withPlacement(ChancePlacementModifier.of(3), CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature SHIELD_STUMPS = register("shield_stumps", AetherTreeConfiguredFeatures.SHIELD_STUMPS.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))));
    public static final PlacedFeature SHIELD_HOLLOW_STUMPS = register("shield_hollow_stumps", AetherTreeConfiguredFeatures.SHIELD_HOLLOW_STUMPS.withPlacement(CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature SHIELD_FALLEN_LEAVES = register("shield_fallen_leaves", AetherTreeConfiguredFeatures.SHIELD_FALLEN_LEAVES.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(2), ChancePlacementModifier.of(4), CountPlacementModifier.of(UniformIntProvider.create(0, 3)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(AETHER_GROUD))));
    // Crystal Tree Island
    public static final PlacedFeature CRYSTAL_TREE_ISLAND = register("crystal_tree_island", AetherTreeConfiguredFeatures.CRYSTAL_TREE_ISLAND.withPlacement(CrystalTreeIslandPlacementModifier.of()));

    private static PlacedFeature placed(ConfiguredFeature<?, ?> cfg, Block sapling){
        return cfg.withWouldSurviveFilter(sapling);
    }

    public static void init(){}
}
