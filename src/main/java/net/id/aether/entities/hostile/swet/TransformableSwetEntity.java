package net.id.aether.entities.hostile.swet;

import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.tag.AetherBlockTags;
import net.id.aether.tag.AetherEntityTypeTags;
import net.id.aether.tag.AetherFluidTags;
import net.id.aether.tag.AetherItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public abstract class TransformableSwetEntity extends SwetEntity {
    public TransformableSwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
    }

    protected boolean changeType(EntityType<? extends SwetEntity> type) {
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
        suggestTypeChange(entity);
    }

    public boolean suggestTypeChange(BlockState state) {
        Block block = state.getBlock();
        if (AetherBlockTags.SWET_TRANSFORMERS_GOLDEN.contains(block)) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
        if (AetherBlockTags.SWET_TRANSFORMERS_BLUE.contains(block)) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        if (AetherBlockTags.SWET_TRANSFORMERS_PURPLE.contains(block)) {
            return this.changeType(AetherEntityTypes.PURPLE_SWET);
        }
//        if (AetherBlockTags.SWET_TRANSFORMERS_VERMILION.contains(block)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(FluidState state) {
        Fluid fluid = state.getFluid();
        if (AetherFluidTags.SWET_TRANSFORMERS_GOLDEN.contains(fluid)) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
        if (AetherFluidTags.SWET_TRANSFORMERS_BLUE.contains(fluid)) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        if (AetherFluidTags.SWET_TRANSFORMERS_PURPLE.contains(fluid)) {
            return this.changeType(AetherEntityTypes.PURPLE_SWET);
        }
//        if (AetherFluidTags.SWET_TRANSFORMERS_VERMILION.contains(fluid)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(Item item){
        if (AetherItemTags.SWET_TRANSFORMERS_BLUE.contains(item)) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        } else if (AetherItemTags.SWET_TRANSFORMERS_GOLDEN.contains(item)) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        } else if (AetherItemTags.SWET_TRANSFORMERS_PURPLE.contains(item)) {
            return this.changeType(AetherEntityTypes.PURPLE_SWET);
        }
//        } else if (AetherItemTags.SWET_TRANSFORMERS_VERMILION.contains(item)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(Entity entity) {
        if (entity instanceof ItemEntity itemEntity){
            if (suggestTypeChange(itemEntity.getStack().getItem())) {
                itemEntity.remove(RemovalReason.KILLED);
                return true;
            }
            return false;
        }
        if (entity.squaredDistanceTo(this) <= 1 && this.getSize() > 1) {
            EntityType<?> type = entity.getType();
            if (AetherEntityTypeTags.SWET_TRANSFORMERS_GOLDEN.contains(type)) {
                return this.changeType(AetherEntityTypes.GOLDEN_SWET);
            }
            if (AetherEntityTypeTags.SWET_TRANSFORMERS_BLUE.contains(type)) {
                return this.changeType(AetherEntityTypes.BLUE_SWET);
            }
            if (AetherEntityTypeTags.SWET_TRANSFORMERS_PURPLE.contains(type)) {
                return this.changeType(AetherEntityTypes.PURPLE_SWET);
            }
//            if (AetherEntityTypeTags.SWET_TRANSFORMERS_VERMILION.contains(type)) {
//                return this.changeType(AetherEntityTypes.VERMILION_SWET);
//            }
        }
        return false;
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        suggestTypeChange(state);
        suggestTypeChange(state.getFluidState());
    }
}
