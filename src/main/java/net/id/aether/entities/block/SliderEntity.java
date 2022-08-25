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

    private static final int MAX_WINDED_TICKS = 60;
    private static final int MAX_PRIMING_TICKS = 10;
    private int windedTicks = 0;
    private int primingTicks = 0;
    private int moveTicks = 0;

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
            if (entity instanceof SliderEntity slider && !entity.noClip && this.collides) {
                this.alignToBlock();
                this.setState(State.WINDED);
                slider.alignToBlock();
                slider.setState(State.WINDED);
            }
            if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides) {
                entity.move(MovementType.SHULKER_BOX, this.getVelocity());
                entity.setOnGround(true);
            }
            this.postTickEntityCollision(entity);
        }
    }

    @Override
    public void postTickMovement() {
        switch (getState()) {
            case MOVING -> {
                this.updateVelocity(0.01F, Vec3d.of(this.getDirection().getVector()));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision) {
                    this.alignToBlock();
                    this.setState(moveTicks == 0 ? State.DORMANT : State.WINDED);
                    this.moveTicks = 0;
                    break;
                }
                moveTicks++;
            }
            case WINDED -> {
                if (this.world.isClient()) return;
                if (++windedTicks > MAX_WINDED_TICKS) {
                    windedTicks = 0;
                    this.setState(State.DORMANT);
                }
            }
            case DORMANT -> {
                if (this.world.isClient()) return;
                for (Entity entity : this.world.getOtherEntities(this, this.getBoundingBox().expand(5))
                        .stream()
                        .filter(entity -> entity instanceof PlayerEntity && this.squaredDistanceTo(entity) < 25)
                        .toList()
                ) {
                    this.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, entity.getPos());
                    this.setDirection(Direction.fromRotation(this.getYaw()));
                    this.setRotation(0, 0);
                    this.setState(State.PRIMING);
                }
            }
            case PRIMING -> {
                if (this.world.isClient()) return;
                if (++primingTicks > MAX_PRIMING_TICKS) {
                    primingTicks = 0;
                    this.setState(State.MOVING);
                }
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public void alignToBlock() {
        this.setVelocity(0, 0, 0);
        this.setPosition(Vec3d.of(this.getBlockPos()).add(0.5, 0, 0.5));
        this.setDirection(this.getDirection().getOpposite());
    }

    // temporary
    public void setBlockState(BlockState state) {
        this.blockState = state;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putString("Direction", this.getDirection().name());
        compound.putString("State", this.getState().name());
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
        if (compound.contains("State", NbtType.STRING)) {
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
            e.printStackTrace();
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
        MOVING,
        WINDED,
        DORMANT,
        PRIMING
    }
}
