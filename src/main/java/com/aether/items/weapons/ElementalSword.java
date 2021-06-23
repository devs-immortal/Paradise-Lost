package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.tools.AetherSword;
import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class ElementalSword extends AetherSword {

    public ElementalSword(Settings settings) {
        super(AetherTiers.Legendary, -2.4F, 4, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        if (this == AetherItems.FLAMING_SWORD) {
            victim.setOnFireFor(30);
        } else if (this == AetherItems.LIGHTNING_SWORD) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, attacker.world);
            if (attacker instanceof ServerPlayerEntity) lightning.setChanneler((ServerPlayerEntity) attacker);
        } else if (this == AetherItems.HOLY_SWORD && victim.isUndead()) {
            victim.damage(DamageSource.mob(attacker), 20);
            stack.damage(10, attacker, null);
        }
        return super.postHit(stack, victim, attacker);
    }
}