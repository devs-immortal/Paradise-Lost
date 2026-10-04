package net.id.paradise_lost.block.natural;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.util.SpreadableParadiseLostBlock;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostVegetationPlacedFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import java.util.List;

public class ParadiseLostGrassBlock extends SpreadableParadiseLostBlock implements BonemealableBlock {
    public ParadiseLostGrassBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.above();
        BlockState blockState = BlockRegistry.GRASS.get().defaultBlockState();

        block0: for (int i = 0; i < 128; ++i) {
            Holder<PlacedFeature> placedFeature;
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; ++j) {
                if (!world.getBlockState(
                        (blockPos2 = blockPos2.offset(
                                random.nextInt(3) - 1,
                                (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                                random.nextInt(3) - 1
                        )).below()
                ).is(this) || world.getBlockState(blockPos2).isCollisionShapeFullBlock(world, blockPos2)) continue block0;
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.is(blockState.getBlock()) && random.nextInt(10) == 0) {
                ((BonemealableBlock) blockState.getBlock()).performBonemeal(world, random, blockPos2, blockState2);
            }

            if (!blockState2.isAir()) {
                continue;
            }
            if (random.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).value().getGenerationSettings().getFlowerFeatures();
                if (list.isEmpty()) {
                    continue;
                }
                placedFeature = ((RandomPatchConfiguration) ((ConfiguredFeature) list.get(0)).config()).feature();
            } else {
                placedFeature = world.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(ParadiseLostVegetationPlacedFeatures.GRASS).get();
            }
            (placedFeature.value()).place(world, world.getChunkSource().getGenerator(), random, blockPos2);
        }
    }
}
