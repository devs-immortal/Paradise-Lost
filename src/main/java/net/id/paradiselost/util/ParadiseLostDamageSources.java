package net.id.paradiselost.util;

import net.minecraft.entity.damage.DamageSource;

public class ParadiseLostDamageSources extends DamageSource {

    public static final DamageSource PARADISE_LOST_FALL = (new ParadiseLostDamageSources("paradise_lost_fall")).setBypassesArmor().setFromFalling();
    public static final DamageSource PARADISE_HARE_FALL = (new ParadiseLostDamageSources("corsican_hare_fall")).setBypassesArmor().setFromFalling();

    public static final DamageSource NIGHTMARE = (new ParadiseLostDamageSources("paradise_lost_nightmare").setBypassesArmor().setUsesMagic().setScaledWithDifficulty());

    protected ParadiseLostDamageSources(String name) {
        super(name);
    }

}
