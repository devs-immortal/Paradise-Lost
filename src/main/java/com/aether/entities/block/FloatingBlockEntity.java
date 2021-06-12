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
import net.minecraft.CrashReportCategory;
import net.minecraft.block.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.math.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.FallingBlock;
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
import java.util.Objects;

public class FloatingBlockEntity extends AetherNonLivingEntity {
    protected static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(FloatingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
    public int floatTime;
    public boolean dropItem;
    public CompoundTag blockEntityData;
    private BlockState blockState;
    private boolean destroyedOnLanding;
    private boolean hurtEntities;
    private int floatHurtMax;
    private float floatHurtAmount;

    public FloatingBlockEntity(EntityType<? extends FloatingBlockEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.blockState = AetherBlocks.GRAVITITE_ORE.defaultBlockState();
        this.dropItem = true;
        this.floatHurtMax = 40;
        this.floatHurtAmount = 2.0F;
    }

    public FloatingBlockEntity(Level world, double x, double y, double z, BlockState floatingBlockState) {
        this(AetherEntityTypes.FLOATING_BLOCK, world);
        this.blockState = floatingBlockState;
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setOrigin(new BlockPos(this.position()));
    }

    @Override
    public void setPos(double x, double y, double z) {
        if (entityData == null) {
            super.setPos(x, y, z);
        } else {
            BlockPos origin = entityData.get(ORIGIN);
            VoxelShape colShape = blockState.getCollisionShape(level, origin);
            if (colShape.isEmpty()) {
                colShape = blockState.getShape(level, origin);
            }
            if (colShape.isEmpty()) {
                super.setPos(x, y, z);
            } else {
                this.setPosRaw(x, y, z);
                AABB box = colShape.bounds();
                this.setBoundingBox(box.move(position().subtract(new Vec3(Mth.lerp(0.5D, box.minX, box.maxX), 0, Mth.lerp(0.5D, box.minZ, box.maxZ)))));
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.entityData.set(ORIGIN, origin);
        this.setPos(getX(), getY(), getZ());
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean canClimb() {
//        return false;
//    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ORIGIN, BlockPos.ZERO);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved() && !blockState.getCollisionShape(level, entityData.get(ORIGIN)).isEmpty();
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isPickable();
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return !(other instanceof FloatingBlockEntity) && super.canCollideWith(other);
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
                blockPos = this.blockPosition();
                if (this.level.getBlockState(blockPos).is(block)) {
                    this.level.removeBlock(blockPos, false);
                } else if (!this.level.isClientSide) {
                    this.discard();
                    return;
                }
            }

            boolean isFastFloater = (this.blockState.getBlock() == AetherBlocks.GRAVITITE_ORE || this.blockState.getBlock() == AetherBlocks.GRAVITITE_LEVITATOR || this.blockState.getBlock() == AetherBlocks.BLOCK_OF_GRAVITITE);
            if (!this.isNoGravity()) {
                if (isFastFloater) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
                } else {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.03D, 0.0D));
                }
            }

            AABB oldBox = getBoundingBox();

            this.move(MoverType.SELF, this.getDeltaMovement());
            if (!FallingBlock.isFree(this.blockState)) {
                AABB newBox = getBoundingBox();
                List<Entity> otherEntities = this.level.getEntities(this, oldBox.minmax(newBox));
                for (Entity entity : otherEntities) {
                    if (!(entity instanceof FloatingBlockEntity) && !entity.noPhysics) {
                        if (entity.getY() < newBox.maxY) {
                            entity.setPos(entity.position().x, newBox.maxY, entity.position().z);
                        }
                    }
                }
            }

            if (!this.level.isClientSide) {
                blockPos = this.blockPosition();
                boolean isConcrete = this.blockState.getBlock() instanceof ConcretePowderBlock;
                boolean shouldSolidify = isConcrete && this.level.getFluidState(blockPos).is(FluidTags.WATER);
                double speed = this.getDeltaMovement().lengthSqr();

                if (isConcrete && speed > 1.0D) {
                    BlockHitResult blockHitResult = this.level
                            .clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo),
                                    new Vec3(this.getX(), this.getY(), this.getZ()), ClipContext.Block.COLLIDER,
                                    ClipContext.Fluid.SOURCE_ONLY, this));

                    if (blockHitResult.getType() != HitResult.Type.MISS
                            && this.level.getFluidState(blockHitResult.getBlockPos()).is(FluidTags.WATER)) {
                        blockPos = blockHitResult.getBlockPos();
                        shouldSolidify = true;
                    }
                }

                if (!this.verticalCollision && !shouldSolidify) {
                    if (!this.level.isClientSide) {
                        if (this.floatTime > 100 && blockPos.getY() > this.level.getHeight()+64) {
                            if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS) && this.level.hasNearbyAlivePlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 4)) {
                                this.spawnAtLocation(block);
                            }
                            this.discard();
                        }
                    }
                } else {
                    BlockState blockState = this.level.getBlockState(blockPos);
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, 0.5, 0.7));
                    if (blockState.getBlock() != Blocks.MOVING_PISTON) {
                        this.discard();
                        if (!this.destroyedOnLanding) {
                            boolean canReplace = blockState.canBeReplaced(new DirectionalPlaceContext(this.level, blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            if (!canReplace) {
                                canReplace = blockState.canBeReplaced(new DirectionalPlaceContext(this.level, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
                            }
                            boolean canPlace = this.blockState.canSurvive(this.level, blockPos);

                            if (canReplace && canPlace) {
                                if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level.getFluidState(blockPos).getType() == Fluids.WATER)
                                    this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);

                                if (this.level.setBlock(blockPos, this.blockState, 3)) {
                                    if (block instanceof FloatingBlock)
                                        ((FloatingBlock) block).onEndFloating(this.level, blockPos, this.blockState, blockState);

                                    if (this.blockEntityData != null/* && this.blockState.getBlock().hasBlockEntity()*/) {
                                        BlockEntity blockEntity = this.level.getBlockEntity(blockPos);
                                        if (blockEntity != null) {
                                            CompoundTag compoundTag = blockEntity.save(new CompoundTag());

                                            for (String keyName : this.blockEntityData.getAllKeys()) {
                                                Tag tag = this.blockEntityData.get(keyName);
                                                if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
                                                    compoundTag.put(keyName, tag.copy());
                                                }
                                            }

                                            blockEntity.load(compoundTag);
                                            blockEntity.setChanged();
                                        }
                                    }
                                } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.spawnAtLocation(block);
                                }
                            } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.spawnAtLocation(block);
                            }
                        } else if (block instanceof FloatingBlock) {
                            ((FloatingBlock) block).onBroken(this.level, blockPos);
                        }
                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean handleFallDamage(float distance, float damageMultiplier) {
//        if (this.hurtEntities) {
//            int i = MathHelper.ceil(distance - 1.0F);
//            if (i > 0) {
//                List<Entity> list = Lists.newArrayList(this.world.getOtherEntities(this, this.getBoundingBox()));
//                boolean flag = this.blockState.isIn(BlockTags.ANVIL);
//                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
//
//                for (Entity entity : list)
//                    entity.damage(damagesource, Math.min(MathHelper.floor(i * this.floatHurtAmount), this.floatHurtMax));
//
//                if (flag && this.random.nextFloat() < 0.05F + i * 0.05F) {
//                    BlockState blockstate = AnvilBlock.getLandingState(this.blockState);
//                    if (blockstate == null) this.destroyedOnLanding = true;
//                    else this.blockState = blockstate;
//                }
//            }
//        }
//        return false;
//    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        compound.putInt("Time", this.floatTime);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.floatHurtAmount);
        compound.putInt("FallHurtMax", this.floatHurtMax);
        if (this.blockEntityData != null) compound.put("TileEntityData", this.blockEntityData);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.blockState = NbtUtils.readBlockState(compound.getCompound("BlockState"));
        this.floatTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.floatHurtAmount = compound.getFloat("FallHurtAmount");
            this.floatHurtMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");

        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.blockState.isAir()) this.blockState = AetherBlocks.GRAVITITE_ORE.defaultBlockState();
    }

    @Environment(EnvType.CLIENT)
    public Level getWorldObj() {
        return this.level;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory section) {
        super.fillCrashReportCategory(section);
        section.setDetail("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void setHurtEntities(boolean hurtEntitiesIn) {
        this.hurtEntities = hurtEntitiesIn;
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public ResourceLocation createSpawnPacket(FriendlyByteBuf buf) {
        super.createSpawnPacket(buf);
        buf.writeNbt(NbtUtils.writeBlockState(this.getBlockState()));

        return NetworkingHell.SPAWN_FLOATING_BLOCK_ENTITY;
    }


    public static void spawn(PacketContext ctx, FriendlyByteBuf buf) {
        EntityData data = new EntityData(buf);
        BlockState blockState = NbtUtils.readBlockState(Objects.requireNonNull(buf.readNbt()));

        ctx.getTaskQueue().execute(() -> {
            FloatingBlockEntity entity = new FloatingBlockEntity(ctx.getPlayer().level, data.x, data.y, data.z, blockState);
            entity.setPacketCoordinates(data.x, data.y, data.z);
            entity.moveTo(data.x, data.y, data.z);
            entity.setId(data.id);
            entity.setUUID(data.uuid);
            ((ClientLevel) ctx.getPlayer().level).putNonPlayerEntity(data.id, entity);
        });
    }

    public static boolean gravititeToolUsedOnBlock(UseOnContext context, Item item) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        if ((!blockState.requiresCorrectToolForDrops() || item.isCorrectToolForDrops(blockState)) && FallingBlock.isFree(world.getBlockState(blockPos.above()))) {
            Player playerEntity = context.getPlayer();
            if (!world.isClientSide) {
                FloatingBlockEntity floatingblockentity = new FloatingBlockEntity(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, world.getBlockState(blockPos));
                world.addFreshEntity(floatingblockentity);
            } else {
                world.playSound(playerEntity, blockPos, blockState.getBlock().getSoundType(blockState).getBreakSound(), SoundSource.BLOCKS, 1.0F, 0.75F);
            }
            if (playerEntity != null) {
                context.getItemInHand().hurtAndBreak(1, playerEntity, (p) -> p.broadcastBreakEvent(context.getHand()));
            }
            return true;
        }
        return false;
    }

    public interface ICPEM {
        void postTick();
    }
}
