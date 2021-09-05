package net.id.aether.mixin.util;

import net.id.aether.util.EnumExtender;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;

@Mixin(Rarity.class)
public class RarityMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Mutable
    @Final
    private static Rarity[] field_8905;

    static {
        EnumExtender.register(Rarity.class, (name, args) -> {
            Rarity entry = (Rarity) (Object) new RarityMixin(name, field_8905.length, (Formatting) args[0]);
            field_8905 = Arrays.copyOf(field_8905, field_8905.length + 1);
            return field_8905[field_8905.length - 1] = entry;
        });
    }

    private RarityMixin(String valueName, int ordinal, Formatting formatting) {
        throw new AssertionError();
    }
}
