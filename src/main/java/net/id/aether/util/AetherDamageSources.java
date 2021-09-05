package net.id.aether.util;

import net.minecraft.entity.damage.DamageSource;

public class AetherDamageSources extends DamageSource {

    public static final DamageSource AETHER_FALL = (new AetherDamageSources("aether_fall")).setBypassesArmor().setFromFalling();

    protected AetherDamageSources(String name) {
        super(name);
    }
}
