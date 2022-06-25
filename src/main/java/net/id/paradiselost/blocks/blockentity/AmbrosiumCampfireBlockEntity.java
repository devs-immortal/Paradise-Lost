package net.id.paradiselost.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Optional;

import static net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes.AMBROSIUM_CAMPFIRE;

public class AmbrosiumCampfireBlockEntity extends BlockEntity implements Clearable {
    private final DefaultedList<ItemStack> itemsBeingCooked;
    private final int[] cookingTimes;
    private final int[] cookingTotalTimes;
    
    public AmbrosiumCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(AMBROSIUM_CAMPFIRE, pos, state);
        itemsBeingCooked = DefaultedList.ofSize(4, ItemStack.EMPTY);
        cookingTimes = new int[4];
        cookingTotalTimes = new int[4];
    }
    
    public static void litServerTick(World world, BlockPos pos, BlockState state, AmbrosiumCampfireBlockEntity campfire) {
        boolean bl = false;
        
        for(int i = 0; i < campfire.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = campfire.itemsBeingCooked.get(i);
            if (!itemStack.isEmpty()) {
                bl = true;
                campfire.cookingTimes[i]++;
                if (campfire.cookingTimes[i] >= campfire.cookingTotalTimes[i]) {
                    Inventory inventory = new SimpleInventory(itemStack);
                    ItemStack itemStack2 = world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, inventory, world)
                        .map((recipe) -> recipe.craft(inventory))
                        .orElse(itemStack);
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                    campfire.itemsBeingCooked.set(i, ItemStack.EMPTY);
                    world.updateListeners(pos, state, state, 3);
                }
            }
        }
        
        if (bl) {
            markDirty(world, pos, state);
        }
    }
    
    public static void unlitServerTick(World world, BlockPos pos, BlockState state, AmbrosiumCampfireBlockEntity campfire) {
        boolean bl = false;
        
        for(int i = 0; i < campfire.itemsBeingCooked.size(); ++i) {
            if (campfire.cookingTimes[i] > 0) {
                bl = true;
                campfire.cookingTimes[i] = MathHelper.clamp(campfire.cookingTimes[i] - 2, 0, campfire.cookingTotalTimes[i]);
            }
        }
        
        if (bl) {
            markDirty(world, pos, state);
        }
        
    }
    
    public static void clientTick(World world, BlockPos pos, BlockState state, AmbrosiumCampfireBlockEntity campfire) {
        var random = world.random;
        int i;
        if (random.nextFloat() < 0.11F) {
            for(i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.spawnSmokeParticle(world, pos, state.get(CampfireBlock.SIGNAL_FIRE), false);
            }
        }
        
        i = state.get(CampfireBlock.FACING).getHorizontal();
        
        for(int j = 0; j < campfire.itemsBeingCooked.size(); ++j) {
            if (!(campfire.itemsBeingCooked.get(j)).isEmpty() && random.nextFloat() < 0.2F) {
                Direction direction = Direction.fromHorizontal(Math.floorMod(j + i, 4));
                float f = 0.3125F;
                double d = (double)pos.getX() + 0.5D - (double)((float)direction.getOffsetX() * 0.3125F) + (double)((float)direction.rotateYClockwise().getOffsetX() * 0.3125F);
                double e = (double)pos.getY() + 0.5D;
                double g = (double)pos.getZ() + 0.5D - (double)((float)direction.getOffsetZ() * 0.3125F) + (double)((float)direction.rotateYClockwise().getOffsetZ() * 0.3125F);
                
                for(int k = 0; k < 4; ++k) {
                    world.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
        
    }
    
    public DefaultedList<ItemStack> getItemsBeingCooked() {
        return itemsBeingCooked;
    }
    
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        itemsBeingCooked.clear();
        Inventories.readNbt(nbt, itemsBeingCooked);
        int[] is;
        if (nbt.contains("CookingTimes", 11)) {
            is = nbt.getIntArray("CookingTimes");
            System.arraycopy(is, 0, cookingTimes, 0, Math.min(cookingTotalTimes.length, is.length));
        }
        
        if (nbt.contains("CookingTotalTimes", 11)) {
            is = nbt.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, cookingTotalTimes, 0, Math.min(cookingTotalTimes.length, is.length));
        }
        
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, itemsBeingCooked, true);
        nbt.putIntArray("CookingTimes", cookingTimes);
        nbt.putIntArray("CookingTotalTimes", cookingTotalTimes);
    }
    
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, itemsBeingCooked, true);
        return nbtCompound;
    }
    
    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack item) {
        return itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(item), world);
    }
    
    public boolean addItem(ItemStack item, int cookTime) {
        for(int i = 0; i < itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) {
                cookingTotalTimes[i] = cookTime;
                cookingTimes[i] = 0;
                itemsBeingCooked.set(i, item.split(1));
                updateListeners();
                return true;
            }
        }
        
        return false;
    }
    
    private void updateListeners() {
        markDirty();
        getWorld().updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }
    
    @Override
    public void clear() {
        itemsBeingCooked.clear();
    }
    
    public void spawnItemsBeingCooked() {
        if (world != null) {
            updateListeners();
        }
    }
}
