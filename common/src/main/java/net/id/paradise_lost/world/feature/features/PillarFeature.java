package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.world.feature.configs.LongFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class PillarFeature extends Feature<LongFeatureConfig> {

    public PillarFeature(Codec<LongFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LongFeatureConfig> context) {
        var pos = context.origin();
        var random = context.random();
        var config = context.config();
        var world = context.level();
        var height = config.size().sample(random);

        if (config.validFloor().contains(world.getBlockState(pos.below()).getBlockHolder())) {
            var valid = true;
            var check = 0;

            while (valid && check < height + 2) {

                if (!world.isEmptyBlock(pos.above(check))) {
                    valid = false;
                }

                check++;
            }

            if (valid) {
                for (int i = 0; i < height; i++) {
                    var pillar = pos.above(i);

                    world.setBlock(pillar, config.body().getState(random, pillar), Block.UPDATE_ALL);

                    for (Direction dir : Direction.values()) {
                        var shell = pillar.relative(dir);
                        if (dir.get2DDataValue() >= 0 && world.isEmptyBlock(shell) && random.nextFloat() < config.shellChance()) {

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
                }

                if (random.nextFloat() < config.topChance()) {
                    var tip = pos.above(height);
                    world.setBlock(tip, config.top().getState(random, tip), Block.UPDATE_ALL);
                }
            }

            return valid;
        }

        return false;
    }
}
