package net.id.paradiselost.world.feature.placement_modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import static net.id.paradiselost.ParadiseLost.locate;

public final class ParadiseLostPlacementModifiers {
    public static final PlacementModifierType<?> CHANCE = register("chance", ChancePlacementModifier.MODIFIER_CODEC);

    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, locate(id), () -> codec);
    }
    
    public static void init() {
    }
}
