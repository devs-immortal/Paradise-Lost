package com.aether.items.tools;

import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class AetherSword extends SwordItem implements IAetherTool {
    private final AetherTiers material;

    public AetherSword(AetherTiers material, float attackSpeed, int damageVsEntity, Settings settings) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float original = super.getMiningSpeedMultiplier(stack, state);
        if (material == AetherTiers.ZANITE) return original + this.calculateIncrease(stack);
        return original;
    }

    @Override
    public AetherTiers getTier() {
        return this.material;
    }
}