package net.id.paradise_lost.entity.block;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.Tag;
import net.id.paradise_lost.api.BlockLikeEntity;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SliderEntity extends BlockLikeEntity {
    private static final EntityDataAccessor<Direction> DIRECTION = SynchedEntityData.defineId(SliderEntity.class, EntityDataSerializers.DIRECTION);

    public SliderEntity(EntityType<? extends BlockLikeEntity> entityType, Level world) {
        super(entityType, world);
    }

    public SliderEntity(Level world, double x, double y, double z, Direction direction) {
        super(EntityRegistry.SLIDER.get(), world, x, y, z, BlockRegistry.GOLDEN_AMBER_TILE.get().defaultBlockState());
        this.setDirection(direction);
    }

    @Override
    public void postTickMovement() {
        if (this.horizontalCollision) {
            this.setDirection(this.getDirection().getOpposite());
        }
        this.moveRelative(0.01F, Vec3.atLowerCornerOf(this.getDirection().getUnitVec3i()));
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    public void setBlockState(BlockState state) {
        this.blockState = state;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Direction", this.getDirection().name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Direction", Tag.TAG_STRING)) {
            Direction dir;
            try {
                dir = Direction.valueOf(compound.getString("Direction"));
            } catch (IllegalArgumentException e) {
                dir = Direction.NORTH;
            }
            this.setDirection(dir);
        }
    }

    public void setDirection(Direction direction) {
        this.entityData.set(DIRECTION, direction);
    }

    public Direction getDirection() {
        return this.entityData.get(DIRECTION);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DIRECTION, Direction.NORTH);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }
}
