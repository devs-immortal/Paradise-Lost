package net.id.paradiselost.blocks.blockentity;

import net.id.paradiselost.component.MoaGenes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class IncubatorBlockEntity extends BlockEntity {

    private UUID owner;
    private int hatchTicks = 100;
    private ItemStack egg;

    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.INCUBATOR, pos, state);
        this.egg = ItemStack.EMPTY;
    }

    public static <T extends BlockEntity> void tickServer(World world, BlockPos pos, BlockState state, T entity) {
        IncubatorBlockEntity incubator = (IncubatorBlockEntity) entity;
        if (incubator.egg.getItem() == ParadiseLostItems.MOA_EGG) {
            incubator.hatchTicks--;
            if (incubator.hatchTicks <= 0) {
                incubator.hatchTicks = 0;
                var moa = MoaGenes.getMoaFromEgg(world, incubator.egg, incubator.owner);
                moa.refreshPositionAndAngles(pos.getX() + 0.25, pos.getY() + 0.65, pos.getZ() + 0.25, world.getRandom().nextFloat() * 360 - 180, 0);
                world.playSound(null, pos, ParadiseLostSoundEvents.ENTITY_MOA_EGG_HATCH, SoundCategory.BLOCKS, 2F, 0.5F);
                world.spawnEntity(moa);
                incubator.egg = ItemStack.EMPTY;
                incubator.markDirty();
            }
        }
    }

    public void handleUse(PlayerEntity player, Hand hand, ItemStack handStack) {
        owner = player.getUuid();
        ItemStack stored = egg.copy();
        egg = handStack.copy();
        player.setStackInHand(hand, stored);
        hatchTicks = (int) (12000 / world.getBiome(pos).value().getTemperature());
    }

    public boolean hasItem() {
        return !egg.isEmpty();
    }

    public ItemStack getItem() {
        return egg;
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!egg.isEmpty()) {
            nbt.put("egg", egg.encode(registryLookup));
        }
        nbt.putInt("hatchTicks", hatchTicks);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("egg")) {
            egg = ItemStack.fromNbt(registryLookup, nbt.get("egg")).get();
        } else {
            egg = ItemStack.EMPTY;
        }

        hatchTicks = nbt.getInt("hatchTicks");
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
