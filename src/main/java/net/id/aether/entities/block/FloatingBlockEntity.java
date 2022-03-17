package net.id.aether.entities.block;

import net.id.aether.api.FloatingBlockHelper;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FloatingBlockEntity extends BlockLikeEntity {
    private Supplier<Boolean> dropState = () -> FloatingBlockHelper.DEFAULT_DROP_STATE.apply(this);
    private boolean dropping = false;
    private BiConsumer<Double, Boolean> onEndFloating = (f, b) -> {};
    public double lastYVelocity = 0;

    public FloatingBlockEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FloatingBlockEntity(World world, double x, double y, double z, BlockState floatingBlockState) {
        super(AetherEntityTypes.FLOATING_BLOCK, world, x, y, z, floatingBlockState);
        this.setHurtEntities(floatingBlockState.isIn(AetherBlockTags.HURTABLE_FLOATERS));
    }

    public FloatingBlockEntity(World world, BlockPos pos, BlockState floatingBlockState, boolean partOfSet) {
        super(AetherEntityTypes.FLOATING_BLOCK, world, pos, floatingBlockState, partOfSet);
        this.setHurtEntities(floatingBlockState.isIn(AetherBlockTags.HURTABLE_FLOATERS));
    }

    @Override
    public void postTickEntityCollision(Entity entity) {
        super.postTickEntityCollision(entity);
        if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides()) {
            entity.fallDistance = 0F;
        }
    }

    @Override
    public void postTickMoveEntities() {
        super.postTickMoveEntities();

        List<Entity> otherEntities = this.world.getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 3 * (this.prevY - this.getY()), 0)));
        for (Entity entity : otherEntities) {
            if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides()) {
                entity.setPosition(entity.getX(), this.getBoundingBox().maxY, entity.getZ());
            }
        }
    }

    @Override
    public void postTickMovement() {
        this.lastYVelocity = this.getVelocity().y;

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
    }

    @Override
    public boolean shouldCease() {
        return super.shouldCease()
                || (this.isOnGround() && (this.isDropping() || this.getVelocity().getY() == 0)
                || (this.verticalCollision && !this.onGround));
    }

    public Supplier<Boolean> getDropState() {
        return dropState;
    }

    public void setDropState(Supplier<Boolean> supplier) {
        dropState = supplier;
    }

    public boolean shouldBeginDropping() {
        return getDropState().get();
    }

    public boolean isDropping() {
        return dropping;
    }

    public void setDropping(boolean dropping) {
        this.dropping = dropping;
    }

    public BiConsumer<Double, Boolean> getOnEndFloating() {
        return this.onEndFloating;
    }

    // It's fine if this isn't properly synced
    public void setOnEndFloating(BiConsumer<Double, Boolean> consumer) {
        this.onEndFloating = consumer;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("Dropping", this.isDropping());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("Dropping", 99)) this.setDropping(compound.getBoolean("Dropping"));
    }

    @Override
    public boolean trySetBlock() {
        if (super.trySetBlock()) {
            this.getOnEndFloating().accept(Math.abs(this.lastYVelocity), true);
            return true;
        }
        return false;
    }

    @Override
    public void breakApart() {
        super.breakApart();
        this.getOnEndFloating().accept(Math.abs(this.lastYVelocity), false);
    }

    public boolean isFastFloater() {
        return this.getBlockState().isIn(AetherBlockTags.FAST_FLOATERS) && !this.partOfSet;
    }

    @Override
    public void alignWith(BlockLikeEntity other, Vec3i offset) {
        super.alignWith(other, offset);
        if (other instanceof FloatingBlockEntity fbe) {
            this.setDropping(fbe.isDropping());
        }
    }
}