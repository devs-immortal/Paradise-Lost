package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class VampireBlade extends AetherSword {

    public VampireBlade() {
        super(AetherTiers.Legendary, AetherItems.AETHER_LOOT, 3, -2.4F);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof PlayerEntity)) return super.postHit(stack, target, attacker);

        PlayerEntity player = (PlayerEntity) attacker;
        if (player.getHealth() < player.getMaxHealth()) player.heal(1.0F);
        return super.postHit(stack, target, attacker);
    }
}