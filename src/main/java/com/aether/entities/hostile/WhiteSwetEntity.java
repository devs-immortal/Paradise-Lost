package com.aether.entities.hostile;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.World;

public class WhiteSwetEntity extends SwetEntity{

    public WhiteSwetEntity(World world){
        super(AetherEntityTypes.WHITE_SWET, world);
        setSize(2, true);
    }

    @Override
    protected void onEntityCollision(Entity entity){
        if (getSize() > 1 && entity instanceof LivingEntity livingEntity) {
            StatusEffectInstance[] effects = livingEntity.getStatusEffects().toArray(new StatusEffectInstance[0]);
            for (StatusEffectInstance effect : effects) {
                this.setStatusEffect(effect, livingEntity);
                livingEntity.removeStatusEffect(effect.getEffectType());
            }
        }
        if(entity.squaredDistanceTo(this) <= 1 && this.getSize() > 1){
            if (entity instanceof CockatriceEntity || entity instanceof AechorPlantEntity) {
                this.changeType(AetherEntityTypes.PURPLE_SWET);
            }
            if (entity instanceof ItemEntity item){
                if (item.getStack().getItem() == AetherItems.BLUEBERRY){
                    this.changeType(AetherEntityTypes.BLUE_SWET);
                }
                item.remove(RemovalReason.KILLED);
            }
        }
        super.onEntityCollision(entity);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if (state.getFluidState().getFluid() == Fluids.WATER) {
            this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        // TODO: relieve lag by doing this in each block's respective class
        world.getStatesInBox(this.getBoundingBox().expand(0.2)).forEach((blockState)->{
            Block block = blockState.getBlock();
            if (block == AetherBlocks.GOLDEN_OAK_LOG ||
                    block == AetherBlocks.GOLDEN_OAK_LEAVES ||
                    block == AetherBlocks.GOLDEN_OAK_SAPLING ||
                    block == AetherBlocks.STRIPPED_GOLDEN_OAK_LOG ||
                    block == AetherBlocks.POTTED_GOLDEN_OAK_SAPLING) {
                this.changeType(AetherEntityTypes.GOLDEN_SWET);
            }
        });
    }

    @Override
    public void changeType(EntityType<? extends SwetEntity> type) {
        super.changeType(type);
    }

}
