package net.id.aether.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class AetherDamageSources extends DamageSource {

    public static final DamageSource AETHER_FALL = (new AetherDamageSources("aether_fall")).setBypassesArmor().setFromFalling();
    public static final DamageSource AERBUNNY_FALL = (new AetherDamageSources("aerbunny_fall")).setBypassesArmor().setFromFalling();

    public static final DamageSource NIGHTMARE = (new AetherDamageSources("aether_nightmare").setBypassesArmor().setUsesMagic().setScaledWithDifficulty());

    protected AetherDamageSources(String name) {
        super(name);
    }

    public static DamageSource swet(LivingEntity attacker) {
        return new EntityDamageSource("swet", attacker);
    }
}
