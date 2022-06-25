package net.id.paradiselost.entities.block;

import net.fabricmc.fabric.api.util.NbtType;
import net.id.incubus_core.blocklikeentities.api.BlockLikeEntity;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SliderEntity extends BlockLikeEntity {
    private static final TrackedData<Direction> DIRECTION = DataTracker.registerData(SliderEntity.class, TrackedDataHandlerRegistry.FACING);
    
    public SliderEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public SliderEntity(World world, double x, double y, double z, Direction direction) {
        super(ParadiseLostEntityTypes.SLIDER, world, x, y, z, ParadiseLostBlocks.CRACKED_CARVED_STONE.getDefaultState());
        setDirection(direction);
    }
    
    @Override
    public void postTickMovement() {
        if (horizontalCollision) {
            setDirection(getDirection().getOpposite());
        }
        updateVelocity(0.01F, Vec3d.of(getDirection().getVector()));
        move(MovementType.SELF, getVelocity());
    }
    
    // temporary
    public void setBlockState(BlockState state) {
        blockState = state;
    }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putString("Direction", getDirection().name());
    }
    
    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("Direction", NbtType.STRING)) {
            Direction dir;
            try {
                dir = Direction.valueOf(compound.getString("Direction"));
            } catch (IllegalArgumentException e) {
                dir = Direction.NORTH;
            }
            setDirection(dir);
        }
    }
    
    public void setDirection(Direction direction) {
        dataTracker.set(DIRECTION, direction);
    }
    
    public Direction getDirection() {
        return dataTracker.get(DIRECTION);
    }
    
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DIRECTION, Direction.NORTH);
    }
}
