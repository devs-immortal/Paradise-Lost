package net.id.paradiselost.world.feature.placement_modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.placementmodifier.AbstractCountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

// They removed the chance decorator in 1.18, so I've added it here.
public class ChancePlacementModifier extends AbstractCountPlacementModifier {
    public static final Codec<ChancePlacementModifier> MODIFIER_CODEC = IntProvider.createValidatingCodec(0, 256)
            .fieldOf("chance")
            .xmap(ChancePlacementModifier::new, (chance) -> chance.chance)
            .codec();

    private final IntProvider chance;

    public ChancePlacementModifier(IntProvider chance) {
        this.chance = chance;
    }

    public static ChancePlacementModifier of(int chance) {
        return new ChancePlacementModifier(ConstantIntProvider.create(chance));
    }

    @Override
    protected int getCount(Random random, BlockPos pos) {
        return random.nextFloat() < 1.0F / (float) chance.get(random) ? 1 : 0;
    }

    @Override
    public PlacementModifierType<?> getType() {
        return ParadiseLostPlacementModifiers.CHANCE;
    }
}
