package net.id.aether.entities.block;

import net.fabricmc.fabric.api.util.NbtType;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.AetherEntityTypes;
import net.id.incubus_core.blocklikeentities.api.BlockLikeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SliderEntity extends BlockLikeEntity {
    private static final TrackedData<Direction> DIRECTION = DataTracker.registerData(SliderEntity.class, TrackedDataHandlerRegistry.FACING);
    private static final TrackedData<String> STATE = DataTracker.registerData(SliderEntity.class, TrackedDataHandlerRegistry.STRING);

    private int windedTicks = 60;

    public SliderEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public SliderEntity(World world, double x, double y, double z, Direction direction) {
        super(AetherEntityTypes.SLIDER, world, x, y, z, AetherBlocks.CRACKED_CARVED_STONE.getDefaultState());
        this.setDirection(direction);
    }

    @Override
    public void postTickMoveEntities() {
        if (FallingBlock.canFallThrough(this.blockState)) return;

        List<Entity> otherEntities = this.world.getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 0.5, 0)));
        for (Entity entity : otherEntities) {
            if (entity instanceof SliderEntity slider && !entity.noClip && this.collides()) {
                this.setState(State.HITTING_WALL);
                slider.setState(State.HITTING_WALL);
            }
            if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides()) {
                entity.move(MovementType.SHULKER_BOX, this.getVelocity());
                entity.setOnGround(true);
            }
            this.postTickEntityCollision(entity);
        }
    }

    @Override
    public void postTickMovement() {
        // todo 2.0.0 Clean up, split into separate methods
        switch (getState()) {
            case MOVING -> {
                this.updateVelocity(0.01F, Vec3d.of(this.getDirection().getVector()));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision) {
                    this.setState(State.HITTING_WALL);
                }
            }
            case HITTING_WALL -> {
                this.setVelocity(0, 0, 0);
                this.setPosition(Vec3d.of(this.getBlockPos()).add(0.5, 0, 0.5));
                this.setDirection(this.getDirection().getOpposite());
                this.setState(State.WINDED);
            }
            case WINDED -> {
                if (--windedTicks <= 0) {
                    windedTicks = 60;
                    handleAggravation();
                }
            }
            case DORMANT -> handleAggravation();
        }
    }

    // fixme 2.0.0 The result of this is sometimes different on the client vs. server.
    private void handleAggravation() {
        this.setState(State.DORMANT);
        this.world.getOtherEntities(this, this.getBoundingBox().expand(5)).forEach(entity -> {
            if (entity instanceof PlayerEntity player) {
                this.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, player.getPos());
                this.setDirection(Direction.fromRotation(this.getYaw()));
                this.setRotation(0, 0);
                this.setState(State.MOVING);
            }
        });
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
        if (compound.contains("State", NbtType.BYTE)) {
            this.setState(compound.getString("State"));
        }
    }

    public void setDirection(Direction direction) {
        this.dataTracker.set(DIRECTION, direction);
    }

    public Direction getDirection() {
        return this.dataTracker.get(DIRECTION);
    }

    public void setState(State state) {
        this.setState(state.name());
    }

    public void setState(String state) {
        this.dataTracker.set(STATE, state);
    }

    public State getState() {
        try {
            return State.valueOf(this.dataTracker.get(STATE));
        } catch (IllegalArgumentException e) {
            return State.DORMANT;
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DIRECTION, Direction.NORTH);
        this.dataTracker.startTracking(STATE, State.DORMANT.name());
    }

    private enum State {
        DORMANT,
        MOVING,
        HITTING_WALL,
        WINDED
    }
}
