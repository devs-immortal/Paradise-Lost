package com.aether.world.feature;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.BlueberryBushBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.CountNoiseDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.OptionalInt;

public class AetherConfiguredFeatures {

    public static ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK_TREE, SKYROOT_TREE, WISTERIA_TREE, FANCY_SKYROOT_TREE;
    public static ConfiguredFeature<?, ?> SCATTERED_TREES, SPARSE_TREES, THICKET_TREES;
    public static ConfiguredFeature<?, ?> COLD_AERCLOUD, BLUE_AERCLOUD, GOLDEN_AERCLOUD;
    public static ConfiguredFeature<?, ?> PATCH_BLUEBERRY;
    public static ConfiguredFeature<?, ?> AETHER_GRASS, AETHER_TALL_GRASS, ALT_AETHER_GRASS, ALT_AETHER_TALL_GRASS, DENSE_TALL_GRASS;
    public static ConfiguredFeature<?, ?> AETHER_LAKE, AETHER_SPRINGS;
    public static ConfiguredFeature<?, ?> QUICKSOIL;

    public static void registerFeatures() {
        AETHER_GRASS = register("aether_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(8));
        AETHER_TALL_GRASS = register("aether_tall_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_TALL_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(8));
        ALT_AETHER_GRASS = register("alt_aether_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP.decorate(Decorator.COUNT_NOISE.configure(new CountNoiseDecoratorConfig(-0.8D, 4, 10)))));
        ALT_AETHER_TALL_GRASS = register("alt_aether_tall_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_TALL_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP.decorate(Decorator.COUNT_NOISE.configure(new CountNoiseDecoratorConfig(-0.8D, 2, 8)))));
        DENSE_TALL_GRASS = register("dense_aether_tall_grass", Feature.RANDOM_PATCH.configure(Configs.AETHER_TALL_GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(96));
        SKYROOT_TREE = register("skyroot_tree", Feature.TREE.configure(Configs.SKYROOT_CONFIG));
        GOLDEN_OAK_TREE = register("golden_oak_tree", Feature.TREE.configure(Configs.GOLDEN_OAK_CONFIG));
        WISTERIA_TREE = register("wisteria_tree", Feature.TREE.configure(Configs.WISTERIA_CONFIG));
        FANCY_SKYROOT_TREE = register("fancy_skyroot_tree", Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG));
        SCATTERED_TREES = register("scattered_trees", Feature.RANDOM_SELECTOR.configure(Configs.SCATTERED_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(5, 0.1F, 1))));
        SPARSE_TREES = register("sparse_trees", Feature.RANDOM_SELECTOR.configure(Configs.SPARSE_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))));
        THICKET_TREES = register("thicket_trees", Feature.RANDOM_SELECTOR.configure(Configs.THICKET_TREES_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(28, 0.25F, 12))));
        PATCH_BLUEBERRY = register("patch_blueberry", Feature.RANDOM_PATCH.configure(Configs.BLUEBERRY_CONFIG).decorate(Decorator.COUNT_NOISE.configure(new CountNoiseDecoratorConfig(-0.8D, 4, 10))));
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Aether.locate(id), configuredFeature);
    }

    public static class Configs {
        public static final RandomPatchFeatureConfig AETHER_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build();
        //public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(AetherBlocks.PURPLE_FLOWER.getDefaultState(), 2).addState(AetherBlocks.WHITE_FLOWER.getDefaultState(), 1), new SimpleBlockPlacer())).tries(64).build();
        public static final RandomPatchFeatureConfig AETHER_TALL_GRASS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.TALL_GRASS.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build();
        public static final TreeFeatureConfig SKYROOT_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
        public static final TreeFeatureConfig WISTERIA_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.ROSE_WISTERIA_LEAVES.getDefaultState()), new SpruceFoliagePlacer(UniformIntDistribution.of(2, 0), UniformIntDistribution.of(1, 2), UniformIntDistribution.of(2, 1)), new StraightTrunkPlacer(6, 4, 0), new TwoLayersFeatureSize(4, 0, 4))).ignoreVines().build();
        public static final TreeFeatureConfig GOLDEN_OAK_CONFIG = (new net.minecraft.world.gen.feature.TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(UniformIntDistribution.of(4), UniformIntDistribution.of(4), 4), new LargeOakTrunkPlacer(6, 15, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build();
        public static final TreeFeatureConfig FANCY_SKYROOT_CONFIG = (new net.minecraft.world.gen.feature.TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(4), 4), new LargeOakTrunkPlacer(4, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build();
        public static final RandomPatchFeatureConfig BLUEBERRY_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.BLUEBERRY_BUSH.getDefaultState().with(BlueberryBushBlock.RIPE, true)), SimpleBlockPlacer.INSTANCE)).tries(64).whitelist(ImmutableSet.of(AetherBlocks.AETHER_GRASS)).cannotProject().build();

        public static final RandomFeatureConfig SCATTERED_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(WISTERIA_CONFIG).withChance(0.02F)),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig THICKET_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(WISTERIA_CONFIG).withChance(0.0002F), Feature.TREE.configure(GOLDEN_OAK_CONFIG).withChance(0.005F), Feature.TREE.configure(SKYROOT_CONFIG).withChance(0.1F)),
                Feature.TREE.configure(Configs.FANCY_SKYROOT_CONFIG)
        );

        public static final RandomFeatureConfig SPARSE_TREES_CONFIG = new RandomFeatureConfig(
                ImmutableList.of(Feature.TREE.configure(WISTERIA_CONFIG).withChance(0.15F), Feature.TREE.configure(GOLDEN_OAK_CONFIG).withChance(0.001F)),
                Feature.TREE.configure(Configs.SKYROOT_CONFIG)
        );
    }
}