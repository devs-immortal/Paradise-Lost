package net.id.aether.util;

import net.minecraft.entity.damage.DamageSource;

public class AetherDamageSources extends DamageSource {

    public static final DamageSource AETHER_FALL = (new AetherDamageSources("aether_fall")).setBypassesArmor().setFromFalling();
    public static final DamageSource AERBUNNY_FALL = (new AetherDamageSources("aerbunny_fall")).setBypassesArmor().setFromFalling();

    public static final DamageSource NIGHTMARE = (new AetherDamageSources("nightmare").setBypassesArmor().setUsesMagic().setScaledWithDifficulty());

    protected AetherDamageSources(String name) {
        super(name);
    }
}
