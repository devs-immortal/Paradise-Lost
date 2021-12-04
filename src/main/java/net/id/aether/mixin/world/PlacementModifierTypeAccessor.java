package net.id.aether.mixin.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlacementModifierType.class)
public interface PlacementModifierTypeAccessor {
    @Invoker("register")
    static <P extends PlacementModifier> PlacementModifierType<P> callRegister(String id, Codec<P> codec) {
        throw new AssertionError();
    }
}
