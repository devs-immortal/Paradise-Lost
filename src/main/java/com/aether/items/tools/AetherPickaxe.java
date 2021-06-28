package com.aether.items.tools;

import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

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
        return defaultResult != ActionResult.PASS ? defaultResult : IAetherTool.super.useOnBlock(context, defaultResult);
    }

    @Override
    public AetherTiers getTier() {
        return this.material;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand){
        return IAetherTool.super.useOnEntity(stack, player, entity, hand);
    }
}