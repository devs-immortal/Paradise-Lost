package net.id.paradise_lost.block.blockentity;

import net.minecraft.tags.ItemTags;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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
        if (!storedFood.isEmpty() && (handStack.isEmpty() || !handStack.equals(storedFood))) {
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
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(FoodBowlBlock.FULL, !inventory.get(0).isEmpty()));
    }

    public ItemStack getContainedItem() {
        return inventory.get(0);
    }
}
