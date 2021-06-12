package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class CandyCaneSword extends AetherSword {
    public CandyCaneSword(Properties settings) {
        super(AetherTiers.Candy, 3, -2, settings);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entityLiving, LivingEntity entityLiving1) {
        // TODO: deathCounter replace ???
        if (entityLiving.deathTime > 0) {
            return true;
        } else {
            if ((new Random()).nextBoolean() && entityLiving1 instanceof Player && !entityLiving1.level.isClientSide && entityLiving.hurtTime > 0)
                entityLiving.spawnAtLocation(AetherItems.CANDY_CANE, 1);
            itemStack.hurtAndBreak(1, entityLiving1, null);
            return true;
        }
    }
}