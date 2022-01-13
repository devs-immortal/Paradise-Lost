package net.id.aether.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.aether.world.feature.config.LongFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PillarFeature extends Feature<LongFeatureConfig> {

    public PillarFeature(Codec<LongFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<LongFeatureConfig> context) {
        var pos = context.getOrigin();
        var random = context.getRandom();
        var config = context.getConfig();
        var world = context.getWorld();
        var height = config.size().get(random);

        if(config.validFloor().contains(world.getBlockState(pos.down()))) {
            var valid = true;
            var check = 0;

            while (valid && check < height + 2) {

                if(!world.isAir(pos.up(check))) {
                    valid = false;
                }

                check++;
            }

            if(valid) {
                for (int i = 0; i < height; i++) {
                    var pillar = pos.up(i);

                    world.setBlockState(pillar, config.body().getBlockState(random, pillar), Block.NOTIFY_ALL);

                    for (Direction dir : Direction.values()) {
                        var shell = pillar.offset(dir);
                        if(dir.getHorizontal() >= 0 && world.isAir(shell) && random.nextFloat() < config.shellChance()) {

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
                }

                if(random.nextFloat() < config.topChance()) {
                    var tip = pos.up(height);
                    world.setBlockState(tip, config.top().getBlockState(random, tip), Block.NOTIFY_ALL);
                }
            }

            return valid;
        }

        return false;
    }
}
