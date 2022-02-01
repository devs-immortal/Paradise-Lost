package net.id.aether.blocks.natural;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.util.SpreadableAetherBlock;
import net.id.aether.world.feature.placed_features.AetherVegetationPlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;

import java.util.List;
import java.util.Random;

public class AetherGrassBlock extends SpreadableAetherBlock implements Fertilizable {
    public AetherGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = AetherBlocks.AETHER_GRASS.getDefaultState();
        block0: for (int i = 0; i < 128; ++i) {
            PlacedFeature placedFeature;
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; ++j) {
                if (!world.getBlockState(
                        (blockPos2 = blockPos2.add(
                                random.nextInt(3) - 1,
                                (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                                random.nextInt(3) - 1
                        )).down()
                ).isOf(this) || world.getBlockState(blockPos2).isFullCube(world, blockPos2)) continue block0;
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.isOf(blockState.getBlock()) && random.nextInt(10) == 0) {
                ((Fertilizable) blockState.getBlock()).grow(world, random, blockPos2, blockState2);
            }

            if (!blockState2.isAir()) continue;
            if (random.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).getGenerationSettings().getFlowerFeatures();
                if (list.isEmpty()) continue;
                placedFeature = ((RandomPatchFeatureConfig)list.get(0).getConfig()).feature().get();
            } else {
                placedFeature = AetherVegetationPlacedFeatures.AETHER_GRASS_BONEMEAL;
            }
            placedFeature.generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos2);
        }
    }
}