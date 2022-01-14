package net.id.aether.world.feature.feature;

import com.mojang.serialization.Codec;
import net.id.aether.world.feature.config.LongFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class FallenPillarFeature extends Feature<LongFeatureConfig> {

    public FallenPillarFeature(Codec<LongFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<LongFeatureConfig> context) {

        var origin = context.getOrigin();
        var random = context.getRandom();
        var config = context.getConfig();
        var world = context.getWorld();
        var length = config.size().get(random);

        if(world.getBlockState(origin.down()).getMaterial().isReplaceable())
            origin = origin.down();

        if(config.validFloor().contains(world.getBlockState(origin.down()))) {

            boolean shifted = false;
            var axis = (origin.getX() % 2 == 0) ^ (origin.getZ() % 2 == 0) ? Direction.Axis.X : Direction.Axis.Z;

            for (int i = 0; i < length; i++) {

                var placement = origin.offset(axis, i);

                adjust: {
                    var placementState = world.getBlockState(placement);

                    if(!placementState.getMaterial().isReplaceable()) {
                        if(!shifted && world.getBlockState(placement.up()).getMaterial().isReplaceable() && placementState.isSideSolidFullSquare(world, placement, Direction.UP)) {
                            placement = placement.up();
                            shifted = true;
                            break adjust;
                        }
                        return i > 0;
                    }

                    if(world.getBlockState(placement.down()).getMaterial().isReplaceable()) {

                        if(world.getBlockState(placement.down(2)).getMaterial().isReplaceable())
                            return i > 0;

                        placement = placement.down();
                        shifted = true;
                    }
                }

                var body = config.body().getBlockState(random, placement);


                if(body.contains(Properties.AXIS)) {
                    body = body.with(Properties.AXIS, axis);
                }

                if(world.getBlockState(placement).isOf(Blocks.WATER) && body.contains(Properties.WATERLOGGED)) {
                    body = body.with(Properties.WATERLOGGED, true);
                }

                world.setBlockState(placement, body, Block.NOTIFY_ALL);

                for (Direction dir : Direction.values()) {

                    var shell = placement.offset(dir);

                    if(dir.getHorizontal() >= 0 && dir.getAxis() != axis && world.isAir(shell) && random.nextFloat() < config.shellChance()) {
                        var shellState = config.shell().getBlockState(random, shell);
                        if(shellState.contains(Properties.HORIZONTAL_FACING)) {
                            shellState = shellState.with(Properties.HORIZONTAL_FACING, dir.getOpposite());
                        }
                        else if(shellState.contains(Properties.FACING)) {
                            shellState = shellState.with(Properties.FACING, dir.getOpposite());
                        }
                        world.setBlockState(shell, shellState, Block.NOTIFY_ALL);
                    }

                }

                var top = placement.up();

                if(world.isAir(top) && random.nextFloat() < config.topChance()) {
                    world.setBlockState(top, config.top().getBlockState(random, top), Block.NOTIFY_ALL);
                }

            }
            return true;

        }
        return false;
    }
}
