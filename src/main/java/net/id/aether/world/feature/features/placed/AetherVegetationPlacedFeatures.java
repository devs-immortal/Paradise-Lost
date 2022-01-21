package net.id.aether.world.feature.features.placed;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.world.feature.decorators.ChancePlacementModifier;
import net.id.aether.world.feature.features.configured.AetherVegetationConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

import static net.id.aether.blocks.AetherBlocks.*;
import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER;

public class AetherVegetationPlacedFeatures extends AetherPlacedFeatures{
    /*
    Highlands
     */
    // Default
    public static final PlacedFeature AETHER_BUSH = register("aether_bush", AetherVegetationConfiguredFeatures.AETHER_BUSH.withPlacement(NoiseThresholdCountPlacementModifier.of(-0.8D, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_DENSE_BUSH = register("aether_dense_bush", AetherVegetationConfiguredFeatures.AETHER_DENSE_BUSH.withPlacement(VegetationPlacedFeatures.modifiers(6)));
    public static final PlacedFeature AETHER_FLOWERS = register("aether_flowers", AetherVegetationConfiguredFeatures.AETHER_FLOWERS.withPlacement(NoiseThresholdCountPlacementModifier.of(-0.8D, 15, 4), RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_GRASS = register("aether_grass", AetherVegetationConfiguredFeatures.AETHER_GRASS_BUSH.withPlacement(VegetationPlacedFeatures.modifiers(36)));
    public static final PlacedFeature AETHER_TALL_GRASS = register("aether_tall_grass", AetherVegetationConfiguredFeatures.AETHER_TALL_GRASS_BUSH.withPlacement(VegetationPlacedFeatures.modifiers(16)));

    public static final PlacedFeature FLUTEGRASS = register("flutegrass", AetherVegetationConfiguredFeatures.FLUTEGRASS.withPlacement(VegetationPlacedFeatures.modifiers(30)));

    public static final PlacedFeature PATCH_BLUEBERRY = register("patch_blueberry", AetherVegetationConfiguredFeatures.PATCH_BLUEBERRY.withPlacement(RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of()));
    // Plato
    public static final PlacedFeature PLATEAU_FOLIAGE = register("plateau_foliage", AetherVegetationConfiguredFeatures.PLATEAU_FOLIAGE.withPlacement(VegetationPlacedFeatures.modifiers(3))/*.spreadHorizontally()*/);
    public static final PlacedFeature PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", AetherVegetationConfiguredFeatures.PLATEAU_FLOWERING_GRASS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 5)), NOT_IN_SURFACE_WATER_MODIFIER)/*.spreadHorizontally()*/);
    public static final PlacedFeature PLATEAU_SHAMROCK = register("plateau_shamrock", AetherVegetationConfiguredFeatures.PLATEAU_SHAMROCK.withPlacement(VegetationPlacedFeatures.modifiers(2)));
    // Shield
    public static final PlacedFeature SHIELD_FLAX = register("shield_flax", AetherVegetationConfiguredFeatures.SHIELD_FLAX.withPlacement(RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of(), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(List.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE)))));
    public static final PlacedFeature SHIELD_NETTLES = register("shield_nettles", AetherVegetationConfiguredFeatures.SHIELD_NETTLES.withPlacement(VegetationPlacedFeatures.modifiers(10)));
    public static final PlacedFeature SHIELD_FOLIAGE = register("shield_foliage", AetherVegetationConfiguredFeatures.SHIELD_FOLIAGE.withPlacement(VegetationPlacedFeatures.modifiers(2)));
    // Tundra
    public static final PlacedFeature TUNDRA_FOLIAGE = register("tundra_foliage", AetherVegetationConfiguredFeatures.TUNDRA_FOLIAGE.withPlacement(VegetationPlacedFeatures.modifiers(3)));
    // Forest
    public static final PlacedFeature THICKET_LIVERWORT = register("thicket_liverwort", AetherVegetationConfiguredFeatures.THICKET_LIVERWORT.withPlacement(VegetationPlacedFeatures.modifiers(2)));
    public static final PlacedFeature THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", AetherVegetationConfiguredFeatures.THICKET_LIVERWORT_CARPET.withPlacement(VegetationPlacedFeatures.modifiers(2)));
    public static final PlacedFeature THICKET_SHAMROCK = register("thicket_shamrock", AetherVegetationConfiguredFeatures.THICKET_SHAMROCK.withPlacement(VegetationPlacedFeatures.modifiers(3)));

    public static final PlacedFeature RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", AetherVegetationConfiguredFeatures.RAINBOW_MALT_SPRIGS.withPlacement(VegetationPlacedFeatures.modifiers(2)));

    public static class Configs {
        /*
        Highlands
         */
        // Default
        public static final PlacedFeature AETHER_BUSH_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(AetherBlocks.AETHER_BUSH))).withInAirFilter();
        public static final PlacedFeature AETHER_GRASS_PLANT_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(AetherBlocks.AETHER_GRASS))).withInAirFilter();
        public static final PlacedFeature AETHER_TALL_GRASS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(AetherBlocks.AETHER_TALL_GRASS))).withInAirFilter();
        public static final PlacedFeature FLUTEGRASS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(AetherBlocks.FLUTEGRASS))).withInAirFilter();
        // Plato
        public static final PlacedFeature PLATEAU_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AetherBlocks.AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5)))).withInAirFilter();
        // Shield
        public static final PlacedFeature SHIELD_FLAX_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(WILD_FLAX))).withInAirFilter();
        public static final PlacedFeature SHIELD_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AetherBlocks.AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5)))).withInAirFilter();
        // Tundra
        public static final PlacedFeature TUNDRA_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_SHORT_GRASS.getDefaultState(), 30).add(AetherBlocks.AETHER_GRASS.getDefaultState(), 10).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 3)))).withInAirFilter();

    }

    public static void init(){}
}
