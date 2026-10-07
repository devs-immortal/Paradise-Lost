package net.id.paradise_lost.block.blockentity;

import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CherineCampfireBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> itemsBeingCooked;
    private final int[] cookingTimes;
    private final int[] cookingTotalTimes;
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> matchGetter;

    public CherineCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), pos, state);
        this.itemsBeingCooked = NonNullList.withSize(4, ItemStack.EMPTY);
        this.cookingTimes = new int[4];
        this.cookingTotalTimes = new int[4];
        this.matchGetter = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    public static void litServerTick(ServerLevel world, BlockPos pos, BlockState state, CherineCampfireBlockEntity campfire) {
        boolean bl = false;

        for (int i = 0; i < campfire.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = campfire.itemsBeingCooked.get(i);
            if (!itemStack.isEmpty()) {
                bl = true;
                ++campfire.cookingTimes[i];
                if (campfire.cookingTimes[i] >=campfire.cookingTotalTimes[i]) {
                    SingleRecipeInput singleStackRecipeInput = new SingleRecipeInput(itemStack);
                    ItemStack itemStack2 = campfire.matchGetter.getRecipeFor(singleStackRecipeInput, world).map((recipe) -> {
                        return (recipe.value()).assemble(singleStackRecipeInput, world.registryAccess());
                    }).orElse(itemStack);
                    if (itemStack2.isItemEnabled(world.enabledFeatures())) {
                        Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                        campfire.itemsBeingCooked.set(i, ItemStack.EMPTY);
                        world.sendBlockUpdated(pos, state, state, 3);
                        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                    }
                }
            }
        }

        if (bl) {
            setChanged(world, pos, state);
        }

    }

    public static void unlitServerTick(Level world, BlockPos pos, BlockState state, CherineCampfireBlockEntity campfire) {
        boolean bl = false;

        for (int i = 0; i < campfire.itemsBeingCooked.size(); ++i) {
            if (campfire.cookingTimes[i] > 0) {
                bl = true;
                campfire.cookingTimes[i] = Mth.clamp(campfire.cookingTimes[i] - 2, 0, campfire.cookingTotalTimes[i]);
            }
        }

        if (bl) {
            setChanged(world, pos, state);
        }

    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, CherineCampfireBlockEntity campfire) {
        RandomSource random = world.random;
        int i;
        if (random.nextFloat() < 0.11F) {
            for (i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.makeParticles(world, pos, state.getValue(CampfireBlock.SIGNAL_FIRE), false);
            }
        }

        i = (state.getValue(CampfireBlock.FACING)).get2DDataValue();

        for (int j = 0; j < campfire.itemsBeingCooked.size(); ++j) {
            if (!(campfire.itemsBeingCooked.get(j)).isEmpty() && random.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
                double d = (double) pos.getX() + 0.5 - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
                double e = (double) pos.getY() + 0.5;
                double g = (double) pos.getZ() + 0.5 - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);

                for (int k = 0; k < 4; ++k) {
                    world.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0, 5.0E-4, 0.0);
                }
            }
        }

    }

    public NonNullList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }

    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.itemsBeingCooked.clear();
        ContainerHelper.loadAllItems(nbt, this.itemsBeingCooked, registryLookup);
        int[] is;
        if (nbt.contains("CookingTimes", 11)) {
            is = nbt.getIntArray("CookingTimes");
            System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }

        if (nbt.contains("CookingTotalTimes", 11)) {
            is = nbt.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }

    }

    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, this.itemsBeingCooked, true, registryLookup);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        CompoundTag nbtCompound = new CompoundTag();
        ContainerHelper.saveAllItems(nbtCompound, this.itemsBeingCooked, true, registryLookup);
        return nbtCompound;
    }

    public boolean addItem(ServerLevel world, @Nullable LivingEntity user, ItemStack stack) {
        for (int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = this.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) {
                Optional<RecipeHolder<CampfireCookingRecipe>> optional = this.matchGetter.getRecipeFor(new SingleRecipeInput(stack), world);
                if (optional.isEmpty()) {
                    return false;
                }
                this.cookingTotalTimes[i] = optional.get().value().cookingTime();
                this.cookingTimes[i] = 0;
                this.itemsBeingCooked.set(i, stack.consumeAndReturn(1, user));
                world.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(user, this.getBlockState()));
                this.updateListeners();
                return true;
            }
        }

        return false;
    }

    private void updateListeners() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.itemsBeingCooked.clear();
    }

    protected void applyImplicitComponents(BlockEntity.DataComponentInput components) {
        super.applyImplicitComponents(components);
        (components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(this.getItemsBeingCooked());
    }

    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        componentMapBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItemsBeingCooked()));
    }

    public void removeComponentsFromTag(CompoundTag nbt) {
        nbt.remove("Items");
    }
}
