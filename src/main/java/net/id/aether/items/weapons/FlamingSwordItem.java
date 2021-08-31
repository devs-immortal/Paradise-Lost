package net.id.aether.items.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class FlamingSwordItem extends SwordItem {
    public FlamingSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        victim.setOnFireFor(30);
        return super.postHit(stack, victim, attacker);
    }
}
