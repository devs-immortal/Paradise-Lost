package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class AetherConfiguredFeatures {
    /*final RandomPatchFeatureConfig AETHER_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build();
    final RandomPatchFeatureConfig AETHER_FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(AetherBlocks.PURPLE_FLOWER.getDefaultState(), 2).addState(AetherBlocks.WHITE_FLOWER.getDefaultState(), 1), new SimpleBlockPlacer())).tries(64).build();
    final RandomPatchFeatureConfig AETHER_TALL_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.TALL_GRASS.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build();
    final TreeFeatureConfig AETHER_SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlocksAether.skyroot_log.getDefaultState()), new SimpleBlockStateProvider(BlocksAether.skyroot_leaves.getDefaultState()), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
    final TreeFeatureConfig AETHER_GOLDEN_OAK_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlocksAether.golden_oak_log.getDefaultState()), new SimpleBlockStateProvider(BlocksAether.golden_oak_leaves.getDefaultState()), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();

    final AercloudConfig AETHER_COLD_AERCLOUD_CONFIG = new AercloudConfig(BlocksAether.cold_aercloud.getDefaultState(), false, 16, 64);
    final AercloudConfig AETHER_BLUE_AERCLOUD_CONFIG = new AercloudConfig(BlocksAether.blue_aercloud.getDefaultState(), false, 8, 32);
    final AercloudConfig AETHER_GOLDEN_AERCLOUD_CONFIG = new AercloudConfig(BlocksAether.golden_aercloud.getDefaultState(), false, 4, 96);

    public static void registerConfiguredFeatures(){
        MutableRegistry<ConfiguredFeature<?, ?>> registry = (MutableRegistry<ConfiguredFeature<?, ?>>) BuiltinRegistries.CONFIGURED_FEATURE;
    }*/
}
