package net.id.paradiselost.world.feature.configuredfeatures;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.world.feature.ParadiseLostFeatures;
import net.id.paradiselost.world.feature.configs.GroundcoverFeatureConfig;
import net.id.paradiselost.world.feature.configs.ProjectedOrganicCoverConfig;
import net.id.paradiselost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;

public class ParadiseLostVegetationConfiguredFeatures extends ParadiseLostConfiguredFeatures {
    /*
    Highlands
     */
    // Default
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PARADISE_LOST_BUSH = register("paradise_lost_bush", Feature.RANDOM_PATCH, Configs.PARADISE_LOST_BUSH_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PARADISE_LOST_DENSE_BUSH = register("paradise_lost_dense_bush", Feature.RANDOM_PATCH, Configs.PARADISE_LOST_DENSE_BUSH_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PARADISE_LOST_GRASS_BUSH = register("paradise_lost_grass", Feature.RANDOM_PATCH, Configs.PARADISE_LOST_GRASS_BUSH_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> PARADISE_LOST_GRASS_BONEMEAL = register("paradise_lost_grass_bonemeal", Feature.SIMPLE_BLOCK, Configs.singleBlockConfig(GRASS));
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PARADISE_LOST_TALL_GRASS_BUSH = register("paradise_lost_tall_grass", Feature.RANDOM_PATCH, Configs.PARADISE_LOST_TALL_GRASS_BUSH_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> FLUTEGRASS = register("flutegrass", Feature.RANDOM_PATCH, Configs.FLUTEGRASS_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> FLUTEGRASS_BONEMEAL = register("flutegrass_bonemeal", Feature.SIMPLE_BLOCK, Configs.singleBlockConfig(ParadiseLostBlocks.FLUTEGRASS));
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PARADISE_LOST_FLOWERS = register("paradise_lost_flowers", Feature.FLOWER, Configs.PARADISE_LOST_FLOWER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PARADISE_LOST_BLUEBERRY = register("patch_blueberry", Feature.RANDOM_PATCH, Configs.BLUEBERRY_PATCH_CONFIG);
    // Plato
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PLATEAU_FOLIAGE = register("plateau_foliage", Feature.RANDOM_PATCH, Configs.PLATEAU_FOLIAGE_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> PLATEAU_FLOWERING_GRASS = register("plateau_flowering_grass", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(GRASS_FLOWERING), UniformIntProvider.create(3, 10), ConstantIntProvider.create(5), UniformIntProvider.create(3, 6), 1.5));
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> PLATEAU_SHAMROCK = register("plateau_shamrock", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(MALT_SPRIG), UniformIntProvider.create(2, 6), ConstantIntProvider.create(5), UniformIntProvider.create(4, 7), 1.4));
    // Shield
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> SHIELD_FOLIAGE = register("shield_foliage", Feature.RANDOM_PATCH, Configs.SHIELD_FOLIAGE_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> SHIELD_FLAX = register("shield_flax", Feature.RANDOM_PATCH, Configs.SHIELD_FLAX_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> SHIELD_NETTLES = register("shield_nettles", ParadiseLostFeatures.HONEY_NETTLE_FEATURE, new DefaultFeatureConfig());
    // Thicket
    public static final RegistryEntry<ConfiguredFeature<GroundcoverFeatureConfig, ?>> THICKET_LIVERWORT = register("thicket_liverwort", ParadiseLostFeatures.GROUNDCOVER_FEATURE, new GroundcoverFeatureConfig(BlockStateProvider.of(LIVERWORT), UniformIntProvider.create(0, 2), UniformIntProvider.create(0, 1)));
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> THICKET_LIVERWORT_CARPET = register("thicket_liverwort_carpet", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(LIVERWORT_CARPET), UniformIntProvider.create(1, 4), ConstantIntProvider.create(5), UniformIntProvider.create(5, 8), 1.3));
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> THICKET_SHAMROCK = register("thicket_shamrock", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(SHAMROCK), UniformIntProvider.create(2, 6), ConstantIntProvider.create(5), UniformIntProvider.create(4, 7), 1.3));
    // Tundra
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> TUNDRA_FOLIAGE = register("tundra_foliage", Feature.RANDOM_PATCH, Configs.TUNDRA_FOLIAGE_CONFIG);
    // ?
    public static final RegistryEntry<ConfiguredFeature<ProjectedOrganicCoverConfig, ?>> RAINBOW_MALT_SPRIGS = register("rainbow_malt_sprigs", ParadiseLostFeatures.ORGANIC_GROUNDCOVER_FEATURE, new ProjectedOrganicCoverConfig(BlockStateProvider.of(MALT_SPRIG), UniformIntProvider.create(3, 13), ConstantIntProvider.create(5), UniformIntProvider.create(3, 4), 1.4));


    private static class Configs extends ParadiseLostConfiguredFeatures.Configs{
        /*
        Highlands
         */
        // Default
        private static final RandomPatchFeatureConfig PARADISE_LOST_BUSH_CONFIG = blockPatch(128, 16, 7, ParadiseLostBlocks.BUSH);
        private static final RandomPatchFeatureConfig PARADISE_LOST_DENSE_BUSH_CONFIG = blockPatch(16, 7, 3, ParadiseLostBlocks.BUSH);
        private static final RandomPatchFeatureConfig PARADISE_LOST_GRASS_BUSH_CONFIG = blockPatch(32, 7, 3, GRASS);
        private static final RandomPatchFeatureConfig PARADISE_LOST_TALL_GRASS_BUSH_CONFIG = blockPatch(32, 7, 3, TALL_GRASS);
        private static final RandomPatchFeatureConfig FLUTEGRASS_CONFIG = blockPatch(32, 7, 3, ParadiseLostBlocks.FLUTEGRASS);

        private static final RandomPatchFeatureConfig PARADISE_LOST_FLOWER_CONFIG = blockPatch(64, 7, 3, new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(ATARAXIA.getDefaultState(), 20)
                        .add(CLOUDSBLUFF.getDefaultState(), 20)
                        .add(DRIGEAN.getDefaultState(), 3)
                        .add(LUMINAR.getDefaultState(), 8)
                        .add(ANCIENT_FLOWER.getDefaultState(), 1)
        ));

        private static final RandomPatchFeatureConfig BLUEBERRY_PATCH_CONFIG = blockPatch(42, 5, 5, BLUEBERRY_BUSH.getDefaultState().with(Properties.AGE_3, 3));
        // Plato
        private static final RandomPatchFeatureConfig PLATEAU_FOLIAGE_CONFIG = blockPatch(96, 7, 3, new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(ParadiseLostBlocks.GRASS.getDefaultState(), 20)
                        .add(FERN.getDefaultState(), 15)
                        .add(ParadiseLostBlocks.BUSH.getDefaultState(), 13)
                        .add(GRASS_FLOWERING.getDefaultState(), 5)
        ));
        // Shield
        private static final RandomPatchFeatureConfig SHIELD_FOLIAGE_CONFIG = blockPatch(64, 7, 3, new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(ParadiseLostBlocks.GRASS.getDefaultState(), 20)
                        .add(FERN.getDefaultState(), 15)
                        .add(ParadiseLostBlocks.BUSH.getDefaultState(), 13)
                        .add(GRASS_FLOWERING.getDefaultState(), 5)
        ));

        private static final RandomPatchFeatureConfig SHIELD_FLAX_CONFIG = new RandomPatchFeatureConfig(96, 12, 5,
                singleBlock(WILD_FLAX, BlockFilterPlacementModifier.of(BlockPredicate.matchingBlocks(Vec3i.ZERO.down(), List.of(HOLYSTONE, COBBLED_HOLYSTONE, MOSSY_HOLYSTONE))), ParadiseLostPlacedFeatures.ON_SOLID_GROUND)
        );
        // Tundra
        private static final RandomPatchFeatureConfig TUNDRA_FOLIAGE_CONFIG = blockPatch(32, 7, 3, new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(SHORT_GRASS.getDefaultState(), 30)
                        .add(ParadiseLostBlocks.GRASS.getDefaultState(), 10)
                        .add(ParadiseLostBlocks.BUSH.getDefaultState(), 3)
        ));

    }

    public static void init(){}
}
