package net.id.aether.world.feature.decorators;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

import java.util.Random;
import java.util.stream.Stream;

public class CrystalTreeIslandDecorator extends PlacementModifier {
    // Copied from BiomePlacementModifier. I don't understand it, but it works.
    private static final CrystalTreeIslandDecorator INSTANCE = new CrystalTreeIslandDecorator();
    public static Codec<CrystalTreeIslandDecorator> MODIFIER_CODEC = Codec.unit(() -> INSTANCE);

    public static CrystalTreeIslandDecorator of() {
        return INSTANCE;
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, BlockPos blockPos) {
        Stream<BlockPos> stream = Stream.empty();
        if (random.nextInt(300) == 0) {
            return Stream.concat(stream, Stream.of(blockPos.add(random.nextInt(16), 137 + random.nextInt(16), random.nextInt(16))));
        } else {
            return Stream.empty();
        }
    }

    @Override
    public PlacementModifierType<?> getType() {
        return AetherDecorators.CRYSTAL_TREE_ISLAND;
    }
}
