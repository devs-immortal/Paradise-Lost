package net.id.aether.effect;

import net.id.aether.Aether;
import net.id.aether.registry.AetherRegistryQueues;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class AetherStatusEffects {

    public static void init () {
        AetherRegistryQueues.STATUS_EFFECT.register();
    }

    public static SimmeringStatusEffect SIMMERING = add("simmering", new SimmeringStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFFFF));

    private static <V extends StatusEffect> V add(String id, V entry) {
        return AetherRegistryQueues.STATUS_EFFECT.add(Aether.locate(id), entry);
    }
}
