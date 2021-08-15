package com.aether.blocks.blockentity;

import com.aether.component.MoaGenes;
import com.aether.items.AetherItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IncubatorBlockEntity extends AetherBlockEntity {

    private int hatchTicks = 100;

    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(AetherBlockEntityTypes.INCUBATOR, pos, state, 1, HopperStrategy.ALL_PASS);
    }

    public void handleUse(PlayerEntity player, Hand hand, ItemStack handStack) {
        ItemStack stored = inventory.get(0);
        inventory.set(0, handStack);
        player.setStackInHand(hand, stored);
        hatchTicks = (int) (12000 / world.getBiome(pos).getTemperature());
    }

    public static <T extends BlockEntity> void tickServer(World world, BlockPos pos, BlockState state, T entity) {
        IncubatorBlockEntity incubator = (IncubatorBlockEntity) entity;
        if(incubator.inventory.get(0).getItem() == AetherItems.MOA_EGG) {
            incubator.hatchTicks--;
            if(incubator.hatchTicks <= 0) {
                incubator.hatchTicks = 0;
                var moa = MoaGenes.getMoaFromEgg(world, incubator.inventory.get(0));
                moa.refreshPositionAndAngles(pos.getX() + 0.25, pos.getY() + 0.65, pos.getZ() + 0.25, world.getRandom().nextFloat() * 360 - 180, 0);
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 2F, 0.5F);
                world.spawnEntity(moa);
                incubator.inventory.clear();
            }
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("hatchTicks", hatchTicks);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        hatchTicks = nbt.getInt("hatchTicks");
    }
}
