package net.id.paradiselost.blocks.blockentity;

import net.id.paradiselost.blocks.mechanical.FoodBowlBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class FoodBowlBlockEntity extends BlockEntity {
    private final DefaultedList<ItemStack> inventory;

    public FoodBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.FOOD_BOWL, pos, state);
        inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean handleUse(PlayerEntity player, Hand hand, ItemStack handStack) {
        ItemStack storedFood = inventory.get(0);
        if (!storedFood.isEmpty() && (handStack.isEmpty() || !handStack.isItemEqual(storedFood))) {
            if (!player.getInventory().insertStack(storedFood)) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 0.75, pos.getZ(), storedFood, 0, 0, 0));
            }
            inventory.clear();
            updateState();
            return true;
        }

        Item food = handStack.getItem();

        if (food.isFood() && food.getFoodComponent().isMeat()) {
            if (storedFood.isEmpty()) {
                inventory.set(0, handStack);
                player.setStackInHand(hand, ItemStack.EMPTY);
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
        world.setBlockState(pos, getCachedState().with(FoodBowlBlock.FULL, !inventory.get(0).isEmpty()));
    }

    public ItemStack getContainedItem() {
        return inventory.get(0);
    }
}
