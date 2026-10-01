package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.world.feature.configs.LongFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class FallenPillarFeature extends Feature<LongFeatureConfig> {

    public FallenPillarFeature(Codec<LongFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LongFeatureConfig> context) {

        var origin = context.origin();
        var random = context.random();
        var config = context.config();
        var world = context.level();
        var length = config.size().sample(random);

        if (world.getBlockState(origin.below()).canBeReplaced()) {
            origin = origin.below();
        }

        if (config.validFloor().contains(world.getBlockState(origin.below()).getBlockHolder())) {

            boolean shifted = false;
            var axis = (origin.getX() % 2 == 0) ^ (origin.getZ() % 2 == 0) ? Direction.Axis.X : Direction.Axis.Z;

            for (int i = 0; i < length; i++) {

                var placement = origin.relative(axis, i);

                adjust: {
                    var placementState = world.getBlockState(placement);

                    if (!placementState.canBeReplaced()) {
                        if (!shifted && world.getBlockState(placement.above()).canBeReplaced() && placementState.isFaceSturdy(world, placement, Direction.UP)) {
                            placement = placement.above();
                            shifted = true;
                            break adjust;
                        }
                        return i > 0;
                    }

                    if (world.getBlockState(placement.below()).canBeReplaced()) {

                        if (world.getBlockState(placement.below(2)).canBeReplaced()) {
                            return i > 0;
                        }

                        placement = placement.below();
                        shifted = true;
                    }
                }

                var body = config.body().getState(random, placement);

                if (body.hasProperty(BlockStateProperties.AXIS)) {
                    body = body.setValue(BlockStateProperties.AXIS, axis);
                }

                if (world.getBlockState(placement).is(Blocks.WATER) && body.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    body = body.setValue(BlockStateProperties.WATERLOGGED, true);
                }

                world.setBlock(placement, body, Block.UPDATE_ALL);

                for (Direction dir : Direction.values()) {

                    var shell = placement.relative(dir);

                    if (dir.get2DDataValue() >= 0 && dir.getAxis() != axis && world.isEmptyBlock(shell) && random.nextFloat() < config.shellChance()) {
                        var shellState = config.shell().getState(random, shell);
                        if (shellState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                            shellState = shellState.setValue(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite());
                        }
                        else if (shellState.hasProperty(BlockStateProperties.FACING)) {
                            shellState = shellState.setValue(BlockStateProperties.FACING, dir.getOpposite());
                        }
                        world.setBlock(shell, shellState, Block.UPDATE_ALL);
                    }

                }

                var top = placement.above();

                if (world.isEmptyBlock(top) && random.nextFloat() < config.topChance()) {
                    world.setBlock(top, config.top().getState(random, top), Block.UPDATE_ALL);
                }

            }
            return true;

        }
        return false;
    }
}
