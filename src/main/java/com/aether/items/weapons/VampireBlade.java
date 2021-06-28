package com.aether.items.weapons;

import com.aether.items.tools.AetherSword;
import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class VampireBlade extends AetherSword {

    public VampireBlade(Settings settings) {
        super(AetherTiers.Legendary, -2.4F, 3, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof PlayerEntity)) return super.postHit(stack, target, attacker);

        PlayerEntity player = (PlayerEntity) attacker;
        if (player.getHealth() < player.getMaxHealth()) player.heal(1.0F);
        return super.postHit(stack, target, attacker);
    }
}