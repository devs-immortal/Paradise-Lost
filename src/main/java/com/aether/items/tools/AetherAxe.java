package com.aether.items.tools;

import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherAxe extends AxeItem implements IAetherTool {

    private final AetherTiers material;
    public float[] zaniteHarvestLevels = new float[]{2F, 4F, 6F, 8F, 12F};

    public AetherAxe(AetherTiers material, float damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, new Settings().group(AetherItemGroups.TOOLS));
        this.material = material;
    }

    public AetherAxe(AetherTiers material, Rarity rarity, float damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, new Settings().group(AetherItemGroups.TOOLS).rarity(rarity));
        this.material = material;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float original = super.getMiningSpeedMultiplier(stack, state);
        if (this.getItemMaterial() == AetherTiers.ZANITE) return this.calculateIncrease(stack, original);
        return original;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        BlockState iblockstate = world.getBlockState(blockpos);

        if (this.getItemMaterial() == AetherTiers.GRAVITITE && this.getMiningSpeedMultiplier(context.getStack(), iblockstate) == this.miningSpeed) {
            if (world.isAir(blockpos.up()) && !world.isClient) {
                //TODO: Spawn floating block
            } else {
                return ActionResult.PASS;
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean postMine(ItemStack stackIn, World worldIn, BlockState stateIn, BlockPos posIn, LivingEntity entityIn) {
        if (!worldIn.isClient && this.getItemMaterial() == AetherTiers.HOLYSTONE && worldIn.getRandom().nextInt(100) <= 5)
            worldIn.spawnEntity(new ItemEntity(worldIn, posIn.getX(), posIn.getY(), posIn.getZ(), new ItemStack(AetherItems.AMBROSIUM_SHARD)));
        return super.postMine(stackIn, worldIn, stateIn, posIn, entityIn);
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }

    private float calculateIncrease(ItemStack tool, float original) {
        boolean AllowedCalculations = !(original != 4.0F);
        int current = tool.getDamage();

        if (AllowedCalculations) {
            if (this.isBetween(tool.getDamage(), current, tool.getDamage() - 50))
                return this.zaniteHarvestLevels[4];
            else if (this.isBetween(tool.getDamage() - 51, current, tool.getDamage() - 110))
                return this.zaniteHarvestLevels[3];
            else if (this.isBetween(tool.getDamage() - 111, current, tool.getDamage() - 200))
                return this.zaniteHarvestLevels[2];
            else if (this.isBetween(tool.getDamage() - 201, current, tool.getDamage() - 239))
                return this.zaniteHarvestLevels[1];
            return this.zaniteHarvestLevels[0];
        }
        return 1.0F;
    }

    private boolean isBetween(int max, int origin, int min) {
        return origin <= max && origin >= min;
    }
}