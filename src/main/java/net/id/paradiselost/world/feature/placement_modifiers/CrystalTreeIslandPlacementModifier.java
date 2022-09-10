package net.id.paradiselost.world.feature.placement_modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import java.util.stream.Stream;

public class CrystalTreeIslandPlacementModifier extends PlacementModifier {
    // Copied from BiomePlacementModifier. I don't understand it, but it works.
    private static final CrystalTreeIslandPlacementModifier INSTANCE = new CrystalTreeIslandPlacementModifier();
    public static Codec<CrystalTreeIslandPlacementModifier> MODIFIER_CODEC = Codec.unit(() -> INSTANCE);

    public static CrystalTreeIslandPlacementModifier of() {
        return INSTANCE;
    }

    @Override
    public Stream<BlockPos> getPositions(FeaturePlacementContext context, Random random, BlockPos blockPos) {
        Stream<BlockPos> stream = Stream.empty();
        if (random.nextInt(300) == 0) {
            return Stream.concat(stream, Stream.of(blockPos.add(random.nextInt(16), 107 + random.nextInt(15) - context.getBottomY(), random.nextInt(16))));
        } else {
            return Stream.empty();
        }
    }

    @Override
    public PlacementModifierType<?> getType() {
        return ParadiseLostPlacementModifiers.CRYSTAL_TREE_ISLAND;
    }
}
