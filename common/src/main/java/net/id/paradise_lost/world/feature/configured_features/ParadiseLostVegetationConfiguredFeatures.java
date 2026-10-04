package net.id.paradise_lost.world.feature.configured_features;

import net.id.paradise_lost.world.feature.ParadiseLostFeatures;
import net.id.paradise_lost.world.feature.configs.BoulderFeatureConfig;
import net.id.paradise_lost.world.feature.configs.JaggedOreConfig;
import net.id.paradise_lost.world.feature.configs.LongFeatureConfig;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostMiscPlacedFeatures;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostTreePlacedFeatures;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostVegetationPlacedFeatures;
import net.id.paradise_lost.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.paradise_lost.world.feature.tree.placers.PointedBallFoliagePlacer;
import net.id.paradise_lost.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.paradise_lost.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.OptionalInt;

@SuppressWarnings("unused")
public class ParadiseLostVegetationConfiguredFeatures extends ParadiseLostConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> BUSH = of("patch_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DENSE_BUSH = of("patch_dense_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWERS = of("patch_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWERS_FOREST = of("patch_flowers_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWERS_THICKET = of("patch_flowers_thicket");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS_BUSH = of("patch_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NATURAL_SWEDROOT = of("natural_swedroot");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BLACKCURRANT = of("patch_blackcurrant");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BROWN_SPORECAP = of("patch_brown_sporecap");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PINK_SPORECAP = of("patch_pink_sporecap");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLATEAU_FLOWERING_GRASS = of("patch_plateau_flowering_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLATEAU_FOLIAGE = of("patch_plateau_foliage");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLATEAU_SHAMROCK = of("patch_plateau_shamrock");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_FLAX = of("patch_shield_flax");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_FOLIAGE = of("patch_shield_foliage");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_NETTLES = of("patch_shield_nettles");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_GRASS_BUSH = of("patch_tall_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICKET_LIVERWORT_CARPET = of("patch_thicket_liverwort_carpet");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICKET_SHAMROCK = of("patch_thicket_shamrock");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TUNDRA_FOLIAGE = of("patch_tundra_foliage");

    public static void init() {
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, NATURAL_SWEDROOT, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(new BlockColumnConfiguration.Layer(ConstantInt.of(1), BlockStateProvider.simple(BlockRegistry.DIRT.get().defaultBlockState())), new BlockColumnConfiguration.Layer(ConstantInt.of(1), BlockStateProvider.simple(BlockRegistry.SWEDROOT.get().defaultBlockState()))), Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.AIR), true));
        register(context, PATCH_BLACKCURRANT, Feature.RANDOM_PATCH, new RandomPatchConfiguration(42, 5, 5, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.BLACKCURRANT_BUSH.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, PATCH_BROWN_SPORECAP, Feature.RANDOM_PATCH, new RandomPatchConfiguration(8, 6, 4, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.BROWN_SPORECAP.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, BUSH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.BUSH.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, DENSE_BUSH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(16, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.BUSH.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, FLOWERS, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, List.of(1.0D)), 0.005f, -0.8f, 0.33333334f, BlockRegistry.CLOUDSBLUFF.get().defaultBlockState(), List.of(BlockRegistry.LUMINAR.get().defaultBlockState(), BlockRegistry.DRIGEAN.get().defaultBlockState(), BlockRegistry.ANCIENT_FLOWER.get().defaultBlockState()), List.of(BlockRegistry.ATARAXIA.get().defaultBlockState(), BlockRegistry.ANCIENT_FLOWER.get().defaultBlockState()))), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, FLOWERS_FOREST, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, List.of(1.0D)), 0.005f, -0.8f, 0.5f, BlockRegistry.CLOUDSBLUFF.get().defaultBlockState(), List.of(BlockRegistry.ANCIENT_FLOWER.get().defaultBlockState()), List.of(BlockRegistry.DRIGEAN.get().defaultBlockState(), BlockRegistry.ATARAXIA.get().defaultBlockState()))), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, FLOWERS_THICKET, Feature.FLOWER, new RandomPatchConfiguration(32, 6, 2, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, List.of(1.0D)), 0.005f, -0.8f, 0.33333334f, BlockRegistry.LUMINAR.get().defaultBlockState(), List.of(BlockRegistry.ANCIENT_FLOWER.get().defaultBlockState()), List.of(BlockRegistry.CLOUDSBLUFF.get().defaultBlockState()))), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, GRASS_BUSH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.GRASS.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, PATCH_PINK_SPORECAP, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(new BlockColumnConfiguration.Layer(ConstantInt.of(1), BlockStateProvider.simple(BlockRegistry.PINK_SPORECAP.get().defaultBlockState()))), Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.AIR), true));
        register(context, PLATEAU_FLOWERING_GRASS, Feature.RANDOM_PATCH, new RandomPatchConfiguration(128, 16, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.GRASS_FLOWERING.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, PLATEAU_FOLIAGE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(96, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.GRASS.get().defaultBlockState(), 20).add(BlockRegistry.FERN.get().defaultBlockState(), 15).add(BlockRegistry.BUSH.get().defaultBlockState(), 13).add(BlockRegistry.GRASS_FLOWERING.get().defaultBlockState(), 5).build())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, PLATEAU_SHAMROCK, Feature.RANDOM_PATCH, new RandomPatchConfiguration(128, 16, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.MALT_SPRIG.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, SHIELD_FLAX, Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 12, 5, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.WILD_FLAX.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.wouldSurvive(BlockRegistry.WILD_FLAX.get().defaultBlockState(), BlockPos.ZERO))))));
        register(context, SHIELD_FOLIAGE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(96, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.GRASS.get().defaultBlockState(), 20).add(BlockRegistry.FERN.get().defaultBlockState(), 15).add(BlockRegistry.BUSH.get().defaultBlockState(), 13).add(BlockRegistry.GRASS_FLOWERING.get().defaultBlockState(), 5).build())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, SHIELD_NETTLES, ParadiseLostFeatures.HONEY_NETTLE_FEATURE, NoneFeatureConfiguration.INSTANCE);
        register(context, TALL_GRASS_BUSH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.TALL_GRASS.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, THICKET_LIVERWORT_CARPET, Feature.RANDOM_PATCH, new RandomPatchConfiguration(128, 16, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.LIVERWORT_CARPET.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), BlockRegistry.HIGHLANDS_GRASS.get()))))));
        register(context, THICKET_SHAMROCK, Feature.RANDOM_PATCH, new RandomPatchConfiguration(128, 16, 7, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.SHAMROCK.get().defaultBlockState())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
        register(context, TUNDRA_FOLIAGE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.GRASS.get().defaultBlockState(), 10).add(BlockRegistry.SHORT_GRASS.get().defaultBlockState(), 30).add(BlockRegistry.BUSH.get().defaultBlockState(), 3).build())), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)))));
    }
}
