package net.id.paradiselost.mixin.util;

import net.id.incubus_core.util.EnumExtender;
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
    @Shadow(aliases = "field_8905") @Mutable @Final private static Rarity[] VALUES;

    static {
        EnumExtender.register(Rarity.class, (name, args) -> {
            Rarity entry = (Rarity) (Object) new RarityMixin(name, VALUES.length, (Formatting) args[0]);
            VALUES = Arrays.copyOf(VALUES, VALUES.length + 1);
            return VALUES[VALUES.length - 1] = entry;
        });
    }

    private RarityMixin(String valueName, int ordinal, Formatting formatting) {
        throw new AssertionError();
    }
}
