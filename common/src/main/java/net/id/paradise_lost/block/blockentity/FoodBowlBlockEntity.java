package net.id.paradise_lost.block.blockentity;

import net.minecraft.tags.ItemTags;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.id.paradise_lost.util.PlayerInventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
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
    public boolean handleUse(Player player, InteractionHand hand, ItemStack handStack, boolean dumpStack) {
        ItemStack storedFood = inventory.getFirst();
        if (!storedFood.isEmpty() && (handStack.isEmpty() || !ItemStack.isSameItemSameComponents(handStack, storedFood))) {
            ItemStack toReturn = storedFood.copy();
            inventory.clear();
            PlayerInventoryHelper.giveBack(player, hand, toReturn);
            updateState();
            return true;
        }

        if (handStack.is(ItemTags.MEAT)) {
            int space = storedFood.isEmpty()
                    ? handStack.getMaxStackSize()
                    : storedFood.getMaxStackSize() - storedFood.getCount();
            if (space <= 0) {
                return false;
            }

            int toDeposit = dumpStack ? Math.min(space, handStack.getCount()) : 1;
            if (storedFood.isEmpty()) {
                inventory.set(0, handStack.copyWithCount(toDeposit));
            } else {
                storedFood.grow(toDeposit);
            }
            if (!player.getAbilities().instabuild) {
                handStack.shrink(toDeposit);
            }
            updateState();
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    public void updateState() {
        setChanged();
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(FoodBowlBlock.FULL, !inventory.getFirst().isEmpty()));
    }

    public ItemStack getContainedItem() {
        return inventory.getFirst();
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
