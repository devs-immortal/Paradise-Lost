package com.aether.world.gen.decorator;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;

import java.util.Random;
import java.util.stream.Stream;

public class CrystalTreeIslandDecorator extends Decorator<NopeDecoratorConfig> {
    public CrystalTreeIslandDecorator(Codec<NopeDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, NopeDecoratorConfig nopeDecoratorConfig, BlockPos blockPos) {
        Stream<BlockPos> stream = Stream.empty();
        if (random.nextInt(300) == 0) {
            return Stream.concat(stream, Stream.of(blockPos.add(random.nextInt(16), 55 + random.nextInt(16), random.nextInt(16))));
        } else {
            return Stream.empty();
        }
    }
}
