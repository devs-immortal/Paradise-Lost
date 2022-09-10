package net.id.paradiselost.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class ParadiseLostDamageSources extends DamageSource {

    public static final DamageSource PARADISE_LOST_FALL = (new ParadiseLostDamageSources("paradise_lost_fall")).setBypassesArmor().setFromFalling();
    public static final DamageSource AERBUNNY_FALL = (new ParadiseLostDamageSources("aerbunny_fall")).setBypassesArmor().setFromFalling();

    public static final DamageSource NIGHTMARE = (new ParadiseLostDamageSources("paradise_lost_nightmare").setBypassesArmor().setUsesMagic().setScaledWithDifficulty());

    protected ParadiseLostDamageSources(String name) {
        super(name);
    }

    public static DamageSource swet(LivingEntity attacker) {
        return new EntityDamageSource("swet", attacker);
    }
}
