package net.id.paradiselost.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PalaceDoorBlockEntity extends BlockEntity {

    private static final float DOOR_OPEN_SPEED = 30F;

    private enum DoorOpenDirection {
        Closed,
        Out,
        In
    }

    private DoorOpenDirection doorOpened;
    public long doorOpenTime;

    public PalaceDoorBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.PALACE_DOOR, pos, state);
        this.doorOpened = DoorOpenDirection.Closed;
    }

    public void open() {
        if (this.world != null && !this.world.isClient()) {
            this.world.addSyncedBlockEvent(this.getPos(), this.getCachedState().getBlock(), 1, DoorOpenDirection.Out.ordinal());
        }
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (this.world != null && type == 1) {
            this.doorOpened = DoorOpenDirection.values()[data];
            this.doorOpenTime = this.world.getTime();
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    public float getDoorAngle() {
        if (this.getWorld() == null || this.doorOpened == DoorOpenDirection.Closed) return 0;
        if (this.doorOpened == DoorOpenDirection.In) {
            var time = this.getWorld().getTime() - this.doorOpenTime;
            return (float) Math.min(time/DOOR_OPEN_SPEED, Math.PI/2.0);
        } else {
            var time = this.getWorld().getTime() - this.doorOpenTime;
            return -(float) Math.min(time/DOOR_OPEN_SPEED, Math.PI/2.0);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
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
