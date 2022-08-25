package net.id.aether.entities.block;

import net.fabricmc.fabric.api.util.NbtType;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.dungeon.SliderBlock;
import net.id.aether.entities.AetherEntityTypes;
import net.id.incubus_core.blocklikeentities.api.BlockLikeEntity;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
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
    private int slideTicks = 0;

    public SliderEntity(EntityType<? extends SliderEntity> entityType, World world) {
        super(entityType, world);
        this.blockState = AetherBlocks.SLIDER_BLOCK.getDefaultState();
    }

    public SliderEntity(EntityType<? extends SliderEntity> entityType, World world, BlockPos pos) {
        super(entityType, world, pos, AetherBlocks.SLIDER_BLOCK.getDefaultState(), false);
        this.setDirection(Direction.NORTH);
        this.setState(State.DORMANT);
    }

    public SliderEntity(World world, BlockPos pos) {
        this(AetherEntityTypes.SLIDER, world, pos);
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
            case SLIDING -> tickSlide();
            case WINDED -> tickWinded(MAX_WINDED_TICKS);
            case DORMANT -> tickDormant();
            case PRIMING -> tickPriming(MAX_PRIMING_TICKS);
        }
    }

    protected void tickPriming(int maxPrimingTicks) {
        if (++primingTicks > maxPrimingTicks) {
            primingTicks = 0;
            this.setState(State.SLIDING);
        }
    }

    protected void tickDormant() {
        int radius = getPlayerDetectionRadius();

        for (Entity entity : this.world.getOtherEntities(this, this.getBoundingBox().expand(radius))
                .stream()
                .filter(entity -> entity instanceof PlayerEntity && this.squaredDistanceTo(entity) < radius * radius)
                .toList()
        ) {
            Direction dir = this.getNextDirection(entity);
            if (dir == null) continue;
            this.setDirection(dir);
            this.setState(State.PRIMING);
        }
    }

    protected void tickWinded(int windedTimeout) {
        if (++windedTicks > windedTimeout) {
            windedTicks = 0;
            this.setState(State.DORMANT);
        }
    }

    protected void tickSlide() {
        this.updateVelocity(this.getMoveSpeed(), Vec3d.of(this.getDirection().getVector()));
        this.move(MovementType.SELF, this.getVelocity());
        if (this.horizontalCollision || this.verticalCollision) {
            this.alignToBlock();
            this.setState(slideTicks == 0 ? State.DORMANT : State.WINDED);
            this.slideTicks = 0;
            return;
        }
        slideTicks++;
    }

    // TODO 0.2.0: Override shouldCease()

    public void alignToBlock() {
        this.setVelocity(0, 0, 0);
        this.setPosition(Vec3d.of(this.getBlockPos()).add(0.5, 0, 0.5));
        this.setDirection(this.getDirection().getOpposite());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putString("Direction", this.getDirection().getName());
        compound.putString("State", this.getState().asString());
        compound.putInt("SlideTime", this.slideTicks);
        compound.putInt("WindedTime", this.windedTicks);
        compound.putInt("PrimeTime", this.primingTicks);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("Direction", NbtType.STRING)) {
            Direction dir = Direction.byName(compound.getString("Direction"));
            this.setDirection(dir != null ? dir : Direction.NORTH);
        }
        if (compound.contains("State", NbtType.STRING)) {
            this.setState(compound.getString("State"));
        }
        this.slideTicks = compound.getInt("SlideTime");
        this.windedTicks = compound.getInt("WindedTime");
        this.primingTicks = compound.getInt("PrimeTime");
    }

    protected float getMoveSpeed() {
        return 0.01F;
    }

    protected int getPlayerDetectionRadius() {
        return 5;
    }

    protected Direction getNextDirection(Entity entity) {
        Vec3d displacement = entity.getPos().subtract(this.getPos());
        return Direction.getFacing(displacement.x, 0D, displacement.z);
    }

    public void setDirection(Direction direction) {
        if (!this.world.isClient()) this.dataTracker.set(DIRECTION, direction);
    }

    public Direction getDirection() {
        return this.dataTracker.get(DIRECTION);
    }

    public void setState(State state) {
        this.setState(state.asString());
    }

    public void setState(String state) {
        // Sanity check
        if (!this.blockState.getProperties().contains(SliderBlock.STATE)) {
            this.blockState = AetherBlocks.SLIDER_BLOCK.getDefaultState();
        }
        this.blockState = this.blockState.with(SliderBlock.STATE, State.byName(state));
        this.dataTracker.set(STATE, state);
    }

    public State getState() {
        var state = State.byName(this.dataTracker.get(STATE));
        return state != null ? state : State.DORMANT;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DIRECTION, Direction.NORTH);
        this.dataTracker.startTracking(STATE, State.DORMANT.asString());
    }

    public enum State implements StringIdentifiable {
        DORMANT("dormant"),
        PRIMING("priming"),
        SLIDING("moving"),
        WINDED("winded");

        private static final StringIdentifiable.Codec<State> CODEC = StringIdentifiable.createCodec(State::values);
        private final String name;

        State(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static State byName(String id) {
            return CODEC.byId(id);
        }
    }
}
