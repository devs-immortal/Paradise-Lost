package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.tools.AetherSword;
import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class CandyCaneSword extends AetherSword {
    public CandyCaneSword(Settings settings) {
        super(AetherTiers.Candy, 3, -2, settings);
    }

    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == AetherItems.CANDY_CANE;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.deathTime > 0) {
            return true;
        } else {
            if ((new Random()).nextBoolean() && attacker instanceof PlayerEntity && !attacker.world.isClient && target.hurtTime > 0)
                target.dropItem(AetherItems.CANDY_CANE, 1);
            stack.damage(1, attacker, null);
            return true;
        }
    }
}