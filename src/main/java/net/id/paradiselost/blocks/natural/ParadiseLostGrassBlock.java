package net.id.paradiselost.blocks.natural;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.util.SpreadableParadiseLostBlock;
import net.id.paradiselost.world.feature.placed_features.ParadiseLostVegetationPlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;

import java.util.List;

public class ParadiseLostGrassBlock extends SpreadableParadiseLostBlock implements Fertilizable {
    public ParadiseLostGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = ParadiseLostBlocks.GRASS.getDefaultState();

        block0: for (int i = 0; i < 128; ++i) {
            RegistryEntry<PlacedFeature> placedFeature;
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

            if (!blockState2.isAir()) {
                continue;
            }
            if (random.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).value().getGenerationSettings().getFlowerFeatures();
                if (list.isEmpty()) {
                    continue;
                }
                placedFeature = ((RandomPatchFeatureConfig) ((ConfiguredFeature) list.get(0)).config()).feature();
            } else {
                placedFeature = world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getEntry(ParadiseLostVegetationPlacedFeatures.GRASS).get();
            }
            (placedFeature.value()).generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos2);
        }
    }
}
