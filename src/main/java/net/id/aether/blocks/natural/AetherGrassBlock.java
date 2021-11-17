package net.id.aether.blocks.natural;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.util.SpreadableAetherBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class AetherGrassBlock extends SpreadableAetherBlock implements Fertilizable {
    public AetherGrassBlock(Settings settings) {
        super(settings);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = AetherBlocks.AETHER_GRASS.getDefaultState();

        label48:
        for (int i = 0; i < 128; ++i) {
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; ++j) {
                blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.down()).isOf(this) || world.getBlockState(blockPos2).isFullCube(world, blockPos2))
                    continue label48;
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.isOf(blockState.getBlock()) && random.nextInt(10) == 0)
                ((Fertilizable) blockState.getBlock()).grow(world, random, blockPos2, blockState2);

            // FIXME: Temp-Code, replace closer to 1.18
            // This is here as a mitigation guide
            /*if (blockState2.isAir()) {
                BlockState blockState4;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) continue;

                    ConfiguredFeature<?, ?> configuredFeature = list.get(0);
                    FlowerFeature<RandomPatchFeatureConfig> flowerFeature = (FlowerFeature<RandomPatchFeatureConfig>) configuredFeature.feature;
                    blockState4 = flowerFeature.getFlowerState(random, blockPos2, (RandomPatchFeatureConfig) configuredFeature.getConfig());
                } else {
                    blockState4 = blockState;
                }

                if (blockState4.canPlaceAt(world, blockPos2)) world.setBlockState(blockPos2, blockState4, Block.NOTIFY_ALL);
            }*/
            if (blockState2.isAir()) {
                PlacedFeature placedFeature;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) {
                        continue;
                    }

                    placedFeature = ((RandomPatchFeatureConfig)list.get(0).getConfig()).feature().get();
                } else {
                    placedFeature = VegetationPlacedFeatures.GRASS_BONEMEAL;
                }

                placedFeature.generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos2);
            }
        }
    }
}