package net.id.paradiselost.effect;

import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.id.incubus_core.condition.api.Condition;
import net.id.incubus_core.condition.api.ConditionModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;

public class SimmeringStatusEffect extends StatusEffect implements ConditionModifier {

    protected SimmeringStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        var random = entity.getRandom();
        var world = entity.getEntityWorld();

        if(world.getTime() % (random.nextInt(10) + 6) == 0) {
            if(random.nextBoolean()) {
                entity.heal(1F);
                entity.playSound(ParadiseLostSoundEvents.EFFECT_SIMMERING_SIMMER, random.nextFloat() * 0.4F + 0.4F, random.nextFloat() * 0.5F + 1);
            }
            world.addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0.0D, 0.1, 0.0D);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public float getDecayMultiplier(Condition condition) {
        return 2.25F;
    }
}
