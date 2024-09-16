package net.id.paradiselost.entities.block;

import net.id.paradiselost.api.BlockLikeEntity;
import net.id.paradiselost.api.FloatingBlockHelper;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FloatingBlockEntity extends BlockLikeEntity {
    private Supplier<Boolean> dropState = () -> FloatingBlockHelper.DEFAULT_DROP_STATE.apply(this);
    private boolean dropping = false;
    private BiConsumer<Double, Boolean> onEndFloating = (f, b) -> {
    };
    public double lastYVelocity = 0;

    public FloatingBlockEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FloatingBlockEntity(World world, double x, double y, double z, BlockState floatingBlockState) {
        super(ParadiseLostEntityTypes.FLOATING_BLOCK, world, x, y, z, floatingBlockState);
        this.setHurtEntities(floatingBlockState.isIn(ParadiseLostBlockTags.HURTABLE_FLOATERS));
    }

    public FloatingBlockEntity(World world, BlockPos pos, BlockState floatingBlockState, boolean partOfSet) {
        super(ParadiseLostEntityTypes.FLOATING_BLOCK, world, pos, floatingBlockState, partOfSet);
        this.setHurtEntities(floatingBlockState.isIn(ParadiseLostBlockTags.HURTABLE_FLOATERS));
    }

    @Override
    public void postTickMoveEntities() {
        if (FallingBlock.canFallThrough(this.blockState)) return;

        List<Entity> otherEntities = this.method_48926().getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 3 * (this.prevY - this.getY()), 0)));
        for (Entity entity : otherEntities) {
            if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides) {
                entity.move(MovementType.SHULKER_BOX, this.getVelocity());
                entity.setOnGround(true);
                
                entity.setPosition(entity.getX(), this.getBoundingBox().maxY, entity.getZ());
                entity.fallDistance = 0F;
            }
            this.postTickEntityCollision(entity);
        }
    }
    
    @Override
    public void postTickMovement() {
        // Drag
        this.setVelocity(this.getVelocity().multiply(0.98D));
        
        this.lastYVelocity = this.getVelocity().y;
        
        if (!this.hasNoGravity()) {
            if (!isDropping() && !shouldBeginDropping()) {
                if (isInTag(ParadiseLostBlockTags.FAST_FLOATERS)) {
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
    }
    
    @Override
    public boolean shouldCease() {
        return super.shouldCease()
               || (this.isOnGround() && (this.isDropping() || this.getVelocity().getY() == 0)
                   || (this.verticalCollision && !this.isOnGround()));
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

    public boolean isInTag(TagKey<Block> tag) {
        return this.getBlockState().isIn(tag) && !this.partOfSet;
    }
    
    @Override
    public void alignWith(BlockLikeEntity other, Vec3i offset) {
        super.alignWith(other, offset);
        if (other instanceof FloatingBlockEntity fbe) {
            this.setDropping(fbe.isDropping());
        }
    }
}
