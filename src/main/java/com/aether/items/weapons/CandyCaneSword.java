package com.aether.items.weapons;

import java.util.Random;

import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import com.aether.items.utils.AetherTiers;

public class CandyCaneSword extends AetherSword {
    public CandyCaneSword() {
        super(AetherTiers.CANDY, 3, -2.0F);
    }

    @Override
    public boolean postHit(ItemStack itemstack, LivingEntity entityliving, LivingEntity entityliving1) {
        // TODO: deathCounter replace ???
        if (entityliving.deathTime > 0) {
            return true;
        } else {
            if ((new Random()).nextBoolean() && entityliving1 instanceof PlayerEntity && !entityliving1.world.isClient && entityliving.hurtTime > 0)
                entityliving.dropItem(AetherItems.CANDY_CANE, 1);
            itemstack.damage(1, entityliving1, null);
            return true;
        }
    }
}