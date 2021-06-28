package com.aether.items.tools;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherShovel extends ShovelItem implements IAetherTool {
    private final AetherTiers material;

    public AetherShovel(AetherTiers material, Item.Settings settings, float damageVsEntity, float attackSpeed) {
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
    public boolean postMine(ItemStack stackIn, World worldIn, BlockState stateIn, BlockPos posIn, LivingEntity entityIn) {
        if (!worldIn.isClient && this.getTier() == AetherTiers.Holystone && worldIn.getRandom().nextInt(100) <= 5)
            worldIn.spawnEntity(new ItemEntity(worldIn, posIn.getX(), posIn.getY(), posIn.getZ(), new ItemStack(AetherItems.AMBROSIUM_SHARD)));
        return super.postMine(stackIn, worldIn, stateIn, posIn, entityIn);
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