package com.aether.blocks.natural;

import com.aether.blocks.SpreadableAetherBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractFlowerFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class AetherGrassBlock extends SpreadableAetherBlock implements BonemealableBlock {
    public AetherGrassBlock(Properties settings) {
        super(settings);
    }

    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.above()).isAir();
    }

    public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.above();
        BlockState blockState = Blocks.GRASS.defaultBlockState();

        label48:
        for (int i = 0; i < 128; ++i) {
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; ++j) {
                blockPos2 = blockPos2.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.below()).is(this) || world.getBlockState(blockPos2).isCollisionShapeFullBlock(world, blockPos2))
                    continue label48;
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.is(blockState.getBlock()) && random.nextInt(10) == 0)
                ((BonemealableBlock) blockState.getBlock()).performBonemeal(world, random, blockPos2, blockState2);

            if (blockState2.isAir()) {
                BlockState blockState4;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) continue;

                    ConfiguredFeature<?, ?> configuredFeature = list.get(0);
                    AbstractFlowerFeature<RandomPatchConfiguration> flowerFeature = (AbstractFlowerFeature<RandomPatchConfiguration>) configuredFeature.feature;
                    blockState4 = flowerFeature.getRandomFlower(random, blockPos2, (RandomPatchConfiguration) configuredFeature.config());
                } else {
                    blockState4 = blockState;
                }

                if (blockState4.canSurvive(world, blockPos2)) world.setBlock(blockPos2, blockState4, 3);
            }
        }
    }
}