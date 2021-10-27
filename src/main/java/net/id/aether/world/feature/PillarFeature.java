package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.aether.world.feature.config.PillarFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class PillarFeature extends Feature<PillarFeatureConfig> {

    public static final Codec<PillarFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.VALUE_CODEC.fieldOf("height").forGetter(PillarFeatureConfig::height),
            BlockStateProvider.TYPE_CODEC.fieldOf("body").forGetter(PillarFeatureConfig::body),
            BlockStateProvider.TYPE_CODEC.fieldOf("tip").forGetter(PillarFeatureConfig::tip),
            BlockStateProvider.TYPE_CODEC.fieldOf("shell").forGetter(PillarFeatureConfig::shell),
            Codec.FLOAT.fieldOf("tipChance").forGetter(PillarFeatureConfig::tipChance),
            Codec.FLOAT.fieldOf("shellChance").forGetter(PillarFeatureConfig::shellChance),
            Codec.list(BlockState.CODEC).fieldOf("floor").forGetter(PillarFeatureConfig::validFloor)
    ).apply(instance, PillarFeatureConfig::new));

    public PillarFeature(Codec<PillarFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<PillarFeatureConfig> context) {
        var pos = context.getOrigin();
        var random = context.getRandom();
        var config = context.getConfig();
        var world = context.getWorld();
        var height = config.height().get(random);

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

                    world.setBlockState(pillar, config.body().getBlockState(random, pillar), 3);

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

                            world.setBlockState(shell, shellState, 3);
                        }
                    }
                }

                if(random.nextFloat() < config.tipChance()) {
                    var tip = pos.up(height);
                    world.setBlockState(tip, config.tip().getBlockState(random, tip), 3);
                }
            }

            return valid;
        }

        return false;
    }
}
