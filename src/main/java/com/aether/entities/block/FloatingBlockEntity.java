package com.aether.entities.block;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.tag.AetherBlockTags;
import com.aether.entities.util.floatingblock.FloatingBlockHelper;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
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
import net.minecraft.tag.FluidTags;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FloatingBlockEntity extends Entity {
    protected static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(FloatingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    public int floatTime;
    public boolean dropItem = true;
    public NbtCompound blockEntityData;
    private BlockState floatTile = AetherBlocks.GRAVITITE_ORE.getDefaultState();
    private boolean dontSetBlock;
    private boolean hurtEntities;
    private int floatHurtMax = 40;
    private float floatHurtAmount = 2.0f;
    private Supplier<Boolean> dropState = () -> false;
    private boolean dropping = false;
    private boolean collides;
    private boolean partOfStructure = false;
    private BiConsumer<Float, Boolean> onEndFloating;

    public FloatingBlockEntity(EntityType<? extends FloatingBlockEntity> entityType, World world) {
        super(entityType, world);
        this.setOnEndFloating((impact, landed) -> {});
        this.floatTime = 0;
        this.setDropState(() -> FloatingBlockHelper.DEFAULT_DROP_STATE.apply(this));
    }

    public FloatingBlockEntity(World world, double x, double y, double z, BlockState floatingBlockState) {
        this(AetherEntityTypes.FLOATING_BLOCK, world);
        this.floatTile = floatingBlockState;
        this.inanimate = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setOrigin(new BlockPos(this.getPos()));
    }

    public FloatingBlockEntity(World world, BlockPos pos, BlockState floatingBlockState, boolean partOfStructure) {
        this(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, floatingBlockState);
        this.partOfStructure = partOfStructure;
    }

    @Override
    protected Box calculateBoundingBox(){
        if (this.dataTracker == null || this.floatTile == null) {
            return super.calculateBoundingBox();
        }
        BlockPos origin = this.dataTracker.get(ORIGIN);
        VoxelShape shape = this.floatTile.getCollisionShape(world, origin);
        if (shape.isEmpty()) {
            this.collides = false;
            shape = this.floatTile.getOutlineShape(world, origin);
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

    public void markPartOfStructure(){
        partOfStructure = true;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
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
        return !(other instanceof FloatingBlockEntity) && super.collidesWith(other);
    }

    @Override
    public void tick() {}

    @SuppressWarnings("deprecation")
    public void postTickEntities() {
        if (this.floatTile.isAir()) {
            this.discard();
        } else {
            float impact = (float) this.getVelocity().length();
            this.prevX = this.getX();
            this.prevY = this.getY();
            this.prevZ = this.getZ();
            if (this.floatTime++ == 0) {
                BlockPos blockPos = this.getBlockPos();
                Block block = this.floatTile.getBlock();
                if (this.world.getBlockState(blockPos).isOf(block)) {
                    this.world.removeBlock(blockPos, false);
                } else if (!this.world.isClient && !this.partOfStructure) {
                    this.discard();
                    return;
                }
            }

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

            // Take flight, my child!
            if (!FallingBlock.canFallThrough(this.floatTile)) {
                List<Entity> otherEntities = this.world.getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0,1 + -2 * this.getVelocity().getY(),0)));
                for (Entity entity : otherEntities) {
                    if (!(entity instanceof FloatingBlockEntity) && !entity.noClip && this.collides()) {
                        entity.fallDistance = 0F;
                        entity.setPosition(entity.getPos().x, getBoundingBox().maxY, entity.getPos().z);
                        entity.setOnGround(true);
                    }
                    this.floatTile.getBlock().onEntityCollision(floatTile, world, this.getBlockPos(), entity);
                }
            }

            if (!this.world.isClient) {
                BlockPos blockPos = this.getBlockPos();
                boolean isConcrete = this.floatTile.getBlock() instanceof ConcretePowderBlock;
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
                            Block.dropStacks(this.floatTile, this.world, this.getBlockPos());
                        }
                        this.discard();
                    }
                } else {
                    this.land(impact);
                }
            }

            if ((this.isDropping() || this.getVelocity().getY() == 0) && this.isOnGround()) {
                this.land(impact);
            }

            this.setVelocity(this.getVelocity().multiply(0.98D));
        }
    }

    @Override
    public boolean handleFallDamage(float distance, float multiplier, DamageSource damageSource) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(distance - 1.0F);
            if (i > 0) {
                List<Entity> list = Lists.newArrayList(this.world.getOtherEntities(this, this.getBoundingBox()));
                boolean flag = this.floatTile.isIn(BlockTags.ANVIL);
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

                for (Entity entity : list) {
                    entity.damage(damagesource, Math.min(MathHelper.floor(i * this.floatHurtAmount), this.floatHurtMax));
                }

                if (flag && this.random.nextFloat() < 0.05F + i * 0.05F) {
                    BlockState blockstate = AnvilBlock.getLandingState(this.floatTile);
                    if (blockstate == null) {
                        this.dontSetBlock = true;
                    }
                    else this.floatTile = blockstate;
                }
            }
        }
        return false;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.put("BlockState", NbtHelper.fromBlockState(this.floatTile));
        compound.putInt("Time", this.floatTime);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("Dropping", this.isDropping());
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.floatHurtAmount);
        compound.putInt("FallHurtMax", this.floatHurtMax);
        if (this.blockEntityData != null) {
            compound.put("TileEntityData", this.blockEntityData);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        this.floatTile = NbtHelper.toBlockState(compound.getCompound("BlockState"));
        this.floatTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.floatHurtAmount = compound.getFloat("FallHurtAmount");
            this.floatHurtMax = compound.getInt("FallHurtMax");
        } else if (this.floatTile.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");

        if (compound.contains("Dropping", 99)) this.setDropping(compound.getBoolean("Dropping"));

        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.floatTile.isAir()) this.floatTile = AetherBlocks.GRAVITITE_ORE.getDefaultState();
    }

    @Environment(EnvType.CLIENT)
    public World getWorldObj() {
        return this.world;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Immitating BlockState", this.floatTile.toString());
    }

    public BlockState getBlockState() {
        return this.floatTile;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
    }

    public Supplier<Boolean> getDropState() {
        return dropState;
    }

    public boolean shouldBeginDropping() {
        return getDropState().get();
    }

    public void setDropState(Supplier<Boolean> supplier) {
        dropState = supplier;
    }

    public boolean isDropping() {
        return dropping;
    }

    public void setDropping(boolean dropping) {
        this.dropping = dropping;
    }

    public boolean isFastFloater(){
        return AetherBlockTags.FAST_FLOATERS.contains(this.floatTile.getBlock()) && !this.partOfStructure;
    }

    @Override
    public boolean entityDataRequiresOperator() {
        return true;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()) * (this.partOfStructure ? -1 : 1));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        int data = packet.getEntityData();
        this.partOfStructure = data < 0;
        this.floatTile = Block.getStateFromRawId(packet.getEntityData() * (this.partOfStructure ? -1 : 1));
        this.inanimate = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPosition(d, e + (double)((1.0F - this.getHeight()) / 2.0F), f);
        this.setOrigin(this.getBlockPos());
    }

    public interface ICPEM {
        void postTick();
    }

    public BiConsumer<Float, Boolean> getOnEndFloating(){
        return this.onEndFloating;
    }

    public void setOnEndFloating(BiConsumer<Float, Boolean> consumer){
        this.onEndFloating = consumer;
    }

    public void land(float impact) {
        boolean landingSuccessful = false;
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.world.getBlockState(blockPos);
        this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
        if (blockState.getBlock() != Blocks.MOVING_PISTON) {
            this.discard();
            if (!this.dontSetBlock) {
                boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
                boolean canPlace = this.floatTile.canPlaceAt(this.world, blockPos);

                if (canReplace && canPlace) {
                    if (this.floatTile.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockPos).getFluid() == Fluids.WATER)
                        this.floatTile = this.floatTile.with(Properties.WATERLOGGED, true);

                    if (this.world.setBlockState(blockPos, this.floatTile, 3)) {
                        landingSuccessful = true;
                        if (this.blockEntityData != null && this.floatTile.hasBlockEntity()) {
                            BlockEntity blockEntity = this.world.getBlockEntity(blockPos);
                            if (blockEntity != null) {
                                NbtCompound compoundTag = blockEntity.writeNbt(new NbtCompound());

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
                if (!landingSuccessful && this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                    Block.dropStacks(this.floatTile, this.world, this.getBlockPos());
                }
            }
            this.getOnEndFloating().accept(impact, landingSuccessful);
        }
    }
    
    public static boolean canMakeBlock(boolean shouldDrop, BlockState below, BlockState above){
        if(shouldDrop){
            return FallingBlock.canFallThrough(below);
        } else {
            return FallingBlock.canFallThrough(above);
        }
    }
}
