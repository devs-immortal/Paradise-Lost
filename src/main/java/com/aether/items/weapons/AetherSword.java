package com.aether.items.weapons;

import com.aether.items.utils.AetherTiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.state.BlockState;

public class AetherSword extends SwordItem {
    private final AetherTiers material;

    public AetherSword(AetherTiers material, float attackSpeed, int damageVsEntity, Properties settings) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float original = super.getDestroySpeed(stack, state);
        if (material == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamageValue() / 50;
    }
}