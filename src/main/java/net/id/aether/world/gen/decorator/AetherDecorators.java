package net.id.aether.world.gen.decorator;

import com.mojang.serialization.Codec;
import net.id.aether.world.feature.decorators.ChancePlacementModifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

import static net.id.aether.Aether.locate;

public final class AetherDecorators{
    // PlacementModifierType<ChancePlacementModifier> CHANCE = () -> MODIFIER_CODEC;
    public static final PlacementModifierType<?> CHANCE = register("chance", ChancePlacementModifier.MODIFIER_CODEC);
    
    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, locate(id), ()->codec);
    }
    
    private AetherDecorators(){}
    
    public static void init(){}
}
