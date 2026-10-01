package net.id.paradise_lost.block.blockentity;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.mechanical.PalaceDoorBlock;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PalaceDoorBlockEntity extends BlockEntity {

    private static final float DOOR_OPEN_SPEED = 66F;
    private static final float MAX_DOOR_ANGLE = (float) (Math.PI / 2.0);
    private static final DustParticleOptions DUST_PARTICLE = new DustParticleOptions(new Vector3f(0.69F, 0.659F, 0.627F), 1.0F);

    private boolean doorOpened;
    public boolean doorFullyOpened;
    public long doorOpenTime;

    public PalaceDoorBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.PALACE_DOOR.get(), pos, state);
    }

    public void open() {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, 1);
        }
    }

    @Override
    public boolean triggerEvent(int type, int data) {
        if (this.level != null && type == 1 && data == 0) {
            this.doorFullyOpened = true;
            var openDirection = this.level.getBlockState(this.getBlockPos()).getValue(PalaceDoorBlock.FACING);
            addFinishOpenParticles(openDirection, openDirection.getClockWise(Direction.Axis.Y));
            addFinishOpenParticles(openDirection, openDirection.getCounterClockWise(Direction.Axis.Y));
            setChanged();
            return true;
        } else if (this.level != null && type == 1 && data == 1) {
            this.doorOpened = true;
            this.doorOpenTime = this.level.getGameTime();
            this.level.playLocalSound(this.worldPosition.getX(), this.worldPosition.getY() - 2, this.worldPosition.getZ(), ParadiseLostSoundEvents.BLOCK_PALACE_DOOR_OPEN, SoundSource.BLOCKS, 2.0F, 1.0F, true);
            this.level.playLocalSound(this.worldPosition.getX(), this.worldPosition.getY() - 2, this.worldPosition.getZ(), ParadiseLostSoundEvents.BLOCK_PALACE_DOOR_UNLOCK, SoundSource.BLOCKS, 2.0F, 1.0F, true);
            setChanged();
            return true;
        } else {
            return super.triggerEvent(type, data);
        }
    }

    private void addFinishOpenParticles(Direction openDirection, Direction offset) {
        var random = this.level.getRandom();
        var particleEffect = new BlockParticleOption(ParticleTypes.FALLING_DUST, BlockRegistry.FLOESTONE.get().defaultBlockState());
        var basePos = this.getBlockPos().relative(openDirection);
        var x = basePos.getX() + 0.5F + offset.getStepX() * 1.3F;
        var z = basePos.getZ() + 0.5F + offset.getStepZ() * 1.3F;
        for (int i = 0; i < 10; i++) {
            var y = ((random.nextFloat() * 4) - 1F) + basePos.getY();
            this.level.addParticle(particleEffect, x + randomlyOffset(random, offset.getStepZ()), y, z + randomlyOffset(random, offset.getStepX()), 0.0, -1.0, 0.0);
        }
    }

    private float randomlyOffset(RandomSource random, int pos) {
        var correctedPos = Math.abs((float) pos);
        return (random.nextFloat() * correctedPos) - (correctedPos / 2F);
    }

    public float getDoorAngle() {
        if (this.getLevel() == null) return 0;
        var time = this.getLevel().getGameTime() - this.doorOpenTime;
        if (this.doorFullyOpened) {
            return MAX_DOOR_ANGLE;
        } else if (this.doorOpened) {
            var doorAngle = time / DOOR_OPEN_SPEED;
            if (doorAngle > MAX_DOOR_ANGLE) {
                this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, 0);
                return MAX_DOOR_ANGLE;
            }
            return doorAngle;
        } else {
            return 0;
        }
    }

    public float getConstantAngle() {
        if (this.getLevel() == null) return 0;
        return this.getLevel().getGameTime() - this.doorOpenTime;
    }

    public float getRotation() {
        if (this.getLevel() == null) return 0.0F;
        return PalaceDoorBlock.getRotation(this.getLevel().getBlockState(this.worldPosition)).toYRot();
    }

    public boolean isOpen() {
        return this.doorOpened;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putBoolean("doorOpened", this.doorOpened);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.doorOpened = nbt.getBoolean("doorOpened");
        this.doorFullyOpened = nbt.getBoolean("doorOpened");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        CompoundTag nbtCompound = new CompoundTag();
        this.saveAdditional(nbtCompound, registryLookup);
        return nbtCompound;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
