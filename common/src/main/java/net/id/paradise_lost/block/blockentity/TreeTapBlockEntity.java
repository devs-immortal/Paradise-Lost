package net.id.paradise_lost.block.blockentity;

import net.id.paradise_lost.block.mechanical.TreeTapBlock;
import net.id.paradise_lost.recipe.ParadiseLostRecipeTypes;
import net.id.paradise_lost.recipe.TreeTapRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TreeTapBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    private final NonNullList<ItemStack> inventory;
    private final RecipeManager.CachedCheck<RecipeInput, TreeTapRecipe> matchGetter;

    public TreeTapBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.TREE_TAP.get(), pos, state);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        this.matchGetter = RecipeManager.createCheck(ParadiseLostRecipeTypes.TREE_TAP_RECIPE_TYPE);
    }

	public void handleUse(Player player, InteractionHand hand, ItemStack handStack) {
        ItemStack stored = inventory.getFirst();
        if (!handStack.isEmpty() && stored.isEmpty()) {
            inventory.set(0, handStack.split(1));
        } else {
            player.addItem(stored);
            inventory.set(0, ItemStack.EMPTY);
        }
        setChanged();
	}

    public int[] getSlotsForFace(Direction side) {
        return new int[1];
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir != Direction.DOWN && this.inventory.get(0).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    public NonNullList<ItemStack> retrieveItems() {
        return inventory;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        retrieveItems().set(slot, stack);
        if (stack.getCount() > 1) {
            stack.setCount(1);
        }
        inventoryChanged();
    }

    private void inventoryChanged() {
        setChanged();
        if (level != null && !level.isClientSide) updateInClientWorld();
    }

	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
        this.inventory.clear();
		ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
	}

	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
	}

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        inventory.set(0, inventory.get(0));
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return null;
    }

    public BlockState getTappedState() {
		return this.level.getBlockState(this.worldPosition.relative(getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite()));
	}

	public void tryCraft() {
		ItemStack stack = getItem(0);
		if (stack.isEmpty()) {
			return;
		}

		RecipeInput input = asRecipeInput();
		BlockState tappedState = this.getTappedState();
		Optional<RecipeHolder<TreeTapRecipe>> recipe = this.matchGetter.getRecipeFor(input, this.getLevel())
				.filter(holder -> holder.value().matches(input, tappedState));
		if (recipe.isPresent() && level.random.nextInt(recipe.get().value().getChance()) == 0) {
			ItemStack output = recipe.get().value().assemble(input, level.registryAccess());
            Block convertBlock = recipe.get().value().getOutputBlock();
            BlockPos attachedPos = this.worldPosition.relative(level.getBlockState(this.worldPosition).getValue(TreeTapBlock.FACING).getOpposite());
            BlockState attachedBlock = level.getBlockState(attachedPos);
            if (convertBlock != Blocks.BEE_NEST) {
                stack.shrink(1);

                if (convertBlock != level.getBlockState(attachedPos).getBlock()) {
                    level.setBlockAndUpdate(attachedPos, convertBlock.defaultBlockState());
                }
                if (!level.isClientSide) level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, level.getRandom().nextFloat() * 0.4f + 0.8f);

                this.inventory.set(0, output);
                inventoryChanged();
            } else if (attachedBlock.getValue(BeehiveBlock.HONEY_LEVEL) == 5) {
                stack.shrink(1);

                level.setBlockAndUpdate(attachedPos, attachedBlock.setValue(BeehiveBlock.HONEY_LEVEL, 0));

                if (!level.isClientSide) level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, level.getRandom().nextFloat() * 0.4f + 0.8f);

                this.inventory.set(0, output);
                inventoryChanged();
            }
		}
        tryTansferItemsOut();
	}

    public void tryTansferItemsOut() {
        ItemStack stack = getItem(0);
        if (stack.isEmpty()) {
            return;
        }

        ItemStack contents = this.inventory.get(0);
        BlockEntity possibleHopper = level.getBlockEntity(worldPosition.below());
        if (possibleHopper instanceof Container) {
            contents = HopperBlockEntity.addItem(this, (Container) possibleHopper, contents, Direction.UP);
        }
        this.inventory.set(0, contents);
        inventoryChanged();
    }

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void updateInClientWorld() {
		((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
	}

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    public int size() {
        return 1;
    }

    private RecipeInput asRecipeInput() {
        return new TreeTapRecipe.TappedInput() {
            @Override
            public ItemStack getItem(int slot) {
                return TreeTapBlockEntity.this.getItem(slot);
            }

            @Override
            public int size() {
                return TreeTapBlockEntity.this.size();
            }

            @Override
            public BlockState getTappedState() {
                return TreeTapBlockEntity.this.getTappedState();
            }
        };
    }
}
