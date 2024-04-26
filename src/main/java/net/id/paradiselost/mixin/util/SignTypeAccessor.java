package net.id.paradiselost.mixin.util;

import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SignType.class)
public interface SignTypeAccessor {
    @Invoker
    static SignType callRegister(SignType type) {
        throw new AssertionError();
    }
}
