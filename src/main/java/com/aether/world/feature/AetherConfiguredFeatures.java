package com.aether.world.feature;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.world.feature.config.AercloudConfig;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class AetherConfiguredFeatures {

    public static ConfiguredFeature<?, ?> GOLDEN_OAK, SKYROOT;
    public static ConfiguredFeature<?, ?> COLD_AERCLOUD, BLUE_AERCLOUD, GOLDEN_AERCLOUD;
    public static ConfiguredFeature<?, ?> AETHER_GRASS, AETHER_TALL_GRASS;
    public static ConfiguredFeature<?, ?> AETHER_LAKE;
    public static ConfiguredFeature<?, ?> QUICKSOIL;

    public static void registerFeatures() {
        AETHER_LAKE = register("aether_lake", AetherFeatures.DEFAULT_LAKE.configure(new SingleStateFeatureConfig(Blocks.WATER.getDefaultState())).decorate(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(4))));
        AETHER_GRASS = register("aether_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(4));
        AETHER_TALL_GRASS = register("aether_tall_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_TALL_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(4));
        SKYROOT = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG));
        GOLDEN_OAK = register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG));
        COLD_AERCLOUD = register("cold_aercloud", AetherFeatures.DEFAULT_AERCLOUD.configure(Configs.COLD_AERCLOUD_CONFIG).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(10))));
        BLUE_AERCLOUD = register("blue_aercloud", AetherFeatures.DEFAULT_AERCLOUD.configure(Configs.BLUE_AERCLOUD_CONFIG).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(20))));
        GOLDEN_AERCLOUD = register("golden_aercloud", AetherFeatures.DEFAULT_AERCLOUD.configure(Configs.GOLDEN_AERCLOUD_CONFIG).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(30))));
        QUICKSOIL = register("quicksoil", AetherFeatures.DEFAULT_QUICKSOIL.configure(new DefaultFeatureConfig()));
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Aether.MOD_ID, id), configuredFeature);
    }

    public static class Configs {
        public static final RandomPatchFeatureConfig AETHER_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build();
        //public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(AetherBlocks.PURPLE_FLOWER.getDefaultState(), 2).addState(AetherBlocks.WHITE_FLOWER.getDefaultState(), 1), new SimpleBlockPlacer())).tries(64).build();
        public static final RandomPatchFeatureConfig AETHER_TALL_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.TALL_GRASS.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build();
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();

        public static final AercloudConfig COLD_AERCLOUD_CONFIG = new AercloudConfig(AetherBlocks.COLD_AERCLOUD.getDefaultState(), false, 16, 64);
        public static final AercloudConfig BLUE_AERCLOUD_CONFIG = new AercloudConfig(AetherBlocks.BLUE_AERCLOUD.getDefaultState(), false, 8, 32);
        public static final AercloudConfig GOLDEN_AERCLOUD_CONFIG = new AercloudConfig(AetherBlocks.GOLDEN_AERCLOUD.getDefaultState(), false, 4, 96);
    }
}
