package com.aether.world.gen.decorator;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.decorator.SimpleDecorator;

import java.util.Random;
import java.util.stream.Stream;

public class CrystalTreeIslandDecorator extends SimpleDecorator<NopeDecoratorConfig> {
    public CrystalTreeIslandDecorator(Codec<NopeDecoratorConfig> codec) {
        super(codec);
    }

    public Stream<BlockPos> getPositions(Random random, NopeDecoratorConfig nopeDecoratorConfig, BlockPos blockPos) {
        Stream<BlockPos> stream = Stream.empty();
        if (random.nextInt(90) == 0) {
            return Stream.concat(stream, Stream.of(blockPos.add(random.nextInt(16), 55 + random.nextInt(16), random.nextInt(16))));
        } else {
            return Stream.empty();
        }
    }
}
