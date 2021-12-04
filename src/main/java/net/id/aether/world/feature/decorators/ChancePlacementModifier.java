package net.id.aether.world.feature.decorators;

import com.mojang.serialization.Codec;
import net.id.aether.mixin.world.PlacementModifierTypeAccessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.decorator.AbstractCountPlacementModifier;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

import java.util.Random;

import static net.id.aether.mixin.world.PlacementModifierTypeAccessor.callRegister;

// They removed the chance decorator in 1.18, so I've added it here.
public class ChancePlacementModifier extends AbstractCountPlacementModifier {
    public static final Codec<ChancePlacementModifier> MODIFIER_CODEC = IntProvider.createValidatingCodec(0, 256).fieldOf("count").xmap(ChancePlacementModifier::new, (chance) -> {
        return chance.chance;
    }).codec();

    private final IntProvider chance;

    PlacementModifierType<ChancePlacementModifier> COUNT = callRegister("aether_chance", ChancePlacementModifier.MODIFIER_CODEC);

    public ChancePlacementModifier (IntProvider chance) {
        this.chance = chance;
    }

    @Override
    protected int getCount(Random random, BlockPos pos) {
        return random.nextFloat() < 1.0F / (float) chance.get(random) ? 1 : 0;
    }

    @Override
    public PlacementModifierType<?> getType() {
        return COUNT;
    }
}
