package com.aether.items.weapons;

import com.aether.items.utils.AetherTiers;
import net.minecraft.item.SwordItem;

public class AetherSword extends SwordItem {
    private final AetherTiers material;

    public AetherSword(AetherTiers material, float attackSpeed, int damageVsEntity, Settings settings) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }
}