package net.id.aether.entities.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.entities.util.PostTickEntity;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.List;

public abstract class BlockLikeEntity extends Entity implements PostTickEntity {
    private static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(BlockLikeEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected BlockState blockState;
    private boolean collides;
    protected NbtCompound blockEntityData;
    protected int moveTime;
    protected boolean partOfSet = false;
    protected boolean dontSetBlock;
    public boolean dropItem = true;
    private boolean hurtEntities;
    private int floatHurtMax = 40;
    private float floatHurtAmount = 2.0f;

    public BlockLikeEntity(EntityType<?> type, World world){
        super(type, world);
    }

    public BlockLikeEntity(EntityType<?> type, World world, BlockState state, Vec3d pos){
        this(type, world);
        this.moveTime = 0;
        this.blockState = state;
        this.intersectionChecked = true;
        this.setPosition(pos);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = pos.getX();
        this.prevY = pos.getY();
        this.prevZ = pos.getZ();
        this.setOrigin(this.getBlockPos());
    }

    public BlockLikeEntity(EntityType<?> type, World world, BlockState state, BlockPos pos){
        this(type, world, state, Vec3d.ofCenter(pos));
    }

    @Override
    public void tick() {
        if (this.blockState.isIn(AetherBlockTags.HURTABLE_FLOATERS)) {
            double verticalSpeed = Math.abs(this.getVelocity().getY());
            this.hurtEntities = true;
            this.floatHurtAmount = this.blockState.getBlock().getHardness() * (float)verticalSpeed;
            this.floatHurtMax = Math.max(Math.round(this.floatHurtAmount), this.floatHurtMax);
        }
    }

    @Override
    public void postTick(){
        if (this.blockState.isOf(Blocks.AIR)){
            this.discard();
            return;
        }
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();

        // Remove block when spawned
        if (this.moveTime++ == 0) {
            BlockPos blockPos = this.getBlockPos();
            Block block = this.blockState.getBlock();
            if (this.world.getBlockState(blockPos).isOf(block)) {
                this.world.removeBlock(blockPos, false);
            } else if (!this.world.isClient && !this.partOfSet) {
                this.discard();
                return;
            }
        }

        // Blockstate onEntityCollision
        if (!FallingBlock.canFallThrough(this.blockState)) {
            List<Entity> otherEntities = this.world.getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 1 + -2 * this.getVelocity().getY(), 0)));
            for (Entity entity : otherEntities) {
                if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides()) {
                    entity.fallDistance = 0F;
                    entity.setPosition(entity.getPos().x, getBoundingBox().maxY, entity.getPos().z);
                    entity.setOnGround(true);
                }
                if (!(entity instanceof BlockLikeEntity ble && ble.partOfSet)) {
                    this.blockState.onEntityCollision(world, this.getBlockPos(), entity);
                }
            }
        }
    }

    // Optionally move this to BlockLikeEntity
    @Override
    public boolean handleFallDamage(float distance, float multiplier, DamageSource damageSource) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(distance - 1.0F);
            if (i > 0) {
                boolean flag = this.blockState.isIn(BlockTags.ANVIL);
                DamageSource damageSource2 = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                float f = Math.min(MathHelper.floor((float)i * this.floatHurtAmount), this.floatHurtMax);

                this.world.getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 1 + -2 * this.getVelocity().getY(), 0))).forEach(entity -> entity.damage(damageSource2, f));

                if (flag && f > 0.0F && this.random.nextFloat() < 0.05F + i * 0.05F) {
                    BlockState blockstate = AnvilBlock.getLandingState(this.blockState);
                    if (blockstate == null) {
                        this.dontSetBlock = true;
                    } else this.blockState = blockstate;
                }
            }
        }
        return false;
    }

    @Override
    protected Box calculateBoundingBox() {
        if (this.dataTracker == null || this.blockState == null) {
            return super.calculateBoundingBox();
        }
        VoxelShape shape = this.blockState.getCollisionShape(world, this.getBlockPos());
        if (shape.isEmpty()) {
            this.collides = false;
            shape = this.blockState.getOutlineShape(world, this.getBlockPos());
            if (shape.isEmpty()) {
                return super.calculateBoundingBox();
            }
        } else {
            this.collides = true;
        }
        Box box = shape.getBoundingBox();
        return box.offset(getPos().subtract(new Vec3d(0.5, 0, 0.5)));
    }

    protected void breakAndDiscard(boolean dropItem){
        if (dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            Block.dropStacks(this.blockState, this.world, this.getBlockPos());
        }
        // spawn break particles
        world.syncWorldEvent(null, WorldEvents.BLOCK_BROKEN, getBlockPos(), Block.getRawIdFromState(blockState));
        this.discard();
    }

    @Override
    public boolean collides() {
        return !this.isRemoved() && collides;
    }

    @Override
    public boolean isCollidable() {
        return this.collides();
    }

    @Override
    public boolean collidesWith(Entity other) {
        return !(other instanceof BlockLikeEntity) && super.collidesWith(other);
    }

    public BlockState getBlockState(){
        return this.blockState;
    }

    public void markPartOfSet() {
        partOfSet = true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        this.blockState = NbtHelper.toBlockState(compound.getCompound("BlockState"));
        this.moveTime = compound.getInt("Time");

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");
        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.blockState.isAir()) this.blockState = Blocks.DIRT.getDefaultState();

        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.floatHurtAmount = compound.getFloat("FallHurtAmount");
            this.floatHurtMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
        compound.putInt("Time", this.moveTime);
        compound.putBoolean("DropItem", this.dropItem);
        if (this.blockEntityData != null) {
            compound.put("TileEntityData", this.blockEntityData);
        }
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.floatHurtAmount);
        compound.putInt("FallHurtMax", this.floatHurtMax);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()) * (this.partOfSet ? -1 : 1));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        int data = packet.getEntityData();
        this.partOfSet = data < 0;
        this.blockState = Block.getStateFromRawId(data * (this.partOfSet ? -1 : 1));
        this.intersectionChecked = true;
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        this.setPosition(x, y + (double) ((1.0F - this.getHeight()) / 2.0F), z);
        this.setOrigin(this.getBlockPos());
    }

    @Override
    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Imitating BlockState", this.blockState.toString());
    }

    @Override
    public boolean entityDataRequiresOperator() {
        return true;
    }

    public void alignWith(BlockLikeEntity other, Vec3i offset) {
        if (this == other) return;
        Vec3d newPos = other.getPos().add(Vec3d.of(offset));
        this.setPos(newPos.x, newPos.y, newPos.z);
        this.setVelocity(other.getVelocity());
    }

    @Environment(EnvType.CLIENT)
    public World getWorldObj() {
        return this.world;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

    @Environment(EnvType.CLIENT)
    public BlockPos getOrigin() {
        return this.dataTracker.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.dataTracker.set(ORIGIN, origin);
        this.setPosition(getX(), getY(), getZ());
    }

    public boolean tryLand(double impact) {
        if (this.isRemoved()) {
            return false;
        }
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.MOVING_PISTON)) {
            this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
            return false;
        }
        boolean landingSuccessful = false;
        if (!this.dontSetBlock) {
            boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
            boolean canPlace = this.blockState.canPlaceAt(this.world, blockPos);

            if (canReplace && canPlace) {
                if (this.blockState.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockPos).getFluid() == Fluids.WATER) {
                    this.blockState = this.blockState.with(Properties.WATERLOGGED, true);
                }

                if (this.world.setBlockState(blockPos, this.blockState, Block.NOTIFY_ALL)) {
                    landingSuccessful = true;
                    this.discard();
                    if (this.blockEntityData != null && this.blockState.hasBlockEntity()) {
                        BlockEntity blockEntity = this.world.getBlockEntity(blockPos);
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
                }
            }
        }
        if (!landingSuccessful) {
            this.crashLand(impact);
            return false;
        }
        return true;
    }

    public void crashLand(double impact) {
        this.breakAndDiscard(true);
    }
}
