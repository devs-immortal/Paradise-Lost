package net.id.aether.world.feature.decorators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

import java.util.Random;
import java.util.stream.Stream;

import static net.id.aether.mixin.world.PlacementModifierTypeAccessor.callRegister;

public class CrystalTreeIslandDecorator extends PlacementModifier {
    PlacementModifierType<PlacementModifier> CRYSTAL_TREE_ISLAND = callRegister("crystal_tree_island", CrystalTreeIslandDecorator.CODEC);

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, BlockPos blockPos) {
        Stream<BlockPos> stream = Stream.empty();
        if (random.nextInt(300) == 0) {
            return Stream.concat(stream, Stream.of(blockPos.add(random.nextInt(16), 55 + random.nextInt(16), random.nextInt(16))));
        } else {
            return Stream.empty();
        }
    }

    @Override
    public PlacementModifierType<?> getType() {
        return CRYSTAL_TREE_ISLAND;
    }
}
