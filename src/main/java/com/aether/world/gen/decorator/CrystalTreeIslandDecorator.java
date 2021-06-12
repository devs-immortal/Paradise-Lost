package com.aether.world.gen.decorator;

import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class CrystalTreeIslandDecorator extends FeatureDecorator<NoneDecoratorConfiguration> {
    public CrystalTreeIslandDecorator(Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecorationContext context, Random random, NoneDecoratorConfiguration nopeDecoratorConfig, BlockPos blockPos) {
        Stream<BlockPos> stream = Stream.empty();
        if (random.nextInt(300) == 0) {
            return Stream.concat(stream, Stream.of(blockPos.offset(random.nextInt(16), 55 + random.nextInt(16), random.nextInt(16))));
        } else {
            return Stream.empty();
        }
    }
}
