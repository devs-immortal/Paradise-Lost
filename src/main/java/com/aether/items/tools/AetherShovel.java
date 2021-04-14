package com.aether.items.tools;

import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItemSettings;
import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import com.aether.util.item.AetherRarity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherShovel extends ShovelItem implements IAetherTool {
    private final AetherTiers material;
    public float[] zaniteHarvestLevels = new float[]{2F, 4F, 6F, 8F, 12F};

    public AetherShovel(AetherTiers material, Item.Settings settings, float damageVsEntity, float attackSpeed) {
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
        ActionResult superUsage = super.useOnBlock(context);
        if (superUsage.equals(ActionResult.PASS)) {
            if (this.getItemMaterial() == AetherTiers.Gravitite && FloatingBlockEntity.gravititeToolUsedOnBlock(context, this)) {
                PlayerEntity playerEntity = context.getPlayer();
                if (playerEntity != null)
                    context.getStack().damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));
                return ActionResult.SUCCESS;
            }
        }
        return superUsage;
    }

    @Override
    public boolean postMine(ItemStack stackIn, World worldIn, BlockState stateIn, BlockPos posIn, LivingEntity entityIn) {
        if (!worldIn.isClient && this.getItemMaterial() == AetherTiers.Holystone && worldIn.getRandom().nextInt(100) <= 5)
            worldIn.spawnEntity(new ItemEntity(worldIn, posIn.getX(), posIn.getY(), posIn.getZ(), new ItemStack(AetherItems.AMBROSIUM_SHARD)));
        return super.postMine(stackIn, worldIn, stateIn, posIn, entityIn);
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}
