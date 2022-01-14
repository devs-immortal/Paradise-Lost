package net.id.aether.world.feature.features.placed;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.mixin.world.SimpleBlockStateProviderAccessor;
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
import static net.id.aether.blocks.AetherBlocks.MOSSY_HOLYSTONE;
import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER;

public class AetherVegetationPlacedFeatures extends AetherPlacedFeatures{
    public static final PlacedFeature AETHER_BUSH = register("aether_bush", AetherVegetationConfiguredFeatures.AETHER_BUSH.withPlacement(SPREAD_32_ABOVE, CountMultilayerPlacementModifier.of(5), new ChancePlacementModifier(ConstantIntProvider.create(4))));

    public static final PlacedFeature RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", AetherVegetationConfiguredFeatures.RAINBOW_MALT_SPRIGS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))/*.spreadHorizontally()*/));

    public static final PlacedFeature PLATEAU_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AetherBlocks.AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5)))).withPlacement(NOT_IN_SURFACE_WATER_MODIFIER);
    public static final PlacedFeature PLATEAU_FOLIAGE = register("plateau_foliage", AetherVegetationConfiguredFeatures.PLATEAU_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 4)))/*.spreadHorizontally()*/);
    public static final PlacedFeature PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", AetherVegetationConfiguredFeatures.PLATEAU_FLOWERING_GRASS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 5)), NOT_IN_SURFACE_WATER_MODIFIER)/*.spreadHorizontally()*/);
    public static final PlacedFeature PLATEAU_SHAMROCK = register("plateau_shamrock", AetherVegetationConfiguredFeatures.PLATEAU_SHAMROCK.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(6)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2))/*.spreadHorizontally()*/));

    public static final PlacedFeature SHIELD_FLAX_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(WILD_FLAX))).withPlacement();
    public static final PlacedFeature SHIELD_FLAX = register("shield_flax", AetherVegetationConfiguredFeatures.SHIELD_FLAX.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(5)), CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 4)), BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(List.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE))))/*.spreadHorizontally()*/);
    public static final PlacedFeature SHIELD_NETTLES = register("shield_nettles", AetherVegetationConfiguredFeatures.SHIELD_NETTLES.withPlacement(CountMultilayerPlacementModifier.of(20), CountPlacementModifier.of(UniformIntProvider.create(0, 12))/*.spreadHorizontally()*/));

    // These "single block" features might also need to be registered in configured features, but also maybe not.
    public static final PlacedFeature SHIELD_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AetherBlocks.AETHER_GRASS.getDefaultState(), 20).add(AETHER_FERN.getDefaultState(), 15).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 13).add(AETHER_GRASS_FLOWERING.getDefaultState(), 5)))).withPlacement();
    public static final PlacedFeature SHIELD_FOLIAGE = register("shield_foliage", AetherVegetationConfiguredFeatures.SHIELD_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(5), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));

    public static final PlacedFeature TUNDRA_FOLIAGE_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(AETHER_SHORT_GRASS.getDefaultState(), 30).add(AetherBlocks.AETHER_GRASS.getDefaultState(), 10).add(AetherBlocks.AETHER_BUSH.getDefaultState(), 3)))).withPlacement();
    public static final PlacedFeature TUNDRA_FOLIAGE = register("tundra_foliage", AetherVegetationConfiguredFeatures.TUNDRA_FOLIAGE.withPlacement(CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);

    public static final PlacedFeature AETHER_DENSE_BUSH = register("aether_dense_bush", AetherVegetationConfiguredFeatures.AETHER_DENSE_BUSH.withPlacement(CountMultilayerPlacementModifier.of(6), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_FLOWERS = register("aether_flowers", AetherVegetationConfiguredFeatures.AETHER_FLOWERS.withPlacement(CountPlacementModifier.of(5), RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_GRASS = register("aether_grass", AetherVegetationConfiguredFeatures.AETHER_GRASS_BUSH.withPlacement(CountPlacementModifier.of(45), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));
    public static final PlacedFeature AETHER_TALL_GRASS = register("aether_tall_grass", AetherVegetationConfiguredFeatures.AETHER_TALL_GRASS_BUSH.withPlacement(CountPlacementModifier.of(20), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));

    public static final PlacedFeature FLUTEGRASS = register("flutegrass", AetherVegetationConfiguredFeatures.FLUTEGRASS.withPlacement(CountPlacementModifier.of(30), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING), BiomePlacementModifier.of()));

    public static final PlacedFeature PATCH_BLUEBERRY = register("patch_blueberry", AetherVegetationConfiguredFeatures.PATCH_BLUEBERRY.withPlacement(NOT_IN_SURFACE_WATER_MODIFIER, CountPlacementModifier.of(10), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.belowTop(0)), BiomePlacementModifier.of()));

    public static final PlacedFeature THICKET_LIVERWORT = register("thicket_liverwort", AetherVegetationConfiguredFeatures.THICKET_LIVERWORT.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2))/*.spreadHorizontally()*/));
    public static final PlacedFeature THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", AetherVegetationConfiguredFeatures.THICKET_LIVERWORT_CARPET.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(6)), CountMultilayerPlacementModifier.of(1)));
    public static final PlacedFeature THICKET_SHAMROCK = register("thicket_shamrock", AetherVegetationConfiguredFeatures.THICKET_SHAMROCK.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, new ChancePlacementModifier(ConstantIntProvider.create(8)), CountMultilayerPlacementModifier.of(1)));

    public static class Configs {
        public static final PlacedFeature AETHER_BUSH_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AetherBlocks.AETHER_BUSH.getDefaultState()))).withInAirFilter();

        public static final PlacedFeature AETHER_GRASS_PLANT_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AetherBlocks.AETHER_GRASS.getDefaultState()))).withInAirFilter();
        public static final PlacedFeature AETHER_TALL_GRASS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AetherBlocks.AETHER_TALL_GRASS.getDefaultState()))).withInAirFilter();

        public static final PlacedFeature FLUTEGRASS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(SimpleBlockStateProviderAccessor.callInit(AetherBlocks.FLUTEGRASS.getDefaultState()))).withInAirFilter();

    }

    public static void init(){}
}
