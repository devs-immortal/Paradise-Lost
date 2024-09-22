package net.id.paradiselost.blocks.blockentity;

import net.id.paradiselost.component.MoaGenes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class IncubatorBlockEntity extends BlockEntity {

    private UUID owner;
    private int hatchTicks = 100;
    private final DefaultedList<ItemStack> inventory;

    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.INCUBATOR, pos, state);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    public static <T extends BlockEntity> void tickServer(World world, BlockPos pos, BlockState state, T entity) {
        IncubatorBlockEntity incubator = (IncubatorBlockEntity) entity;
        if (incubator.inventory.get(0).getItem() == ParadiseLostItems.MOA_EGG) {
            incubator.hatchTicks--;
            if (incubator.hatchTicks <= 0) {
                incubator.hatchTicks = 0;
                var moa = MoaGenes.getMoaFromEgg(world, incubator.inventory.get(0), incubator.owner);
                moa.refreshPositionAndAngles(pos.getX() + 0.25, pos.getY() + 0.65, pos.getZ() + 0.25, world.getRandom().nextFloat() * 360 - 180, 0);
                world.playSound(null, pos, ParadiseLostSoundEvents.ENTITY_MOA_EGG_HATCH, SoundCategory.BLOCKS, 2F, 0.5F);
                world.spawnEntity(moa);
                incubator.inventory.clear();
            }
        }
    }

    public void handleUse(PlayerEntity player, Hand hand, ItemStack handStack) {
        owner = player.getUuid();
        ItemStack stored = inventory.get(0);
        inventory.set(0, handStack);
        player.setStackInHand(hand, stored);
        hatchTicks = (int) (12000 / world.getBiome(pos).value().getTemperature());
    }

    public boolean hasItem() {
        return !inventory.isEmpty();
    }

    public ItemStack getItem() {
        return inventory.get(0);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("hatchTicks", hatchTicks);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        hatchTicks = nbt.getInt("hatchTicks");
    }
    
}
