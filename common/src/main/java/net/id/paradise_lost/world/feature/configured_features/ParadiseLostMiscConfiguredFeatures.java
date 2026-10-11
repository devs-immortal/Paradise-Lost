package net.id.paradise_lost.world.feature.configured_features;

import net.id.paradise_lost.world.feature.ParadiseLostFeatures;
import net.id.paradise_lost.world.feature.configs.BoulderFeatureConfig;
import net.id.paradise_lost.world.feature.configs.JaggedOreConfig;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;

import java.util.List;

@SuppressWarnings("unused")
public class ParadiseLostMiscConfiguredFeatures extends ParadiseLostConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CALCITE_BLOB = of("calcite_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GENERIC_BOULDER = of("generic_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GOLDEN_BOULDER = of("golden_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HELIOLITH_BLOB = of("heliolith_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEVITA_BLOB = of("levita_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CHERINE = of("ore_cherine");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FLOESTONE_REDSTONE = of("ore_floestone_redstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LEVITA = of("ore_levita");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_OLVITE = of("ore_olvite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLAINS_BOULDER = of("plains_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_PONDS = of("shield_pond");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIELD_ROCKS = of("shield_rocks");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SURTRUM_METEORITE = of("surtrum_meteorite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICKET_BOULDER = of("thicket_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TUNDRA_PONDS = of("tundra_pond");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TUNDRA_SNOW = of("tundra_snow");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TUNDRA_SPIRES = of("tundra_spires");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WATER_SPRING = of("spring_water");

    public static void init() {
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, CALCITE_BLOB, ParadiseLostFeatures.JAGGED_ORE, new JaggedOreConfig(BlockStateProvider.simple(Blocks.CALCITE.defaultBlockState()), UniformInt.of(2, 5), UniformInt.of(9, 12), UniformInt.of(8, 11), UniformInt.of(2, 6)));
        register(context, GENERIC_BOULDER, ParadiseLostFeatures.BOULDER, new BoulderFeatureConfig(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState(), 3).add(BlockRegistry.MOSSY_FLOESTONE.get().defaultBlockState(), 1).build()), ConstantInt.of(4), UniformInt.of(3, 6)));
        register(context, GOLDEN_BOULDER, ParadiseLostFeatures.BOULDER, new BoulderFeatureConfig(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState(), 1).add(BlockRegistry.GOLDEN_MOSSY_FLOESTONE.get().defaultBlockState(), 3).build()), ConstantInt.of(4), UniformInt.of(3, 5)));
        register(context, HELIOLITH_BLOB, ParadiseLostFeatures.JAGGED_ORE, new JaggedOreConfig(BlockStateProvider.simple(BlockRegistry.HELIOLITH.get().defaultBlockState()), UniformInt.of(4, 7), UniformInt.of(7, 11), UniformInt.of(5, 11), UniformInt.of(1, 4)));
        register(context, LEVITA_BLOB, ParadiseLostFeatures.JAGGED_ORE, new JaggedOreConfig(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.LEVITA.get().defaultBlockState(), 150).add(BlockRegistry.LEVITA_ORE.get().defaultBlockState(), 1).build()), UniformInt.of(3, 5), UniformInt.of(6, 10), UniformInt.of(4, 10), UniformInt.of(3, 6)));
        register(context, ORE_CHERINE, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(BlockRegistry.FLOESTONE.get()), BlockRegistry.CHERINE_ORE.get().defaultBlockState())), 14, 0.0f));
        register(context, ORE_FLOESTONE_REDSTONE, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(BlockRegistry.FLOESTONE.get()), BlockRegistry.FLOESTONE_REDSTONE_ORE.get().defaultBlockState())), 11, 0.0f));
        register(context, ORE_LEVITA, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(BlockRegistry.FLOESTONE.get()), BlockRegistry.LEVITA_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, ORE_OLVITE, Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(BlockRegistry.FLOESTONE.get()), BlockRegistry.OLVITE_ORE.get().defaultBlockState())), 9, 0.0f));
        register(context, PLAINS_BOULDER, ParadiseLostFeatures.BOULDER, new BoulderFeatureConfig(BlockStateProvider.simple(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState()), ConstantInt.of(3), UniformInt.of(3, 5)));
        register(context, SHIELD_PONDS, Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(Blocks.WATER.defaultBlockState(), BlockRegistry.MOSSY_FLOESTONE.get().defaultBlockState(), UniformInt.of(2, 7), UniformInt.of(1, 2)));
        register(context, SHIELD_ROCKS, Feature.RANDOM_PATCH, new RandomPatchConfiguration(48, 9, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.COBBLED_FLOESTONE_SLAB.get().defaultBlockState(), 10).add(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState(), 4).build())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), BlockRegistry.FLOESTONE.get()))))));
        register(context, WATER_SPRING, Feature.SPRING, new SpringConfiguration(Fluids.WATER.defaultFluidState(), true, 4, 1, HolderSet.direct(Block::builtInRegistryHolder, BlockRegistry.FLOESTONE.get())));
        register(context, SURTRUM_METEORITE, ParadiseLostFeatures.SURTRUM_METEORITE_FEATURE, NoneFeatureConfiguration.INSTANCE);
        register(context, THICKET_BOULDER, ParadiseLostFeatures.BOULDER, new BoulderFeatureConfig(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState(), 1).add(BlockRegistry.MOSSY_FLOESTONE.get().defaultBlockState(), 4).build()), ConstantInt.of(6), UniformInt.of(2, 5)));
        register(context, TUNDRA_PONDS, Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(Blocks.ICE.defaultBlockState(), Blocks.PACKED_ICE.defaultBlockState(), UniformInt.of(4, 9), UniformInt.of(0, 1)));
        register(context, TUNDRA_SNOW, Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(Blocks.POWDER_SNOW.defaultBlockState(), Blocks.SNOW_BLOCK.defaultBlockState(), UniformInt.of(3, 8), UniformInt.of(0, 1)));
        register(context, TUNDRA_SPIRES, ParadiseLostFeatures.VITROULITE_SPIRE_FEATURE, NoneFeatureConfiguration.INSTANCE);
    }
}
