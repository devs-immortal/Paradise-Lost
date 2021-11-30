package net.id.aether.blocks.blockentity;

import net.id.aether.util.InventoryWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class AetherBlockEntity extends BlockEntity implements InventoryWrapper, SidedInventory {

    protected final DefaultedList<ItemStack> inventory;
    protected HopperStrategy hopperStrategy;

    public AetherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int invSize, HopperStrategy hopperStrategy) {
        super(type, pos, state);
        inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
        this.hopperStrategy = hopperStrategy;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[inventory.size()];
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return switch (hopperStrategy) {
            case IN_TOP_OUT_BOTTOM -> dir == Direction.UP;
            case IN_ANY_OUT_BOTTOM -> dir != Direction.DOWN;
            case IN_ANY, ALL_PASS -> true;
            default -> false;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return switch (hopperStrategy) {
            case IN_TOP_OUT_BOTTOM, IN_ANY_OUT_BOTTOM -> dir == Direction.DOWN;
            case OUT_ANY, ALL_PASS -> true;
            default -> false;
        };
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    public enum HopperStrategy {
        IN_TOP_OUT_BOTTOM,
        IN_ANY_OUT_BOTTOM,
        IN_ANY,
        OUT_ANY,
        ALL_PASS,
        NO_PASS
    }
}
