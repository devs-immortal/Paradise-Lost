package net.id.paradiselost.blocks.blockentity;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.mechanical.PalaceDoorBlock;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PalaceDoorBlockEntity extends BlockEntity {

    private static final float DOOR_OPEN_SPEED = 66F;
    private static final float MAX_DOOR_ANGLE = (float) (Math.PI / 2.0);
    private static final DustParticleEffect DUST_PARTICLE = new DustParticleEffect(new Vector3f(0.69F, 0.659F, 0.627F), 1.0F);

    private boolean doorOpened;
    public boolean doorFullyOpened;
    public long doorOpenTime;

    public PalaceDoorBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.PALACE_DOOR, pos, state);
    }

    public void open() {
        if (this.world != null && !this.world.isClient()) {
            this.world.addSyncedBlockEvent(this.getPos(), this.getCachedState().getBlock(), 1, 1);
        }
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (this.world != null && type == 1 && data == 0) {
            this.doorFullyOpened = true;
            var openDirection = this.world.getBlockState(this.getPos()).get(PalaceDoorBlock.FACING);
            addFinishOpenParticles(openDirection, openDirection.rotateClockwise(Direction.Axis.Y));
            addFinishOpenParticles(openDirection, openDirection.rotateCounterclockwise(Direction.Axis.Y));
            markDirty();
            return true;
        } else if (this.world != null && type == 1 && data == 1) {
            this.doorOpened = true;
            this.doorOpenTime = this.world.getTime();
            this.world.playSound(this.pos.getX(), this.pos.getY() - 2, this.pos.getZ(), ParadiseLostSoundEvents.BLOCK_PALACE_DOOR_OPEN, SoundCategory.BLOCKS, 2.0F, 1.0F, true);
            this.world.playSound(this.pos.getX(), this.pos.getY() - 2, this.pos.getZ(), ParadiseLostSoundEvents.BLOCK_PALACE_DOOR_UNLOCK, SoundCategory.BLOCKS, 2.0F, 1.0F, true);
            markDirty();
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    private void addFinishOpenParticles(Direction openDirection, Direction offset) {
        var random = this.world.getRandom();
        var particleEffect = new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, ParadiseLostBlocks.FLOESTONE.getDefaultState());
        var basePos = this.getPos().offset(openDirection);
        var x = basePos.getX() + 0.5F + offset.getOffsetX() * 1.3F;
        var z = basePos.getZ() + 0.5F + offset.getOffsetZ() * 1.3F;
        for (int i = 0; i < 10; i++) {
            var y = ((random.nextFloat() * 4) - 1F) + basePos.getY();
            this.world.addParticle(particleEffect, x + randomlyOffset(random, offset.getOffsetZ()), y, z + randomlyOffset(random, offset.getOffsetX()), 0.0, -1.0, 0.0);
        }
    }

    private float randomlyOffset(Random random, int pos) {
        var correctedPos = Math.abs((float) pos);
        return (random.nextFloat() * correctedPos) - (correctedPos / 2F);
    }

    public float getDoorAngle() {
        if (this.getWorld() == null) return 0;
        var time = this.getWorld().getTime() - this.doorOpenTime;
        if (this.doorFullyOpened) {
            return MAX_DOOR_ANGLE;
        } else if (this.doorOpened) {
            var doorAngle = time / DOOR_OPEN_SPEED;
            if (doorAngle > MAX_DOOR_ANGLE) {
                this.world.addSyncedBlockEvent(this.getPos(), this.getCachedState().getBlock(), 1, 0);
                return MAX_DOOR_ANGLE;
            }
            return doorAngle;
        } else {
            return 0;
        }
    }

    // DO NOT REMOVE: FOR TESTING
    public float getConstantAngle() {
        if (this.getWorld() == null) return 0;
        return this.getWorld().getTime() - this.doorOpenTime;
    }

    public float getRotation() {
        if (this.getWorld() == null) return 0.0F;
        return PalaceDoorBlock.getRotation(this.getWorld().getBlockState(this.pos)).asRotation();
    }

    public boolean isOpen() {
        return this.doorOpened;
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putBoolean("doorOpened", this.doorOpened);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.doorOpened = nbt.getBoolean("doorOpened");
        this.doorFullyOpened = nbt.getBoolean("doorOpened");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeNbt(nbtCompound, registryLookup);
        return nbtCompound;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

}
