package com.aether.entities.block;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.FloatingBlock;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.AetherNonLivingEntity;
import com.aether.entities.util.EntityData;
import com.aether.util.NetworkingHell;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class FloatingBlockEntity extends AetherNonLivingEntity {
    protected static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(FloatingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    public int floatTime;
    public boolean dropItem;
    public CompoundTag blockEntityData;
    private BlockState blockState;
    private boolean destroyedOnLanding;
    private boolean hurtEntities;
    private int floatHurtMax;
    private float floatHurtAmount;

    public FloatingBlockEntity(EntityType<? extends FloatingBlockEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.blockState = AetherBlocks.GRAVITITE_ORE.getDefaultState();
        this.dropItem = true;
        this.floatHurtMax = 40;
        this.floatHurtAmount = 2.0F;
    }

    public FloatingBlockEntity(World world, double x, double y, double z, BlockState floatingBlockState) {
        this(AetherEntityTypes.FLOATING_BLOCK, world);
        this.blockState = floatingBlockState;
        this.inanimate = true;
        this.updatePosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setOrigin(new BlockPos(this.getPos()));
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
    }

    @Override
    public boolean canClimb() {
        return false;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

    @Override
    public boolean collides() {
        return !this.removed;
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.remove();
        } else {
            //this.prevX = this.getX();
            //this.prevY = this.getY();
            //this.prevZ = this.getZ();
            Block block = this.blockState.getBlock();
            BlockPos blockPos;
            if (this.floatTime++ == 0) {
                blockPos = this.getBlockPos();
                if (this.world.getBlockState(blockPos).isOf(block)) {
                    this.world.removeBlock(blockPos, false);
                } else if (!this.world.isClient) {
                    this.remove();
                    return;
                }
            }

            if (!this.hasNoGravity()) this.setVelocity(this.getVelocity().add(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * 0.075D, 0.0D));

            this.move(MovementType.SELF, this.getVelocity());
            if (!this.world.isClient) {
                blockPos = this.getBlockPos();
                boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.world.getFluidState(blockPos).isIn(FluidTags.WATER);
                double d0 = this.getVelocity().lengthSquared();

                if (flag && d0 > 1.0D) {
                    BlockHitResult blockHitResult = this.world
                            .raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ),
                                    new Vec3d(this.getX(), this.getY(), this.getZ()), RaycastContext.ShapeType.COLLIDER,
                                    RaycastContext.FluidHandling.SOURCE_ONLY, this));

                    if (blockHitResult.getType() != HitResult.Type.MISS
                            && this.world.getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                        blockPos = blockHitResult.getBlockPos();
                        flag1 = true;
                    }
                }

                if ((!this.verticalCollision || this.onGround) && !flag1) {
                    if (!this.world.isClient && this.floatTime > 100 && (blockPos.getY() < 1 || blockPos.getY() > this.world.getHeight()) || this.floatTime > 600) {
                        if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS) && blockPos.getY() <= this.world.getHeight())
                            this.dropItem(block);

                        this.remove();
                    }
                } else {
                    BlockState blockState = this.world.getBlockState(blockPos);
                    this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
                    if (blockState.getBlock() != Blocks.MOVING_PISTON) {
                        this.remove();
                        if (!this.destroyedOnLanding) {
                            boolean flag2 = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
                            boolean flag4 = this.blockState.canPlaceAt(this.world, blockPos);
                            if (flag2 && flag4) {
                                if (this.blockState.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockPos).getFluid() == Fluids.WATER)
                                    this.blockState = this.blockState.with(Properties.WATERLOGGED, true);

                                if (this.world.setBlockState(blockPos, this.blockState, 3)) {
                                    if (block instanceof FloatingBlock)
                                        ((FloatingBlock) block).onEndFloating(this.world, blockPos, this.blockState, blockState);

                                    if (this.blockEntityData != null && this.blockState.getBlock().hasBlockEntity()) {
                                        BlockEntity blockEntity = this.world.getBlockEntity(blockPos);
                                        if (blockEntity != null) {
                                            CompoundTag compoundTag = blockEntity.toTag(new CompoundTag());

                                            for (String keyName : this.blockEntityData.getKeys()) {
                                                Tag tag = this.blockEntityData.get(keyName);
                                                if (!"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName))
                                                    compoundTag.put(keyName, tag.copy());
                                            }

                                            blockEntity.fromTag(this.blockState, compoundTag);
                                            blockEntity.markDirty();
                                        }
                                    }
                                } else if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.dropItem(block);
                                }
                            } else if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                this.dropItem(block);
                            }
                        } else if (block instanceof FloatingBlock) {
                            ((FloatingBlock) block).onBroken(this.world, blockPos);
                        }
                    }
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98D));
        }
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(distance - 1.0F);
            if (i > 0) {
                List<Entity> list = Lists.newArrayList(this.world.getOtherEntities(this, this.getBoundingBox()));
                boolean flag = this.blockState.isIn(BlockTags.ANVIL);
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

                for (Entity entity : list)
                    entity.damage(damagesource, Math.min(MathHelper.floor(i * this.floatHurtAmount), this.floatHurtMax));

                if (flag && this.random.nextFloat() < 0.05F + i * 0.05F) {
                    BlockState blockstate = AnvilBlock.getLandingState(this.blockState);
                    if (blockstate == null) this.destroyedOnLanding = true;
                    else this.blockState = blockstate;
                }
            }
        }
        return false;
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag compound) {
        compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
        compound.putInt("Time", this.floatTime);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.floatHurtAmount);
        compound.putInt("FallHurtMax", this.floatHurtMax);
        if (this.blockEntityData != null) compound.put("TileEntityData", this.blockEntityData);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag compound) {
        this.blockState = NbtHelper.toBlockState(compound.getCompound("BlockState"));
        this.floatTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.floatHurtAmount = compound.getFloat("FallHurtAmount");
            this.floatHurtMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");

        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.blockState.isAir()) this.blockState = AetherBlocks.GRAVITITE_ORE.getDefaultState();
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
        section.add("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void setHurtEntities(boolean hurtEntitiesIn) {
        this.hurtEntities = hurtEntitiesIn;
    }

    @Override
    public boolean entityDataRequiresOperator() {
        return true;
    }

    @Override
    public Identifier createSpawnPacket(PacketByteBuf buf) {
        super.createSpawnPacket(buf);
        buf.writeCompoundTag(NbtHelper.fromBlockState(this.getBlockState()));

        return NetworkingHell.SPAWN_FLOATING_BLOCK_ENTITY;
    }


    public static void spawn(PacketContext ctx, PacketByteBuf buf) {
        EntityData data = new EntityData(buf);
        BlockState blockState = NbtHelper.toBlockState(Objects.requireNonNull(buf.readCompoundTag()));

        ctx.getTaskQueue().execute(() -> {
            FloatingBlockEntity entity = new FloatingBlockEntity(ctx.getPlayer().world, data.x, data.y, data.z, blockState);
            entity.updateTrackedPosition(data.x, data.y, data.z);
            entity.refreshPositionAfterTeleport(data.x, data.y, data.z);
            entity.setEntityId(data.id);
            entity.setUuid(data.uuid);
            ((ClientWorld) ctx.getPlayer().world).addEntity(data.id, entity);
        });
    }
}
