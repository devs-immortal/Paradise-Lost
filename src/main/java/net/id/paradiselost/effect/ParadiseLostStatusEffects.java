package net.id.paradiselost.effect;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.registry.ParadiseLostRegistryQueues;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ParadiseLostStatusEffects {

    public static void init () {
        ParadiseLostRegistryQueues.STATUS_EFFECT.register();
    }

    public static SimmeringStatusEffect SIMMERING = add("simmering", new SimmeringStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFFFF));

    private static <V extends StatusEffect> V add(String id, V entry) {
        return ParadiseLostRegistryQueues.STATUS_EFFECT.add(ParadiseLost.locate(id), entry);
    }
}
