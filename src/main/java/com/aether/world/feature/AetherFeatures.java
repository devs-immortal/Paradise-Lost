package com.aether.world.feature;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;

import java.util.OptionalInt;

public class AetherFeatures {


    public static final ConfiguredFeature<TreeFeatureConfig, ?> GOLDEN_OAK;

    static {
        GOLDEN_OAK = register("fancy_oak", Feature.TREE.configure((new net.minecraft.world.gen.feature.TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(UniformIntDistribution.of(3), UniformIntDistribution.of(6), 5), new LargeOakTrunkPlacer(5, 17, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(6)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build()));
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Aether.MOD_ID, id), configuredFeature);
    }
}
