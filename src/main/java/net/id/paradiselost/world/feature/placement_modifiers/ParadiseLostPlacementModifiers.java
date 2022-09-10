package net.id.paradiselost.world.feature.placement_modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import static net.id.paradiselost.ParadiseLost.locate;

public final class ParadiseLostPlacementModifiers {
    public static final PlacementModifierType<?> CHANCE = register("chance", ChancePlacementModifier.MODIFIER_CODEC);
    public static final PlacementModifierType<?> CRYSTAL_TREE_ISLAND = register("crystal_tree_island", CrystalTreeIslandPlacementModifier.MODIFIER_CODEC);

    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, locate(id), ()->codec);
    }
    
    public static void init(){}
}
