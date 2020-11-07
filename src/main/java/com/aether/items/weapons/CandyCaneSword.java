package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class CandyCaneSword extends AetherSword {
    public CandyCaneSword() {
        super(AetherTiers.CANDY, 3, -2.0F);
    }

    @Override
    public boolean postHit(ItemStack itemStack, LivingEntity entityLiving, LivingEntity entityLiving1) {
        // TODO: deathCounter replace ???
        if (entityLiving.deathTime > 0) {
            return true;
        } else {
            if ((new Random()).nextBoolean() && entityLiving1 instanceof PlayerEntity && !entityLiving1.world.isClient && entityLiving.hurtTime > 0)
                entityLiving.dropItem(AetherItems.candy_cane, 1);
            itemStack.damage(1, entityLiving1, null);
            return true;
        }
    }
}