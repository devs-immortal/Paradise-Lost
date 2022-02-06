package net.id.aether.entities.hostile.swet;

import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.tag.AetherBlockTags;
import net.id.aether.tag.AetherEntityTypeTags;
import net.id.aether.tag.AetherFluidTags;
import net.id.aether.tag.AetherItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class TransformableSwetEntity extends SwetEntity {
    public TransformableSwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
    }

    protected boolean changeType(EntityType<? extends SwetEntity> type) {
        if (!this.getType().equals(type) && !this.isRemoved()) {
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
        if (state.isIn(AetherBlockTags.SWET_TRANSFORMERS_GOLDEN)) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
        if (state.isIn(AetherBlockTags.SWET_TRANSFORMERS_BLUE)) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        if (state.isIn(AetherBlockTags.SWET_TRANSFORMERS_PURPLE)) {
            return this.changeType(AetherEntityTypes.PURPLE_SWET);
        }
//        if (state.isIn(AetherBlockTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(FluidState state) {
        if (state.isIn(AetherFluidTags.SWET_TRANSFORMERS_GOLDEN)) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
        if (state.isIn(AetherFluidTags.SWET_TRANSFORMERS_BLUE)) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        if (state.isIn(AetherFluidTags.SWET_TRANSFORMERS_PURPLE)) {
            return this.changeType(AetherEntityTypes.PURPLE_SWET);
        }
//        if (state.isIn(AetherFluidTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(ItemStack state) {
        if (state.isIn(AetherItemTags.SWET_TRANSFORMERS_GOLDEN)) {
            return this.changeType(AetherEntityTypes.GOLDEN_SWET);
        }
        if (state.isIn(AetherItemTags.SWET_TRANSFORMERS_BLUE)) {
            return this.changeType(AetherEntityTypes.BLUE_SWET);
        }
        if (state.isIn(AetherItemTags.SWET_TRANSFORMERS_PURPLE)) {
            return this.changeType(AetherEntityTypes.PURPLE_SWET);
        }
//        if (state.isIn(AetherItemTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            if (suggestTypeChange(itemEntity.getStack())) {
                itemEntity.remove(RemovalReason.KILLED);
                return true;
            }
            return false;
        }
        if (entity.squaredDistanceTo(this) <= 1 && this.getSize() > 1) {
            EntityType<?> state = entity.getType();
            if (state.isIn(AetherEntityTypeTags.SWET_TRANSFORMERS_GOLDEN)) {
                return this.changeType(AetherEntityTypes.GOLDEN_SWET);
            }
            if (state.isIn(AetherEntityTypeTags.SWET_TRANSFORMERS_BLUE)) {
                return this.changeType(AetherEntityTypes.BLUE_SWET);
            }
            if (state.isIn(AetherEntityTypeTags.SWET_TRANSFORMERS_PURPLE)) {
                return this.changeType(AetherEntityTypes.PURPLE_SWET);
            }
//        if (state.isIn(AetherEntityTypeTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(AetherEntityTypes.VERMILION_SWET);
//        }
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
