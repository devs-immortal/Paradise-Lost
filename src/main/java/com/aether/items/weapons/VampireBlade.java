package com.aether.items.weapons;

import com.aether.items.utils.AetherTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VampireBlade extends AetherSword {

    public VampireBlade(Properties settings) {
        super(AetherTiers.Legendary, -2.4F, 3, settings);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player)) return super.hurtEnemy(stack, target, attacker);

        Player player = (Player) attacker;
        if (player.getHealth() < player.getMaxHealth()) player.heal(1.0F);
        return super.hurtEnemy(stack, target, attacker);
    }
}