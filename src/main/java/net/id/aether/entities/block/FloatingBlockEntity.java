package net.id.aether.entities.block;

import net.id.aether.api.FloatingBlockHelper;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.util.PostTickEntity;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FloatingBlockEntity extends BlockLikeEntity implements PostTickEntity {
    private Supplier<Boolean> dropState = () -> false;
    private boolean dropping = false;
    private BiConsumer<Double, Boolean> onEndFloating;

    public FloatingBlockEntity(EntityType<? extends FloatingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    public FloatingBlockEntity(World world, BlockState floatingBlockState, Vec3d pos) {
        super(AetherEntityTypes.FLOATING_BLOCK, world, floatingBlockState, pos);
        this.setOnEndFloating((impact, landed) -> {});
        this.setDropState(() -> FloatingBlockHelper.DEFAULT_DROP_STATE.apply(this));
    }

    public FloatingBlockEntity(World world, BlockPos pos, BlockState floatingBlockState, boolean partOfSet) {
        this(world, floatingBlockState, Vec3d.ofCenter(pos));
        this.partOfSet = partOfSet;
    }

    public void postTick() {
        super.postTick();
        if (this.isRemoved()) return;

        double impact = this.getVelocity().length();

        if (!this.hasNoGravity()) {
            if (!isDropping() && !shouldBeginDropping()) {
                if (isFastFloater()) {
                    this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));
                } else {
                    this.setVelocity(this.getVelocity().add(0.0D, 0.03D, 0.0D));
                }
            } else {
                this.setDropping(true);
                this.setVelocity(this.getVelocity().add(0.0D, -0.03D, 0.0D));
            }
        }

        this.move(MovementType.SELF, this.getVelocity());

        if (!this.world.isClient) {
            BlockPos blockPos = this.getBlockPos();
            boolean isConcrete = this.blockState.getBlock() instanceof ConcretePowderBlock;
            boolean shouldSolidify = isConcrete && this.world.getFluidState(blockPos).isIn(FluidTags.WATER);
            double speed = this.getVelocity().lengthSquared();

            if (isConcrete && speed > 1.0D) {
                BlockHitResult blockHitResult = this.world
                        .raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ),
                                new Vec3d(this.getX(), this.getY(), this.getZ()), RaycastContext.ShapeType.COLLIDER,
                                RaycastContext.FluidHandling.SOURCE_ONLY, this));

                if (blockHitResult.getType() != HitResult.Type.MISS
                        && this.world.getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                    blockPos = blockHitResult.getBlockPos();
                    shouldSolidify = true;
                }
            }

            if ((!this.verticalCollision || this.onGround) && !shouldSolidify) {
                if (!this.world.isClient && (blockPos.getY() < this.world.getBottomY() || blockPos.getY() > this.world.getTopY())) {
                    if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                        Block.dropStacks(this.blockState, this.world, this.getBlockPos());
                    }
                    this.discard();
                }
            } else {
                this.tryLand(impact);
            }
        }

        if ((this.isDropping() || this.getVelocity().getY() == 0) && this.isOnGround()) {
            this.tryLand(impact);
        }

        // Drag, for terminal velocity.
        this.setVelocity(this.getVelocity().multiply(0.98D));
    }

    @Override
    public void alignWith(BlockLikeEntity other, Vec3i offset) {
        super.alignWith(other, offset);
        if (other instanceof FloatingBlockEntity fbe) {
            this.setDropping(fbe.isDropping());
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("Dropping", this.isDropping());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("Dropping", 99)) this.setDropping(compound.getBoolean("Dropping"));
    }

    public Supplier<Boolean> getDropState() {
        return dropState;
    }

    public void setDropState(Supplier<Boolean> supplier) {
        this.dropState = supplier;
    }

    public boolean shouldBeginDropping() {
        return this.getDropState().get();
    }

    public boolean isDropping() {
        return this.dropping;
    }

    public void setDropping(boolean dropping) {
        this.dropping = dropping;
    }

    public boolean isFastFloater() {
        return this.blockState.isIn(AetherBlockTags.FAST_FLOATERS) && !this.partOfSet;
    }

    public BiConsumer<Double, Boolean> getOnEndFloating() {
        return this.onEndFloating;
    }

    public void setOnEndFloating(BiConsumer<Double, Boolean> consumer) {
        this.onEndFloating = consumer;
    }

    @Override
    public boolean tryLand(double impact) {
        if (super.tryLand(impact)) {
            this.getOnEndFloating().accept(impact, true);
            return true;
        }
        return false;
    }

    @Override
    public void crashLand(double impact) {
        super.crashLand(impact);
        this.getOnEndFloating().accept(impact, false);
    }

}