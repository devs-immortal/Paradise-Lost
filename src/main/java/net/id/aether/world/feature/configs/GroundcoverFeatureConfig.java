package net.id.aether.world.feature.configs;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record GroundcoverFeatureConfig(BlockStateProvider states, IntProvider size, IntProvider spacing) implements FeatureConfig {}
