package net.id.aether.entities.block;

import net.id.aether.api.FloatingBlockHelper;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.util.PostTickEntity;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FloatingBlockEntity extends BlockLikeEntity implements PostTickEntity {
    private Supplier<Boolean> dropState = () -> FloatingBlockHelper.DEFAULT_DROP_STATE.apply(this);
    private boolean dropping = false;
    private BiConsumer<Float, Boolean> onEndFloating = (f, b) -> {};
    public float lastYVelocity = 0;

    public FloatingBlockEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FloatingBlockEntity(World world, double x, double y, double z, BlockState floatingBlockState) {
        super(AetherEntityTypes.FLOATING_BLOCK, world, x, y, z, floatingBlockState);
    }

    public FloatingBlockEntity(World world, BlockPos pos, BlockState floatingBlockState, boolean partOfSet) {
        super(AetherEntityTypes.FLOATING_BLOCK, world, pos, floatingBlockState, partOfSet);
    }

    @Override
    public void postTickMovement() {
        this.lastYVelocity = 0;

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
    public boolean shouldCease(double impact) {
        return super.shouldCease(impact) ||
                (this.isOnGround() && (this.isDropping() || this.getVelocity().getY() == 0));
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

    public BiConsumer<Float, Boolean> getOnEndFloating() {
        return this.onEndFloating;
    }

    public void setOnEndFloating(BiConsumer<Float, Boolean> consumer) {
        this.onEndFloating = consumer;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        compound.putBoolean("Dropping", this.isDropping());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        if (compound.contains("Dropping", 99)) this.setDropping(compound.getBoolean("Dropping"));
    }

    @Override
    public boolean trySetBlock() {
        if (super.trySetBlock()) {
            this.getOnEndFloating().accept(this.lastYVelocity, true);
            return true;
        }
        return false;
    }

    @Override
    public void breakApart() {
        super.breakApart();
        this.getOnEndFloating().accept(this.lastYVelocity, false);
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