package net.id.aether.world.feature.configured_features;

import net.id.aether.world.feature.AetherFeatures;
import net.id.aether.world.feature.configs.BoulderFeatureConfig;
import net.id.aether.world.feature.configs.GroundcoverFeatureConfig;
import net.id.aether.world.feature.placed_features.AetherMiscPlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

import static net.id.aether.blocks.AetherBlocks.*;

public class AetherMiscConfiguredFeatures extends AetherConfiguredFeatures{
    /*
    Highlands
     */
    // Default
    public static final ConfiguredFeature<?, ?> QUICKSOIL = register("quicksoil", AetherFeatures.QUICKSOIL.configure(AetherConfiguredFeatures.Configs.QUICKSOIL_CONFIG));

    public static final ConfiguredFeature<?, ?> WATER_SPRING = register("water_spring", Feature.SPRING_FEATURE.configure(AetherConfiguredFeatures.Configs.WATER_SPRING_CONFIG));

    public static final ConfiguredFeature<?, ?> GENERIC_BOULDER = register("generic_boulder", AetherConfiguredFeatures.Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(MOSSY_HOLYSTONE.getDefaultState(), 1).add(COBBLED_HOLYSTONE.getDefaultState(), 3).build()), ConstantIntProvider.create(4), UniformIntProvider.create(3, 6))));
    public static final ConfiguredFeature<?, ?> PLAINS_BOULDER = register("plains_boulder", AetherConfiguredFeatures.Configs.BOULDER.configure(new BoulderFeatureConfig(BlockStateProvider.of(COBBLED_HOLYSTONE), ConstantIntProvider.create(3), UniformIntProvider.create(3, 5)))); // TODO REPLACED WITH JSON //
    public static final ConfiguredFeature<?, ?> THICKET_BOULDER = register("thicket_boulder", AetherConfiguredFeatures.Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(MOSSY_HOLYSTONE.getDefaultState(), 4).add(COBBLED_HOLYSTONE.getDefaultState(), 1).build()), ConstantIntProvider.create(6), UniformIntProvider.create(2, 5))));
    public static final ConfiguredFeature<?, ?> GOLDEN_BOULDER = register("golden_boulder", AetherConfiguredFeatures.Configs.BOULDER.configure(new BoulderFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(GOLDEN_MOSSY_HOLYSTONE.getDefaultState(), 4).add(COBBLED_HOLYSTONE.getDefaultState(), 1).build()), ConstantIntProvider.create(4), UniformIntProvider.create(3, 5))));

    public static final ConfiguredFeature<OreFeatureConfig, ?> ORE_AMBROSIUM = register("ore_ambrosium", Feature.ORE.configure(new OreFeatureConfig(Configs.AMBROSIUM_ORES, 14)));
    public static final ConfiguredFeature<OreFeatureConfig, ?> ORE_GRAVITITE = register("ore_gravitite", Feature.ORE.configure(new OreFeatureConfig(Configs.GRAVITITE_ORES, 6)));
    public static final ConfiguredFeature<OreFeatureConfig, ?> ORE_ZANITE = register("ore_zanite", Feature.ORE.configure(new OreFeatureConfig(Configs.ZANITE_ORES, 9)));
    // Plato
    // Shield
    public static final ConfiguredFeature<?, ?> SHIELD_ROCKS = register("shield_rocks", Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(96, 9, 3, () -> AetherMiscPlacedFeatures.SHIELD_ROCKS_SINGLE_BLOCK)/*.cannotProject()*/));

    public static final ConfiguredFeature<?, ?> SHIELD_PONDS = register("shield_pond", AetherConfiguredFeatures.Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.WATER.getDefaultState(), COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(Properties.WATERLOGGED, true), UniformIntProvider.create(2, 7), UniformIntProvider.create(1, 2))));

    public static final ConfiguredFeature<?, ?> SHIELD_STONE = register("shield_stone", AetherConfiguredFeatures.Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HOLYSTONE.getDefaultState(), 7).add(COBBLED_HOLYSTONE.getDefaultState(), 5).add(MOSSY_HOLYSTONE.getDefaultState(), 2).build()), UniformIntProvider.create(1, 4), UniformIntProvider.create(0, 0))));
    public static final ConfiguredFeature<?, ?> SHIELD_PODZOL = register("shield_podzol", AetherConfiguredFeatures.Configs.GROUNDCOVER_FEATURE.configure(new GroundcoverFeatureConfig(BlockStateProvider.of(AETHER_FROZEN_GRASS), UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 0))));
    // Tundra
    public static final ConfiguredFeature<?, ?> TUNDRA_SPIRES = register("tundra_spires", AetherConfiguredFeatures.Configs.ICESTONE_SPIRE_FEATURE.configure(FeatureConfig.DEFAULT));

    public static final ConfiguredFeature<?, ?> TUNDRA_PONDS = register("tundra_pond", AetherConfiguredFeatures.Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1))));
    public static final ConfiguredFeature<?, ?> TUNDRA_SNOW = register("tundra_snow", AetherConfiguredFeatures.Configs.AETHER_DELTA_FEATURE.configure(new DeltaFeatureConfig(Blocks.POWDER_SNOW.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), UniformIntProvider.create(3, 8), UniformIntProvider.create(0, 1))));

    public static final ConfiguredFeature<?, ?> FREEZE_AETHER_TOP_LAYER = register("freeze_aether_top_layer", AetherConfiguredFeatures.Configs.FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE.configure(FeatureConfig.DEFAULT));

    public static class Configs extends AetherConfiguredFeatures.Configs{
        //FIXME Make this a tag
        public static final RuleTest AETHER_STONE_REPLACEABLES = new BlockMatchRuleTest(HOLYSTONE);
        public static final List<OreFeatureConfig.Target> AMBROSIUM_ORES = List.of(OreFeatureConfig.createTarget(AETHER_STONE_REPLACEABLES, AMBROSIUM_ORE.getDefaultState()));
        public static final List<OreFeatureConfig.Target> GRAVITITE_ORES = List.of(OreFeatureConfig.createTarget(AETHER_STONE_REPLACEABLES, GRAVITITE_ORE.getDefaultState()));
        public static final List<OreFeatureConfig.Target> ZANITE_ORES = List.of(OreFeatureConfig.createTarget(AETHER_STONE_REPLACEABLES, GRAVITITE_ORE.getDefaultState()));
    }
    public static void init(){}
}
