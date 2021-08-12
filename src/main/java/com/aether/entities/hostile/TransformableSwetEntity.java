package com.aether.entities.hostile;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TransformableSwetEntity extends SwetEntity {
    public TransformableSwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
    }

    protected boolean changeType(EntityType<? extends SwetEntity> type){
        if(!this.getType().equals(type) && !this.isRemoved()) {
            SwetEntity swet = (this.convertTo(type, true));
            swet.setSize(this.getSize(), false);
            world.spawnEntity(swet);
            return true;
        }
        return false;
    }

    @Override
    protected void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if(entity.squaredDistanceTo(this) <= 1 && this.getSize() > 1){
            if (entity instanceof CockatriceEntity || entity instanceof AechorPlantEntity) {
                this.changeType(AetherEntityTypes.PURPLE_SWET);
            }
            if (entity instanceof ItemEntity item){
                if (item.getStack().getItem() == AetherItems.BLUEBERRY){
                    this.changeType(AetherEntityTypes.BLUE_SWET);
                    item.remove(RemovalReason.KILLED);
                }
                if (item.getStack().getItem() == AetherItems.GOLDEN_AMBER){
                    this.changeType(AetherEntityTypes.GOLDEN_SWET);
                    item.remove(RemovalReason.KILLED);
                }
            }
        }
    }

    public boolean suggestTypeChange(World world, BlockPos blockPos, BlockState state){
        Block block = state.getBlock();
        if (block == AetherBlocks.GOLDEN_OAK_LOG ||
                block == AetherBlocks.GOLDEN_OAK_LEAVES ||
                block == AetherBlocks.GOLDEN_OAK_SAPLING ||
                block == AetherBlocks.STRIPPED_GOLDEN_OAK_LOG ||
                block == AetherBlocks.POTTED_GOLDEN_OAK_SAPLING) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
        if (state.getBlock() == AetherBlocks.BLUEBERRY_BUSH) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        return false;
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        if (state.getFluidState().getFluid() == Fluids.WATER ||
                state.getFluidState().getFluid() == Fluids.FLOWING_WATER) {
            this.changeType(AetherEntityTypes.BLUE_SWET);
        }
    }
}
