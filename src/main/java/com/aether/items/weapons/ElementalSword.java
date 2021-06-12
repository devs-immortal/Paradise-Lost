package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ElementalSword extends AetherSword {

    public ElementalSword(Properties settings) {
        super(AetherTiers.Legendary, -2.4F, 4, settings);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        if (this == AetherItems.FLAMING_SWORD) {
            victim.setSecondsOnFire(30);
        } else if (this == AetherItems.LIGHTNING_SWORD) {
            LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, attacker.level);
            if (attacker instanceof ServerPlayer) lightning.setCause((ServerPlayer) attacker);
        } else if (this == AetherItems.HOLY_SWORD && victim.isInvertedHealAndHarm()) {
            victim.hurt(DamageSource.mobAttack(attacker), 20);
            stack.hurtAndBreak(10, attacker, null);
        }
        return super.hurtEnemy(stack, victim, attacker);
    }
}