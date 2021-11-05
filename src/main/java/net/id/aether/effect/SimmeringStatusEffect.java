package net.id.aether.effect;

import net.id.aether.effect.condition.Condition;
import net.id.aether.effect.condition.ConditionModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SimmeringStatusEffect extends StatusEffect implements ConditionModifier {

    protected SimmeringStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(1.0F);
        }
    }

    @Override
    public float getDecayMultiplier(Condition condition) {
        return 2.0F;
    }

    @Override
    public float getSeverityMultiplier(Condition condition) {
        return 0.7F;
    }
}
