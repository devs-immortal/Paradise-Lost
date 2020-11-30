package com.aether.world.feature;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.world.feature.config.AercloudConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.Random;

public class AetherConfiguredFeatures {

    public static ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK, SKYROOT;
    public static ConfiguredFeature<?, ?> SCATTERED_TREES;
    public static ConfiguredFeature<?, ?> COLD_AERCLOUD, BLUE_AERCLOUD, GOLDEN_AERCLOUD;
    public static ConfiguredFeature<?, ?> AETHER_GRASS, AETHER_TALL_GRASS;
    public static ConfiguredFeature<?, ?> AETHER_LAKE, AETHER_SPRINGS;
    public static ConfiguredFeature<?, ?> QUICKSOIL;

    public static void registerFeatures() {
        AETHER_LAKE = register("aether_lake", AetherFeatures.DEFAULT_LAKE.configure(Configs.AETHER_LAKES_CONFIG).decorate(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(4))));
        AETHER_SPRINGS = register("aether_springs", Feature.SPRING_FEATURE.configure(Configs.AETHER_SPRINGS_CONFIG).decorate(Decorator.RANGE_VERY_BIASED.configure(new RangeDecoratorConfig(8, 16, 256))).spreadHorizontally()).repeat(40);
        AETHER_GRASS = register("aether_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(4));
        AETHER_TALL_GRASS = register("aether_tall_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_TALL_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(4));
        SKYROOT = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG));
        GOLDEN_OAK = register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG));
        SCATTERED_TREES = register("scattered_trees", Feature.RANDOM_SELECTOR.configure(Configs.SCATTERED_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(5, 0.1F, 1))));
        COLD_AERCLOUD = register("cold_aercloud", AetherFeatures.DEFAULT_AERCLOUD.configure(Configs.COLD_AERCLOUD_CONFIG).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(15))));
        BLUE_AERCLOUD = register("blue_aercloud", AetherFeatures.DEFAULT_AERCLOUD.configure(Configs.BLUE_AERCLOUD_CONFIG).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(75))));
        GOLDEN_AERCLOUD = register("golden_aercloud", AetherFeatures.DEFAULT_AERCLOUD.configure(Configs.GOLDEN_AERCLOUD_CONFIG).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(100))));
        QUICKSOIL = register("quicksoil", AetherFeatures.DEFAULT_QUICKSOIL.configure(new DefaultFeatureConfig())).decorate(Decorator.COUNT.configure(new CountConfig(5)));
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Aether.locate(id), configuredFeature);
    }

    public static class Configs {
        public static final RandomPatchFeatureConfig AETHER_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build();
        //public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(AetherBlocks.PURPLE_FLOWER.getDefaultState(), 2).addState(AetherBlocks.WHITE_FLOWER.getDefaultState(), 1), new SimpleBlockPlacer())).tries(64).build();
        public static final RandomPatchFeatureConfig AETHER_TALL_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.TALL_GRASS.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build();
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();

        public static final RandomFeatureConfig SCATTERED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(GOLDEN_OAK_CONFIG).withChance(0.1F)),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );

        public static final AercloudConfig COLD_AERCLOUD_CONFIG = new AercloudConfig(AetherBlocks.COLD_AERCLOUD.getDefaultState(), false, 48, 64);
        public static final AercloudConfig BLUE_AERCLOUD_CONFIG = new AercloudConfig(AetherBlocks.BLUE_AERCLOUD.getDefaultState(), false, 24, 32);
        public static final AercloudConfig GOLDEN_AERCLOUD_CONFIG = new AercloudConfig(AetherBlocks.GOLDEN_AERCLOUD.getDefaultState(), false, 16, 96);

        public static final SingleStateFeatureConfig AETHER_LAKES_CONFIG = new SingleStateFeatureConfig(Blocks.WATER.getDefaultState());
        public static final SpringFeatureConfig AETHER_SPRINGS_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), false, 4, 1, ImmutableSet.of(AetherBlocks.AETHER_DIRT, AetherBlocks.HOLYSTONE));
    }
}