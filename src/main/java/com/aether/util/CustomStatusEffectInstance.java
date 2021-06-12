package com.aether.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;

public class CustomStatusEffectInstance extends MobEffectInstance {

    public boolean ShowParticles = false;

    public CustomStatusEffectInstance(MobEffect statusEffect) {
        super(statusEffect);
    }

    public CustomStatusEffectInstance(MobEffect type, int duration) {
        super(type, duration);
    }

    public CustomStatusEffectInstance(MobEffect type, int duration, int amplifier) {
        super(type, duration, amplifier);
    }

    public CustomStatusEffectInstance(MobEffect type, int duration, int amplifier, boolean ambient, boolean visible) {
        super(type, duration, amplifier, ambient, visible);
    }

    public CustomStatusEffectInstance(MobEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
        super(type, duration, amplifier, ambient, showParticles, showIcon);
    }

    public CustomStatusEffectInstance(MobEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable MobEffectInstance hiddenEffect) {
        super(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect);
    }

    public CustomStatusEffectInstance(MobEffectInstance statusEffectInstance) {
        super(statusEffectInstance);
    }

    @Override
    public boolean isVisible() {
        return this.ShowParticles;
    }
}