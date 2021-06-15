package com.aether.items.tools;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherAxe extends AxeItem implements IAetherTool {

    private final AetherTiers material;

    public AetherAxe(AetherTiers material, Settings settings, float damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float original = super.getMiningSpeedMultiplier(stack, state);
        if (this.getTier() == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamage() / 50;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult defaultResult = super.useOnBlock(context);
        return IAetherTool.super.useOnBlock(context, defaultResult);
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
}