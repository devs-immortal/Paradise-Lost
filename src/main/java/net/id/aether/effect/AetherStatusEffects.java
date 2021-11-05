package net.id.aether.effect;

import net.id.aether.Aether;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.registry.Registry;

public class AetherStatusEffects {
    public static SimmeringStatusEffect SIMMERING = register(369, "simmering", new SimmeringStatusEffect(StatusEffectCategory.BENEFICIAL, 0x0000FF));

    private static <V extends StatusEffect> V register(int rawId, String id, V entry) {
        return Registry.register(Registry.STATUS_EFFECT, Aether.locate(id), entry);
    }
}
