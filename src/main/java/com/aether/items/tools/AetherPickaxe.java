package com.aether.items.tools;

import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;

public class AetherPickaxe extends PickaxeItem implements IAetherTool {

    private final AetherTiers material;

    public AetherPickaxe(AetherTiers material, Settings settings, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float original = super.getMiningSpeedMultiplier(stack, state);
        if (this.getTier() == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult defaultResult = super.useOnBlock(context);
        return IAetherTool.super.useOnBlock(context, defaultResult);
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamage() / 50;
    }


    @Override
    public AetherTiers getTier() {
        return this.material;
    }
}