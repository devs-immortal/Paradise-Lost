package net.id.aether.entities.block;

import net.fabricmc.fabric.api.util.NbtType;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.AetherEntityTypes;
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
        super(AetherEntityTypes.SLIDER, world, x, y, z, AetherBlocks.CRACKED_CARVED_STONE.getDefaultState());
        this.setDirection(direction);
    }

    @Override
    public void postTickMovement() {
        if (this.horizontalCollision) {
            this.setDirection(this.getDirection().getOpposite());
        }
        this.updateVelocity(0.01F, Vec3d.of(this.getDirection().getVector()));
        this.move(MovementType.SELF, this.getVelocity());
    }

    // temporary
    public void setBlockState(BlockState state) {
        this.blockState = state;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putString("Direction", this.getDirection().name());
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
            this.setDirection(dir);
        }
    }

    public void setDirection(Direction direction) {
        this.dataTracker.set(DIRECTION, direction);
    }

    public Direction getDirection() {
        return this.dataTracker.get(DIRECTION);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DIRECTION, Direction.NORTH);
    }
}
