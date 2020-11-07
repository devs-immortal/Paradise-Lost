package com.aether.util;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;

public class CustomStatusEffectInstance extends StatusEffectInstance {

    public boolean ShowParticles = false;
    public CustomStatusEffectInstance(StatusEffect statusEffect) {
        super(statusEffect);
    }

    public CustomStatusEffectInstance(StatusEffect type, int duration) {
        super(type, duration);
    }

    public CustomStatusEffectInstance(StatusEffect type, int duration, int amplifier) {
        super(type, duration, amplifier);
    }

    public CustomStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean visible) {
        super(type, duration, amplifier, ambient, visible);
    }

    public CustomStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
        super(type, duration, amplifier, ambient, showParticles, showIcon);
    }

    public CustomStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable StatusEffectInstance hiddenEffect) {
        super(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect);
    }

    public CustomStatusEffectInstance(StatusEffectInstance statusEffectInstance) {
        super(statusEffectInstance);
    }

    @Override
    public boolean shouldShowParticles() {
        return this.ShowParticles;
    }
}
