package net.id.paradise_lost.api;

import net.id.paradise_lost.entity.util.PostTickEntity;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.List;

public abstract class BlockLikeEntity extends Entity implements PostTickEntity {
    private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(BlockLikeEntity.class, EntityDataSerializers.BLOCK_POS);
    public int moveTime;
    public boolean dropItem = true;
    protected CompoundTag blockEntityData;
    protected BlockState blockState = Blocks.STONE.defaultBlockState();
    protected boolean canSetBlock = true;
    protected boolean hurtEntities = false;
    protected int fallHurtMax = 40;
    protected float fallHurtAmount = 2.0f;
    protected boolean collides;
    protected boolean partOfSet = false;

    public BlockLikeEntity(EntityType<? extends BlockLikeEntity> entityType, Level world) {
        super(entityType, world);
        this.moveTime = 0;
    }

    public BlockLikeEntity(EntityType<? extends BlockLikeEntity> entityType, Level world, double x, double y, double z, BlockState blockState) {
        this(entityType, world);
        this.blockState = blockState;
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setOrigin(this.blockPosition());
    }

    public BlockLikeEntity(EntityType<? extends BlockLikeEntity> entityType, Level world, BlockPos pos, BlockState blockState, boolean partOfSet) {
        this(entityType, world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, blockState);
        this.partOfSet = partOfSet;
    }

    @Override
    protected AABB makeBoundingBox() {
        if (this.entityData == null || this.blockState == null) {
            return super.makeBoundingBox();
        }
        BlockPos origin = this.entityData.get(ORIGIN);
        VoxelShape shape = this.blockState.getCollisionShape(level(), origin);
        if (shape.isEmpty()) {
            this.collides = false;
            shape = this.blockState.getShape(level(), origin);
            if (shape.isEmpty()) {
                return super.makeBoundingBox();
            }
        } else {
            this.collides = true;
        }
        AABB box = shape.bounds();
        return box.move(position().subtract(new Vec3(0.5, 0, 0.5)));
    }

    @Override
    public void tick() {

        if (this.hurtEntities) {
            double verticalSpeed = Math.abs(this.getDeltaMovement().y());
            this.fallHurtAmount = this.blockState.getBlock().defaultDestroyTime() * (float) verticalSpeed;
            this.fallHurtMax = Math.max(Math.round(this.fallHurtAmount), this.fallHurtMax);
        }
    }

    public abstract void postTickMovement();

    public void postTickEntityCollision(Entity entity) {
        if (!(entity instanceof BlockLikeEntity ble && ble.partOfSet)) {
            this.blockState.entityInside(level(), this.blockPosition(), entity);
        }
    }

    public boolean shouldCease() {
        if (this.level().isClientSide) return false;

        BlockPos blockPos = this.blockPosition();
        boolean isConcrete = this.blockState.getBlock() instanceof ConcretePowderBlock;

        if (isConcrete && this.level().getFluidState(blockPos).is(FluidTags.WATER)) {
            return true;
        }

        double speed = this.getDeltaMovement().lengthSqr();

        if (isConcrete && speed > 1.0D) {
            BlockHitResult blockHitResult = this.level().clip(new ClipContext(
                    new Vec3(this.xo, this.yo, this.zo),
                    new Vec3(this.getX(), this.getY(), this.getZ()),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this)
            );

            if (blockHitResult.getType() != HitResult.Type.MISS
                    && this.level().getFluidState(blockHitResult.getBlockPos()).is(FluidTags.WATER)) {
                return true;
            }
        }

        return this.moveTime > 100 && (blockPos.getY() < this.level().getMinBuildHeight() || blockPos.getY() > this.level().getMaxBuildHeight());
    }

    public void incubus_Concern$postTick() {
        if (this.blockState.isAir()) {
            this.discard();
            return;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (this.moveTime++ == 0) {
            BlockPos blockPos = this.blockPosition();
            Block block = this.blockState.getBlock();
            if (this.level().getBlockState(blockPos).is(block)) {
                this.level().removeBlock(blockPos, false);
            } else if (!this.level().isClientSide && !this.partOfSet) {
                this.discard();
                return;
            }
        }

        this.postTickMovement();

        this.postTickMoveEntities();

        if (this.shouldCease()) this.cease();
    }

    public void postTickMoveEntities() {
        if (FallingBlock.isFree(this.blockState)) return;

        List<Entity> otherEntities = this.level().getEntities(this, getBoundingBox().minmax(getBoundingBox().move(0, 0.5, 0)));
        for (var entity : otherEntities) {
            if (!(entity instanceof BlockLikeEntity) && !entity.noPhysics && collides) {
                entity.move(MoverType.SHULKER_BOX, this.getDeltaMovement());
                entity.setOnGround(true);

                if (!entity.getBoundingBox().move(entity.getDeltaMovement().scale(2)).intersects(
                        this.getBoundingBox().move(this.getDeltaMovement().scale(2)))) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(this.getDeltaMovement()));
                }
            }
            this.postTickEntityCollision(entity);
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource damageSource) {
        int i = Mth.ceil(distance - 1.0F);

        if (!this.hurtEntities || i <= 0) {
            return false;
        }

        boolean flag = this.blockState.is(BlockTags.ANVIL);
        DamageSource damageSource2 = flag ? this.level().damageSources().anvil(this) : this.level().damageSources().fallingBlock(this);
        float f = Math.min(Mth.floor((float) i * this.fallHurtAmount), this.fallHurtMax);

        this.level().getEntities(this, getBoundingBox().minmax(getBoundingBox().move(0, 1 + -2 * this.getDeltaMovement().y(), 0))).forEach(entity -> entity.hurt(damageSource2, f));

        if (flag && f > 0.0F && this.random.nextFloat() < 0.05F + i * 0.05F) {
            BlockState blockstate = AnvilBlock.damage(this.blockState);
            if (blockstate == null) {
                this.canSetBlock = false;
            } else this.blockState = blockstate;
        }
        return false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
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
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("BlockState"));
        this.moveTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallHurtAmount = compound.getFloat("FallHurtAmount");
            this.fallHurtMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");

        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.blockState.isAir()) this.blockState = Blocks.STONE.defaultBlockState();
    }

        public Level getWorldObj() {
        return this.level();
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory section) {
        super.fillCrashReportCategory(section);
        section.setDetail("Imitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
    }

    public void cease() {
        if (this.isRemoved()) {
            return;
        }
        BlockPos pos = this.blockPosition();
        BlockState state = this.level().getBlockState(pos);

        if (state.is(Blocks.MOVING_PISTON)) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, 0.5, 0.7));
            return;
        }
        if (!this.trySetBlock()) {
            this.breakApart();
        }
    }

    public boolean trySetBlock() {
        BlockPos blockPos = this.blockPosition();
        BlockState blockState = this.level().getBlockState(blockPos);
        boolean canReplace = blockState.canBeReplaced(new DirectionalPlaceContext(this.level(), blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
        boolean canPlace = this.blockState.canSurvive(this.level(), blockPos);

        if (!this.canSetBlock || !canPlace || !canReplace)
            return false;

        if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockPos).getType() == Fluids.WATER) {
            this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        if (this.level().setBlock(blockPos, this.blockState, Block.UPDATE_ALL)) {
            this.discard();
            if (this.blockEntityData != null && this.blockState.hasBlockEntity()) {
                BlockEntity blockEntity = this.level().getBlockEntity(blockPos);
                if (blockEntity != null) {
                    CompoundTag compoundTag = blockEntity.saveWithoutMetadata(this.level().registryAccess());
                    for (String keyName : this.blockEntityData.getAllKeys()) {
                        Tag tag = this.blockEntityData.get(keyName);
                        if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
                            compoundTag.put(keyName, tag.copy());
                        }
                    }

                    blockEntity.loadWithComponents(compoundTag, this.level().registryAccess());
                    blockEntity.setChanged();
                }
            }
            for (Direction dir : Direction.stream().toList()) {
                var newState = this.blockState.updateShape(
                        dir,
                        this.level().getBlockState(blockPos.relative(dir)),
                        this.level(),
                        blockPos,
                        blockPos.relative(dir)
                );
                this.level().setBlockAndUpdate(blockPos, newState);
                this.blockState = newState;
            }
            level().scheduleTick(blockPos, this.blockState.getBlock(), 1);

            this.postTickMoveEntities();
            return true;
        }
        return false;
    }

    public void breakApart() {
        if (this.isRemoved()) return;

        this.discard();
        if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            Block.dropResources(this.blockState, this.level(), this.blockPosition());
        }

        level().levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, this.blockPosition(), Block.getId(blockState));
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
        return new ClientboundAddEntityPacket(this, entityTrackerEntry, Block.getId(this.getBlockState()) * (this.partOfSet ? -1 : 1));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        int data = packet.getData();
        this.partOfSet = data < 0;
        this.blockState = Block.stateById(packet.getData() * (this.partOfSet ? -1 : 1));
        this.blocksBuilding = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPos(d, e + (double) ((1.0F - this.getBbHeight()) / 2.0F), f);
        this.setOrigin(this.blockPosition());
    }

    public void alignWith(BlockLikeEntity other, Vec3i offset) {
        if (this == other) return;
        Vec3 newPos = other.position().add(Vec3.atLowerCornerOf(offset));
        this.setPosRaw(newPos.x, newPos.y, newPos.z);
        this.setDeltaMovement(other.getDeltaMovement());
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

        public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.entityData.set(ORIGIN, origin);
        this.setPos(getX(), getY(), getZ());
    }

    public void markPartOfSet() {
        this.partOfSet = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ORIGIN, BlockPos.ZERO);
    }

    @Override
    public boolean canBeCollidedWith() {
        return collides;
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return !(other instanceof BlockLikeEntity) && super.canCollideWith(other);
    }
}
