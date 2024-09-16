package net.id.paradiselost.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.util.PostTickEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import java.util.List;

/**
 * An entity that resembles a block.
 */
public abstract class BlockLikeEntity extends Entity implements PostTickEntity {
    private static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(BlockLikeEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    public int moveTime;
    public boolean dropItem = true;
    protected NbtCompound blockEntityData;
    protected BlockState blockState = Blocks.STONE.getDefaultState();
    protected boolean canSetBlock = true;
    protected boolean hurtEntities = false;
    protected int fallHurtMax = 40;
    protected float fallHurtAmount = 2.0f;
    protected boolean collides;
    protected boolean partOfSet = false;

    public BlockLikeEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
        this.moveTime = 0;
    }

    public BlockLikeEntity(EntityType<? extends BlockLikeEntity> entityType, World world, double x, double y, double z, BlockState blockState) {
        this(entityType, world);
        this.blockState = blockState;
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setOrigin(this.getBlockPos());
    }

    public BlockLikeEntity(EntityType<? extends BlockLikeEntity> entityType, World world, BlockPos pos, BlockState blockState, boolean partOfSet) {
        this(entityType, world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, blockState);
        this.partOfSet = partOfSet;
    }

    /**
     * Calculates the bounding box based on the blockstate's collision shape.
     * If the blockstate doesn't have collision, this method turns collision
     * off for this entity and sets the bounding box to the outline shape instead.
     * Note: Complex bounding boxes are not supported. These are all rectangular prisms.
     * @return The bounding box of this entity
     */
    @Override
    protected Box calculateBoundingBox() {
        if (this.dataTracker == null || this.blockState == null) {
            return super.calculateBoundingBox();
        }
        BlockPos origin = this.dataTracker.get(ORIGIN);
        VoxelShape shape = this.blockState.getCollisionShape(getWorld(), origin);
        if (shape.isEmpty()) {
            this.collides = false;
            shape = this.blockState.getOutlineShape(getWorld(), origin);
            if (shape.isEmpty()) {
                return super.calculateBoundingBox();
            }
        } else {
            this.collides = true;
        }
        Box box = shape.getBoundingBox();
        return box.offset(getPos().subtract(new Vec3d(0.5, 0, 0.5)));
    }

    @Override
    public void tick() {
        // recalculate fall damage
        if (this.hurtEntities) {
            double verticalSpeed = Math.abs(this.getVelocity().getY());
            this.fallHurtAmount = this.blockState.getBlock().getHardness() * (float) verticalSpeed;
            this.fallHurtMax = Math.max(Math.round(this.fallHurtAmount), this.fallHurtMax);
        }
    }

    /**
     * Override me! Calculate movement.
     */
    public abstract void postTickMovement();

    /**
     * Take actions on entities on "collision".
     * By default, it replicates the blockstate's behavior on collision.
     */
    public void postTickEntityCollision(Entity entity) {
        if (!(entity instanceof BlockLikeEntity ble && ble.partOfSet)) {
            this.blockState.onEntityCollision(getWorld(), this.getBlockPos(), entity);
        }
    }

    /**
     * @return Whether this entity should cease and return to being a block in the world.
     */
    public boolean shouldCease() {
        if (this.getWorld().isClient) return false;

        BlockPos blockPos = this.getBlockPos();
        boolean isConcrete = this.blockState.getBlock() instanceof ConcretePowderBlock;

        if (isConcrete && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER)) {
            return true;
        }

        double speed = this.getVelocity().lengthSquared();

        if (isConcrete && speed > 1.0D) {
            BlockHitResult blockHitResult = this.getWorld().raycast(new RaycastContext(
                    new Vec3d(this.prevX, this.prevY, this.prevZ),
                    new Vec3d(this.getX(), this.getY(), this.getZ()),
                    RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this)
            );

            if (blockHitResult.getType() != HitResult.Type.MISS
                    && this.getWorld().getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                return true;
            }
        }

        // Check if it is outside of the world
        return this.moveTime > 100 && (blockPos.getY() < this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopY());
    }

    /**
     * The big kahuna. You likely don't need to override this method.
     * Instead, override the methods that it calls.
     */
    public void incubus_Concern$postTick() {
        if (this.blockState.isAir()) {
            this.discard();
            return;
        }

        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();

        // Destroy the block in the world that this is spawned from
        // If no block exists, remove this entity (unless part of a set)
        if (this.moveTime++ == 0) {
            BlockPos blockPos = this.getBlockPos();
            Block block = this.blockState.getBlock();
            if (this.getWorld().getBlockState(blockPos).isOf(block)) {
                this.getWorld().removeBlock(blockPos, false);
            } else if (!this.getWorld().isClient && !this.partOfSet) {
                this.discard();
                return;
            }
        }

        this.postTickMovement();

        this.postTickMoveEntities();

        if (this.shouldCease()) this.cease();
    }

    /**
     * You likely won't need to override this method, but it imparts this block's
     * momentum onto other entities.
     */
    public void postTickMoveEntities() {
        if (FallingBlock.canFallThrough(this.blockState)) return;

        List<Entity> otherEntities = this.getWorld().getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 0.5, 0)));
        for (var entity : otherEntities) {
            if (!(entity instanceof BlockLikeEntity) && !entity.noClip && collides) {
                entity.move(MovementType.SHULKER_BOX, this.getVelocity());
                entity.setOnGround(true);

                // If we're about to stop touching, give the entity momentum.
                if (!entity.getBoundingBox().offset(entity.getVelocity().multiply(2)).intersects(
                        this.getBoundingBox().offset(this.getVelocity().multiply(2)))) {
                    entity.setVelocity(entity.getVelocity().add(this.getVelocity()));
                }
            }
            this.postTickEntityCollision(entity);
        }
    }

    @Override
    public boolean handleFallDamage(float distance, float multiplier, DamageSource damageSource) {
        int i = MathHelper.ceil(distance - 1.0F);

        if (!this.hurtEntities || i <= 0) {
            return false;
        }

        boolean flag = this.blockState.isIn(BlockTags.ANVIL);
        DamageSource damageSource2 = flag ? this.getWorld().getDamageSources().fallingAnvil(this) : this.getWorld().getDamageSources().fallingBlock(this);
        float f = Math.min(MathHelper.floor((float) i * this.fallHurtAmount), this.fallHurtMax);

        this.getWorld().getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 1 + -2 * this.getVelocity().getY(), 0))).forEach(entity -> entity.damage(damageSource2, f));

        if (flag && f > 0.0F && this.random.nextFloat() < 0.05F + i * 0.05F) {
            BlockState blockstate = AnvilBlock.getLandingState(this.blockState);
            if (blockstate == null) {
                this.canSetBlock = false;
            } else this.blockState = blockstate;
        }
        return false;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
        compound.putInt("Time", this.moveTime);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.fallHurtAmount);
        compound.putInt("FallHurtMax", this.fallHurtMax);
        if (this.blockEntityData != null) {
            compound.put("TileEntityData", this.blockEntityData);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        this.blockState = NbtHelper.toBlockState(BuiltinRegistries.createWrapperLookup().getWrapperOrThrow(RegistryKeys.BLOCK), compound.getCompound("BlockState"));
        this.moveTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallHurtAmount = compound.getFloat("FallHurtAmount");
            this.fallHurtMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");

        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.blockState.isAir()) this.blockState = Blocks.STONE.getDefaultState();
    }

    @Environment(EnvType.CLIENT)
    public World getWorldObj() {
        return this.getWorld();
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Imitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
    }

    /**
     * End entity movement and become a block in the world (Removes this entity).
     */
    public void cease() {
        if (this.isRemoved()) {
            return;
        }
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getWorld().getBlockState(pos);
        // I don't like this
        if (state.isOf(Blocks.MOVING_PISTON)) {
            this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
            return;
        }
        if (!this.trySetBlock()) {
            this.breakApart();
        }
    }

    /**
     * Tries to set the block
     * @return {@code true} if the block can be set
     */
    public boolean trySetBlock() {
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
        boolean canPlace = this.blockState.canPlaceAt(this.getWorld(), blockPos);

        if (!this.canSetBlock || !canPlace || !canReplace)
            return false;

        if (this.blockState.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
            this.blockState = this.blockState.with(Properties.WATERLOGGED, true);
        }

        if (this.getWorld().setBlockState(blockPos, this.blockState, Block.NOTIFY_ALL)) {
            this.discard();
            if (this.blockEntityData != null && this.blockState.hasBlockEntity()) {
                BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                if (blockEntity != null) {
                    NbtCompound compoundTag = blockEntity.createNbt();
                    for (String keyName : this.blockEntityData.getKeys()) {
                        NbtElement tag = this.blockEntityData.get(keyName);
                        if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
                            compoundTag.put(keyName, tag.copy());
                        }
                    }

                    blockEntity.readNbt(compoundTag);
                    blockEntity.markDirty();
                }
            }
            for (Direction dir : Direction.stream().toList()) {
                var newState = this.blockState.getStateForNeighborUpdate(
                        dir,
                        this.getWorld().getBlockState(blockPos.offset(dir)),
                        this.getWorld(),
                        blockPos,
                        blockPos.offset(dir)
                );
                this.getWorld().setBlockState(blockPos, newState);
                this.blockState = newState;
            }
            getWorld().scheduleBlockTick(blockPos, this.blockState.getBlock(), 1);
            // Stop entities from clipping through the block when it's set
            this.postTickMoveEntities();
            return true;
        }
        return false;
    }

    /**
     * Break the block, spawn break particles, and drop stacks if it can.
     */
    public void breakApart() {
        if (this.isRemoved()) return;

        this.discard();
        if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            Block.dropStacks(this.blockState, this.getWorld(), this.getBlockPos());
        }
        // spawn break particles
        getWorld().syncWorldEvent(null, WorldEvents.BLOCK_BROKEN, this.getBlockPos(), Block.getRawIdFromState(blockState));
    }

    @Override
    public boolean entityDataRequiresOperator() {
        return true;
    }

    @Override
    public EntitySpawnS2CPacket createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()) * (this.partOfSet ? -1 : 1));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        int data = packet.getEntityData();
        this.partOfSet = data < 0;
        this.blockState = Block.getStateFromRawId(packet.getEntityData() * (this.partOfSet ? -1 : 1));
        this.intersectionChecked = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPosition(d, e + (double) ((1.0F - this.getHeight()) / 2.0F), f);
        this.setOrigin(this.getBlockPos());
    }

    /**
     * Aligns this block to another.
     * @param other The other block to align with
     * @param offset The offset from the other block. this pos - other pos.
     * @see BlockLikeSet
     */
    public void alignWith(BlockLikeEntity other, Vec3i offset) {
        if (this == other) return;
        Vec3d newPos = other.getPos().add(Vec3d.of(offset));
        this.setPos(newPos.x, newPos.y, newPos.z);
        this.setVelocity(other.getVelocity());
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public BlockPos getOrigin() {
        return this.dataTracker.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.dataTracker.set(ORIGIN, origin);
        this.setPosition(getX(), getY(), getZ());
    }

    public void markPartOfSet() {
        this.partOfSet = true;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

    //@Override
    //public boolean collides() {
    //    return !this.isRemoved() && this.collides;
    //}

    @Override
    public boolean isCollidable() {
        return collides;
    }

    @Override
    public boolean collidesWith(Entity other) {
        return !(other instanceof BlockLikeEntity) && super.collidesWith(other);
    }
}
