package com.aether.items.tools;

import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherPickaxe extends PickaxeItem implements IAetherTool {

    private final AetherTiers material;

    public AetherPickaxe(AetherTiers material, Settings settings, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float original = super.getMiningSpeedMultiplier(stack, state);
        if (this.getItemMaterial() == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamage() / 50;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        if (this.getItemMaterial() == AetherTiers.Gravitite && FloatingBlockEntity.gravititeToolUsedOnBlock(context, this)) {
            PlayerEntity playerEntity = context.getPlayer();
            if (playerEntity != null) {
                context.getStack().damage(1, playerEntity, (p) -> {
                    p.sendToolBreakStatus(context.getHand());
                });
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }


    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}
