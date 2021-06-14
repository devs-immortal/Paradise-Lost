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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class FloatingBlockEntity extends AetherNonLivingEntity {
    protected static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(FloatingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    public int floatTime;
    public boolean dropItem;
    public NbtCompound blockEntityData;
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
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setOrigin(new BlockPos(this.getPos()));
    }

    @Override
    public void setPosition(double x, double y, double z) {
        if (dataTracker == null || blockState == null) {
            super.setPosition(x, y, z);
        } else {
            BlockPos origin = dataTracker.get(ORIGIN);
            VoxelShape colShape = blockState.getCollisionShape(world, origin);
            if (colShape.isEmpty()) {
                colShape = blockState.getOutlineShape(world, origin);
            }
            if (colShape.isEmpty()) {
                super.setPosition(x, y, z);
            } else {
                this.setPos(x, y, z);
                Box box = colShape.getBoundingBox();
                this.setBoundingBox(box.offset(getPos().subtract(new Vec3d(MathHelper.lerp(0.5D, box.minX, box.maxX), 0, MathHelper.lerp(0.5D, box.minZ, box.maxZ)))));
            }
        }
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

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean canClimb() {
//        return false;
//    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

    @Override
    public boolean collides() {
        return !this.isRemoved() && !blockState.getCollisionShape(world, dataTracker.get(ORIGIN)).isEmpty();
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
    public void tick() {

    }

    /**
     * Because this entity moves other entities, including the player, this entity has
     * to tick after the all other entities have ticked to prevent them phasing though this
     */
    public void postTickEntities() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            BlockPos blockPos;
            if (this.floatTime++ == 0) {
                blockPos = this.getBlockPos();
                if (this.world.getBlockState(blockPos).isOf(block)) {
                    this.world.removeBlock(blockPos, false);
                } else if (!this.world.isClient) {
                    this.discard();
                    return;
                }
            }

            boolean isFastFloater = (this.blockState.getBlock() == AetherBlocks.GRAVITITE_ORE || this.blockState.getBlock() == AetherBlocks.GRAVITITE_LEVITATOR || this.blockState.getBlock() == AetherBlocks.BLOCK_OF_GRAVITITE);
            if (!this.hasNoGravity()) {
                if (isFastFloater) {
                    this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));
                } else {
                    this.setVelocity(this.getVelocity().add(0.0D, 0.03D, 0.0D));
                }
            }

            Box oldBox = getBoundingBox();

            this.move(MovementType.SELF, this.getVelocity());
            if (!FallingBlock.canFallThrough(this.blockState)) {
                Box newBox = getBoundingBox();
                List<Entity> otherEntities = this.world.getOtherEntities(this, oldBox.union(newBox));
                for (Entity entity : otherEntities) {
                    if (!(entity instanceof FloatingBlockEntity) && !entity.noClip) {
                        if (entity.getY() < newBox.maxY) {
                            entity.setPosition(entity.getPos().x, newBox.maxY, entity.getPos().z);
                        }
                    }
                }
            }

            if (!this.world.isClient) {
                blockPos = this.getBlockPos();
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

                if (!this.verticalCollision && !shouldSolidify) {
                    if (!this.world.isClient) {
                        if (this.floatTime > 100 && blockPos.getY() > this.world.getHeight()+64) {
                            if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS) && this.world.isPlayerInRange(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 4)) {
                                this.dropItem(block);
                            }
                            this.discard();
                        }
                    }
                } else {
                    BlockState blockState = this.world.getBlockState(blockPos);
                    this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
                    if (blockState.getBlock() != Blocks.MOVING_PISTON) {
                        this.discard();
                        if (!this.destroyedOnLanding) {
                            boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            if (!canReplace) {
                                canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
                            }
                            boolean canPlace = this.blockState.canPlaceAt(this.world, blockPos);

                            if (canReplace && canPlace) {
                                if (this.blockState.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockPos).getFluid() == Fluids.WATER)
                                    this.blockState = this.blockState.with(Properties.WATERLOGGED, true);

                                if (this.world.setBlockState(blockPos, this.blockState, 3)) {
                                    if (block instanceof FloatingBlock)
                                        ((FloatingBlock) block).onEndFloating(this.world, blockPos, this.blockState, blockState);

                                    if (this.blockEntityData != null/* && this.blockState.getBlock().hasBlockEntity()*/) {
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
    public boolean handleFallDamage(float distance, float multiplier, DamageSource damageSource) {
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
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
        compound.putInt("Time", this.floatTime);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.floatHurtAmount);
        compound.putInt("FallHurtMax", this.floatHurtMax);
        if (this.blockEntityData != null) compound.put("TileEntityData", this.blockEntityData);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
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
        buf.writeNbt(NbtHelper.fromBlockState(this.getBlockState()));

        return NetworkingHell.SPAWN_FLOATING_BLOCK_ENTITY;
    }


    public static void spawn(PacketContext ctx, PacketByteBuf buf) {
        EntityData data = new EntityData(buf);
        BlockState blockState = NbtHelper.toBlockState(Objects.requireNonNull(buf.readNbt()));

        ctx.getTaskQueue().execute(() -> {
            FloatingBlockEntity entity = new FloatingBlockEntity(ctx.getPlayer().world, data.x, data.y, data.z, blockState);
            entity.updateTrackedPosition(data.x, data.y, data.z);
            entity.refreshPositionAfterTeleport(data.x, data.y, data.z);
            entity.setId(data.id);
            entity.setUuid(data.uuid);
            ((ClientWorld) ctx.getPlayer().world).addEntity(data.id, entity);
        });
    }

    public static boolean gravititeToolUsedOnBlock(ItemUsageContext context, Item item) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if ((!blockState.isToolRequired() || item.isSuitableFor(blockState)) && FallingBlock.canFallThrough(world.getBlockState(blockPos.up()))) {
            PlayerEntity playerEntity = context.getPlayer();
            if (!world.isClient) {
                FloatingBlockEntity floatingblockentity = new FloatingBlockEntity(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, world.getBlockState(blockPos));
                world.spawnEntity(floatingblockentity);
            } else {
                world.playSound(playerEntity, blockPos, blockState.getBlock().getSoundGroup(blockState).getBreakSound(), SoundCategory.BLOCKS, 1.0F, 0.75F);
            }
            if (playerEntity != null) {
                context.getStack().damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));
            }
            return true;
        }
        return false;
    }

    public interface ICPEM {
        void postTick();
    }
}
