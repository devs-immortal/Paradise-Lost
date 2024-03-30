package net.id.paradiselost.world.feature.configured_features;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.world.feature.ParadiseLostFeatures;
import net.id.paradiselost.world.feature.configs.BoulderFeatureConfig;
import net.id.paradiselost.world.feature.configs.GroundcoverFeatureConfig;
import net.id.paradiselost.world.feature.configs.JaggedOreConfig;
import net.id.paradiselost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;

public class ParadiseLostMiscConfiguredFeatures extends ParadiseLostConfiguredFeatures {
    /*
    Highlands
     */
    // Default
    public static final RegistryEntry<ConfiguredFeature<SpringFeatureConfig, ?>> WATER_SPRING = register("water_spring", Feature.SPRING_FEATURE, Configs.WATER_SPRING_CONFIG);

    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> GENERIC_BOULDER = register("generic_boulder", ParadiseLostFeatures.BOULDER, Configs.GENERIC_BOULDER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> PLAINS_BOULDER = register("plains_boulder", ParadiseLostFeatures.BOULDER, Configs.PLAINS_BOULDER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> THICKET_BOULDER = register("thicket_boulder", ParadiseLostFeatures.BOULDER, Configs.THICKET_BOULDER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> GOLDEN_BOULDER = register("golden_boulder", ParadiseLostFeatures.BOULDER, Configs.GOLDEN_BOULDER_CONFIG);

    public static final RegistryEntry<ConfiguredFeature<JaggedOreConfig, ?>> HELIOLITH_BLOB = register("heliolith_blob", ParadiseLostFeatures.JAGGED_ORE, Configs.HELIOLITH_BLOB_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<JaggedOreConfig, ?>> LEVITA_BLOB = register("levita_blob", ParadiseLostFeatures.JAGGED_ORE, Configs.LEVITA_BLOB_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_CHERINE = register("ore_cherine", Feature.ORE, Configs.ore(CHERINE_ORE, 14));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_OLVITE = register("ore_olvite", Feature.ORE, Configs.ore(OLVITE_ORE, 9));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_LEVITA = register("ore_levita", Feature.ORE, Configs.ore(LEVITA_ORE, 3));
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> SURTRUM_METEORITE = register("surtrum_meteorite", ParadiseLostFeatures.SURTRUM_METEORITE_FEATURE, new DefaultFeatureConfig());
    // Plato
    // Shield
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> SHIELD_ROCKS = register("shield_rocks", Feature.RANDOM_PATCH, Configs.SHIELD_ROCKS_CONFIG);

    public static final RegistryEntry<ConfiguredFeature<DeltaFeatureConfig, ?>> SHIELD_PONDS = register("shield_pond", ParadiseLostFeatures.DELTA_FEATURE, new DeltaFeatureConfig(Blocks.WATER.getDefaultState(), COBBLED_FLOESTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(Properties.WATERLOGGED, true), UniformIntProvider.create(2, 7), UniformIntProvider.create(1, 2)));

    public static final RegistryEntry<ConfiguredFeature<GroundcoverFeatureConfig, ?>> SHIELD_PODZOL = register("shield_podzol", ParadiseLostFeatures.GROUNDCOVER_FEATURE, new GroundcoverFeatureConfig(BlockStateProvider.of(FROZEN_GRASS), UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 0)));
    // Tundra
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> TUNDRA_SPIRES = register("tundra_spires", ParadiseLostFeatures.VITROULITE_SPIRE_FEATURE, FeatureConfig.DEFAULT);

    public static final RegistryEntry<ConfiguredFeature<DeltaFeatureConfig, ?>> TUNDRA_PONDS = register("tundra_pond", ParadiseLostFeatures.DELTA_FEATURE, new DeltaFeatureConfig(Blocks.ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1)));
    public static final RegistryEntry<ConfiguredFeature<DeltaFeatureConfig, ?>> TUNDRA_SNOW = register("tundra_snow", ParadiseLostFeatures.DELTA_FEATURE, new DeltaFeatureConfig(Blocks.POWDER_SNOW.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), UniformIntProvider.create(3, 8), UniformIntProvider.create(0, 1)));

    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> FREEZE_TOP_LAYER = register("freeze_top_layer", ParadiseLostFeatures.FREEZE_TOP_LAYER_FEATURE_FEATURE, FeatureConfig.DEFAULT);

    // TODO 1.7 uncomment public static final RegistryEntry<ConfiguredFeature<DynamicConfiguration, ?>> LAKE = register("lake", ParadiseLostFeatures.LAKE, new DynamicConfiguration(Blocks.WATER.getDefaultState(), Optional.of("normal")));

    public static void init() {
    }
    
    private static class Configs extends ParadiseLostConfiguredFeatures.Configs {
        private static final SpringFeatureConfig WATER_SPRING_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 4, 1, RegistryEntryList.of(Block::getRegistryEntry, FLOESTONE));

        private static final RandomPatchFeatureConfig SHIELD_ROCKS_CONFIG = new RandomPatchFeatureConfig(48, 9, 3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(COBBLED_FLOESTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), 10)
                        .add(COBBLED_FLOESTONE.getDefaultState(), 4)
                        .build())
        ), BlockFilterPlacementModifier.of(ParadiseLostPlacedFeatures.IN_OR_ON_GROUND)));

        private static BoulderFeatureConfig boulder(BlockStateProvider provider, int tries, IntProvider size) {
            return new BoulderFeatureConfig(BlockStateProvider.of(COBBLED_FLOESTONE), ConstantIntProvider.create(tries), size);
        }
        private static BoulderFeatureConfig boulder(Block block, int tries, IntProvider size) {
            return boulder(BlockStateProvider.of(block), tries, size);
        }

        private static final JaggedOreConfig HELIOLITH_BLOB_CONFIG =
                new JaggedOreConfig(BlockStateProvider.of(HELIOLITH), UniformIntProvider.create(4, 7), UniformIntProvider.create(7, 11), UniformIntProvider.create(5, 11), UniformIntProvider.create(1, 4));
        private static final JaggedOreConfig LEVITA_BLOB_CONFIG =
                new JaggedOreConfig(new WeightedBlockStateProvider(
                        DataPool.<BlockState>builder()
                                .add(LEVITA.getDefaultState(), 200)
                                .add(LEVITA_ORE.getDefaultState(), 1)
                ), UniformIntProvider.create(3, 5), UniformIntProvider.create(6, 10), UniformIntProvider.create(4, 10), UniformIntProvider.create(3, 6));

        private static final BoulderFeatureConfig GENERIC_BOULDER_CONFIG = boulder(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(MOSSY_FLOESTONE.getDefaultState(), 1)
                        .add(COBBLED_FLOESTONE.getDefaultState(), 3)
        ), 4, UniformIntProvider.create(3, 6));

        private static final BoulderFeatureConfig PLAINS_BOULDER_CONFIG = boulder(COBBLED_FLOESTONE, 3, UniformIntProvider.create(3, 5));
        private static final BoulderFeatureConfig THICKET_BOULDER_CONFIG = boulder(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(MOSSY_FLOESTONE.getDefaultState(), 4)
                        .add(COBBLED_FLOESTONE.getDefaultState(), 1)
        ), 6, UniformIntProvider.create(2, 5));

        private static final BoulderFeatureConfig GOLDEN_BOULDER_CONFIG = boulder(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(GOLDEN_MOSSY_FLOESTONE.getDefaultState(), 4)
                        .add(COBBLED_FLOESTONE.getDefaultState(), 1)
        ), 4, UniformIntProvider.create(3, 5));

        private static OreFeatureConfig ore(Block ore, int size) {
            return new OreFeatureConfig(List.of(OreFeatureConfig.createTarget(new BlockMatchRuleTest(FLOESTONE), ore.getDefaultState())), size);
        }
    }
}
