package com.aether.items.tools;

import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AetherShovel extends ShovelItem implements IAetherTool {
    private final AetherTiers material;
    public float[] zaniteHarvestLevels = new float[]{2F, 4F, 6F, 8F, 12F};

    public AetherShovel(AetherTiers material, Item.Properties settings, float damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float original = super.getDestroySpeed(stack, state);
        if (this.getItemMaterial() == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamageValue() / 50;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult superUsage = super.useOn(context);
        if (superUsage.equals(InteractionResult.PASS)) {
            if (this.getItemMaterial() == AetherTiers.Gravitite && FloatingBlockEntity.gravititeToolUsedOnBlock(context, this)) {
                return InteractionResult.SUCCESS;
            }
        }
        return superUsage;
    }

    @Override
    public boolean mineBlock(ItemStack stackIn, Level worldIn, BlockState stateIn, BlockPos posIn, LivingEntity entityIn) {
        if (!worldIn.isClientSide && this.getItemMaterial() == AetherTiers.Holystone && worldIn.getRandom().nextInt(100) <= 5)
            worldIn.addFreshEntity(new ItemEntity(worldIn, posIn.getX(), posIn.getY(), posIn.getZ(), new ItemStack(AetherItems.AMBROSIUM_SHARD)));
        return super.mineBlock(stackIn, worldIn, stateIn, posIn, entityIn);
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}