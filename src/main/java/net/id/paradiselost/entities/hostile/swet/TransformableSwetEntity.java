package net.id.paradiselost.entities.hostile.swet;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.tag.ParadiseLostEntityTypeTags;
import net.id.paradiselost.tag.ParadiseLostFluidTags;
import net.id.paradiselost.tag.ParadiseLostItemTags;
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
        if (!getType().equals(type) && !isRemoved()) {
            SwetEntity swet = (convertTo(type, true));
            swet.setSize(getSize(), false);
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
        if (state.isIn(ParadiseLostBlockTags.SWET_TRANSFORMERS_GOLDEN)) {
            return changeType(ParadiseLostEntityTypes.GOLDEN_SWET);
        }
        if (state.isIn(ParadiseLostBlockTags.SWET_TRANSFORMERS_BLUE)) {
            return changeType(ParadiseLostEntityTypes.BLUE_SWET);
        }
        if (state.isIn(ParadiseLostBlockTags.SWET_TRANSFORMERS_PURPLE)) {
            return changeType(ParadiseLostEntityTypes.PURPLE_SWET);
        }
//        if (state.isIn(ParadiseLostBlockTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(ParadiseLostEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(FluidState state) {
        if (state.isIn(ParadiseLostFluidTags.SWET_TRANSFORMERS_GOLDEN)) {
            return changeType(ParadiseLostEntityTypes.GOLDEN_SWET);
        }
        if (state.isIn(ParadiseLostFluidTags.SWET_TRANSFORMERS_BLUE)) {
            return changeType(ParadiseLostEntityTypes.BLUE_SWET);
        }
        if (state.isIn(ParadiseLostFluidTags.SWET_TRANSFORMERS_PURPLE)) {
            return changeType(ParadiseLostEntityTypes.PURPLE_SWET);
        }
//        if (state.isIn(ParadiseLostFluidTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(ParadiseLostEntityTypes.VERMILION_SWET);
//        }
        return false;
    }

    public boolean suggestTypeChange(ItemStack state) {
        if (state.isIn(ParadiseLostItemTags.SWET_TRANSFORMERS_GOLDEN)) {
            return changeType(ParadiseLostEntityTypes.GOLDEN_SWET);
        }
        if (state.isIn(ParadiseLostItemTags.SWET_TRANSFORMERS_BLUE)) {
            return changeType(ParadiseLostEntityTypes.BLUE_SWET);
        }
        if (state.isIn(ParadiseLostItemTags.SWET_TRANSFORMERS_PURPLE)) {
            return changeType(ParadiseLostEntityTypes.PURPLE_SWET);
        }
//        if (state.isIn(ParadiseLostItemTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(ParadiseLostEntityTypes.VERMILION_SWET);
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
        if (entity.squaredDistanceTo(this) <= 1 && getSize() > 1) {
            EntityType<?> state = entity.getType();
            if (state.isIn(ParadiseLostEntityTypeTags.SWET_TRANSFORMERS_GOLDEN)) {
                return changeType(ParadiseLostEntityTypes.GOLDEN_SWET);
            }
            if (state.isIn(ParadiseLostEntityTypeTags.SWET_TRANSFORMERS_BLUE)) {
                return changeType(ParadiseLostEntityTypes.BLUE_SWET);
            }
            if (state.isIn(ParadiseLostEntityTypeTags.SWET_TRANSFORMERS_PURPLE)) {
                return changeType(ParadiseLostEntityTypes.PURPLE_SWET);
            }
//        if (state.isIn(ParadiseLostEntityTypeTags.SWET_TRANSFORMERS_VERMILION)) {
//            return this.changeType(ParadiseLostEntityTypes.VERMILION_SWET);
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
