package com.aether.entities.block;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.FloatingBlock;
import com.aether.entities.AetherEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class FloatingBlockEntity extends Entity {

    private static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(FloatingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private BlockState state;
    private int floatTime;

    public FloatingBlockEntity(World world) {
        super(AetherEntityTypes.FLOATING_BLOCK, world);
        this.state = AetherBlocks.GRAVITITE_ORE.getDefaultState();
    }

    public FloatingBlockEntity(World world, double x, double y, double z, BlockState state) {
        this(world);

        this.state = state;
        //this.inanimate = true;

        this.updatePosition(x, y + (double) ((1.0F - this.getHeight()) / 2.0F), z);
        this.setVelocity(Vec3d.ZERO);

        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;

        this.setOrigin(new BlockPos(this.getPos()));
    }

    public BlockPos getOrigin() {
        return this.dataTracker.get(ORIGIN);
    }

    public void setOrigin(BlockPos p_184530_1_) {
        this.dataTracker.set(ORIGIN, p_184530_1_);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

	/*
	@Override
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

	@Override
	public boolean canTriggerWalking()
    {
        return false;
    }*/

    @Override
    public boolean collides() {
        return !this.removed;
    }

    @Override
    public void tick() {
        if (this.state.isAir()) {
            this.remove();
        } else {
            this.prevX = this.getX();
            this.prevY = this.getY();
            this.prevZ = this.getZ();
            Block block = this.state.getBlock();

            if (this.floatTime++ == 0) {
                BlockPos blockpos = new BlockPos(this.getPos());

                if (this.world.getBlockState(blockpos).getBlock() == block) {
                    this.world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
                } else if (!this.world.isClient) {
                    this.remove();
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0D, 0.04D, 0.0D));
            }

            this.move(MovementType.SELF, this.getVelocity());

            if (!this.world.isClient) {
                BlockPos blockpos1 = new BlockPos(this.getPos());
                boolean flag = this.state.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.world.getFluidState(blockpos1).isIn(FluidTags.WATER);
                double d0 = this.getVelocity().lengthSquared();

                if (flag && d0 > 1.0D) {
                    BlockHitResult raytraceresult = this.world.raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ), new Vec3d(this.getX(), this.getY(), this.getZ()), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.SOURCE_ONLY, this));

                    if (raytraceresult.getType() != HitResult.Type.MISS && this.world.getFluidState(raytraceresult.getBlockPos()).isIn(FluidTags.WATER)) {
                        blockpos1 = raytraceresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onGround && !flag1) {
                    if (this.floatTime > 100 && !this.world.isClient && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.floatTime > 600) {
                        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            this.dropItem(block);
                        }

                        this.remove();
                    }
                } else {
                    if (!flag1 && FloatingBlock.canFloatThrough(this.world.getBlockState(new BlockPos(this.getX(), this.getY() + 0.009999999776482582D, this.getZ())))) {
                        this.onGround = false;
                        return;
                    }

                    this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98D));

            if (this.getVelocity().y == 0.0F) {
                this.world.setBlockState(new BlockPos(this.getPos()), this.getBlockstate());
                this.remove();
            }
        }
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag compound) {
        compound.put("state", NbtHelper.fromBlockState(this.state));
        compound.putInt("time", this.floatTime);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag compound) {
        this.state = NbtHelper.toBlockState(compound.getCompound("state"));
        this.floatTime = compound.getInt("time");

        if (this.state.isAir()) {
            this.state = AetherBlocks.GRAVITITE_ORE.getDefaultState();
        }
    }

    public World getWorldObj() {
        return this.world;
    }

    @Override
    public boolean entityDataRequiresOperator() {
        return true;
    }

    /*
	@Override
	public boolean ignoreItemEntityData()
	{
		return true;
	}*/

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    public BlockState getBlockstate() {
        return this.state;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockstate()));
    }
}
