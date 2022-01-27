package net.id.aether.world.feature.decorators;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

import static net.id.aether.Aether.locate;

public final class AetherDecorators{
    public static final PlacementModifierType<?> CHANCE = register("chance", ChancePlacementModifier.MODIFIER_CODEC);
    public static final PlacementModifierType<?> CRYSTAL_TREE_ISLAND = register("crystal_tree_island", CrystalTreeIslandDecorator.MODIFIER_CODEC);

    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, locate(id), ()->codec);
    }
    
    public static void init(){}
}
