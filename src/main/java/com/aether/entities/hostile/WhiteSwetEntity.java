package com.aether.entities.hostile;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.World;

public class WhiteSwetEntity extends SwetEntity{

    public WhiteSwetEntity(World world){
        super(AetherEntityTypes.WHITE_SWET, world);
        resize(2);
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
        if(entity.getVehicle().equals(this)){
            if (entity instanceof CockatriceEntity || entity instanceof AechorPlantEntity) {
                this.changeType(AetherEntityTypes.PURPLE_SWET);
            } // else ifs for other entities
        }
        super.onEntityCollision(entity);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        Block block = state.getBlock();
        if (state.getFluidState().getFluid() == Fluids.WATER) {
            this.changeType(AetherEntityTypes.BLUE_SWET);
        } else if (block == AetherBlocks.GOLDEN_OAK_LOG
                || block == AetherBlocks.GOLDEN_OAK_LEAVES
                || block == AetherBlocks.GOLDEN_OAK_SAPLING
                || block == AetherBlocks.STRIPPED_GOLDEN_OAK_LOG
                || block == AetherBlocks.POTTED_GOLDEN_OAK_SAPLING) {
            this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
    }
}
