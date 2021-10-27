package net.id.aether.world.feature.config;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public record PillarFeatureConfig(IntProvider height, BlockStateProvider body, BlockStateProvider tip, BlockStateProvider shell, float tipChance, float shellChance, List<BlockState> validFloor) implements FeatureConfig {}
