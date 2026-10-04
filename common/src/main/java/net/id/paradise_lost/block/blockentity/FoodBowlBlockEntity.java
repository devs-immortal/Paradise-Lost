package net.id.paradise_lost.block.blockentity;

import net.minecraft.tags.ItemTags;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoodBowlBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> inventory;

    public FoodBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.FOOD_BOWL.get(), pos, state);
        inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean handleUse(Player player, InteractionHand hand, ItemStack handStack) {
        ItemStack storedFood = inventory.get(0);
        if (!storedFood.isEmpty() && (handStack.isEmpty() || !ItemStack.isSameItemSameComponents(handStack, storedFood))) {
            if (!player.getInventory().add(storedFood)) {
                level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 0.75, worldPosition.getZ(), storedFood, 0, 0, 0));
            }
            inventory.clear();
            updateState();
            return true;
        }

        if (handStack.is(ItemTags.MEAT)) {
            if (storedFood.isEmpty()) {
                inventory.set(0, handStack);
                player.setItemInHand(hand, ItemStack.EMPTY);
            } else {
                int overflow = (storedFood.getCount() + handStack.getCount()) - 64;
                storedFood.setCount(Math.min(64 + overflow, 64));
                handStack.setCount(Math.max(overflow, 0));
            }
            updateState();
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    private void updateState() {
        setChanged();
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(FoodBowlBlock.FULL, !inventory.get(0).isEmpty()));
    }

    public ItemStack getContainedItem() {
        return inventory.get(0);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        inventory.clear();
        ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
    }
}
