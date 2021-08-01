package com.aether.items.weapons;

import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class CandyCaneSwordItem extends SwordItem {
    public CandyCaneSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.world.isClient()
                && target.hurtTime > 0
                && !target.isDead()
                && attacker instanceof PlayerEntity
                && target.world.getRandom().nextBoolean()) {

            target.dropItem(AetherItems.CANDY_CANE, 1);
        }

        return super.postHit(stack, target, attacker);
    }
}