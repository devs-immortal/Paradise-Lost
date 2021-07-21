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

public class WhiteSwetEntity extends TransformableSwetEntity{

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
        super.onEntityCollision(entity);
    }
}
