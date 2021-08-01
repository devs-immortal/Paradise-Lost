package com.aether.items.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class HolySwordItem extends SwordItem {
    public HolySwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        if (victim.isUndead()) {
            victim.damage(DamageSource.mob(attacker), 20);
            stack.damage(10, attacker, null);
        }
        return super.postHit(stack, victim, attacker);
    }
}
