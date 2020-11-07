package com.aether.items.weapons;

import com.aether.items.AetherItemGroups;
import com.aether.items.utils.AetherTiers;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Rarity;

public class AetherSword extends SwordItem {

    private final AetherTiers material;

    public AetherSword(AetherTiers material, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, new Settings().group(AetherItemGroups.WEAPONS));
        this.material = material;
    }

    public AetherSword(AetherTiers material, Rarity rarity, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, new Settings().group(AetherItemGroups.WEAPONS).rarity(rarity));
        this.material = material;
    }
}