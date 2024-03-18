package net.id.paradiselost.blocks.blockentity;

import net.id.incubus_core.be.InventoryBlockEntity;
import net.id.paradiselost.recipe.ParadiseLostRecipeTypes;
import net.id.paradiselost.recipe.TreeTapRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TreeTapBlockEntity extends BlockEntity implements InventoryBlockEntity {

    private final DefaultedList<ItemStack> inventory;

    public TreeTapBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.TREE_TAP, pos, state);
        inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

	public void handleUse(PlayerEntity player, Hand hand, ItemStack handStack) {
        ItemStack stored = inventory.get(0);
        if (!handStack.isEmpty() && inventory.get(0).isEmpty()) {
            inventory.set(0, handStack.split(1));
        } else {
            player.giveItemStack(stored);
            inventory.set(0, ItemStack.EMPTY);
        }
        markDirty();
	}

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.getHopperStrategy().canInsert(dir) && this.inventory.get(0).isEmpty();
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return this.getHopperStrategy().canExtract(dir);
    }

    @Override
    public @NotNull HopperStrategy getHopperStrategy() {
        return HopperStrategy.IN_ANY_OUT_BOTTOM;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > 1) {
            stack.setCount(1);
        }
        inventoryChanged();
        System.out.println(getItems());
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack stack = Inventories.splitStack(getItems(), slot, count);
        inventoryChanged();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        var stack = Inventories.removeStack(getItems(), slot);
        inventoryChanged();
        return stack;
    }

    private void inventoryChanged() {
        this.markDirty();
        if (world != null && !world.isClient) updateInClientWorld();
    }

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventory);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
	}

	public BlockState getTappedState() {
		return this.world.getBlockState(this.pos.offset(getCachedState().get(Properties.HORIZONTAL_FACING).getOpposite()));
	}

	public void tryCraft() {
		ItemStack stack = getStack(0);
		if (stack.isEmpty()) {
			return;
		}

		Optional<TreeTapRecipe> recipe = this.world.getRecipeManager().getFirstMatch(ParadiseLostRecipeTypes.TREE_TAP_RECIPE_TYPE, this, this.world);
		if (recipe.isPresent()) {
			ItemStack output = recipe.get().craft(this);
			stack.decrement(1);

			// TODO: play a sound?
			if (stack.isEmpty()) {
				this.inventory.set(0, output);
				updateInClientWorld();
			} else {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), output);
			}
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}

}
