package net.id.paradiselost.screen.handler;

import com.mojang.datafixers.util.Pair;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.mixin.util.SlotAccessor;
import net.id.paradiselost.screen.ParadiseLostScreens;
import net.id.paradiselost.screen.slot.FakeSlot;
import net.id.paradiselost.screen.slot.PreviewSlot;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SaddleItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

/**
 * The handler for the Moa screen, kinda stupid and hacky...
 */
public class MoaScreenHandler extends ScreenHandler {
    private final SimpleInventory dummy = new SimpleInventory(20) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return false;
        }
    };
    private final MoaEntity moa;
    private final Set<Slot> moaChestSlots;
    private boolean enableMoaInventory;
    
    public MoaScreenHandler(int syncId, PlayerInventory playerInventory, Inventory moaInventory, MoaEntity moa) {
        super(ParadiseLostScreens.MOA, syncId);
        this.moa = moa;
        
        addSlot(new FakeSlot(
                8, 18,
                () -> moa.isSaddled() ? new ItemStack(Items.SADDLE) : ItemStack.EMPTY,
                (stack) -> moa.setSaddled(!stack.isEmpty() && stack.getItem() instanceof SaddleItem),
                (stack) -> stack.getItem() instanceof SaddleItem
            ) {
                @Override
                public int getMaxItemCount() {
                    return 1;
                }
    
                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(BLOCK_ATLAS_TEXTURE, PreviewSlot.Image.SADDLE.location());
                }
            }
        );
        addSlot(new FakeSlot(
                8, 36,
                moa::getChest,
                moa::setChest,
                (stack) -> stack.getItem() instanceof BlockItem item && item.getBlock() instanceof AbstractChestBlock
            ) {
                @Override
                public int getMaxItemCount() {
                    return 1;
                }
    
                @Override
                public void markDirty() {
                    updateChestState();
                }
    
                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(BLOCK_ATLAS_TEXTURE, PreviewSlot.Image.CHEST.location());
                }
            }
        );
    
        //FIXME This needs a real fix, without this the client never sees that the Moa has an inventory
        if (moa.world.isClient) {
            moa.refreshChest(false);
            moaInventory = moa.getInventory();
        }
        
        enableMoaInventory = moa.hasChest();
        var chestInventory = enableMoaInventory ? moaInventory : dummy;
        
        Set<Slot> moaChestSlots = new HashSet<>();
        for (int y = 0; y < 4; y++) {
            int slotY = 18 + 18 * y;
            for (int x = 0; x < 5; x++) {
                moaChestSlots.add(addSlot(new Slot(chestInventory, y * 5 + x, 80 + x * 18, slotY) {
                    @Override
                    public boolean isEnabled() {
                        return enableMoaInventory;
                    }
                }));
            }
        }
        this.moaChestSlots = Collections.unmodifiableSet(moaChestSlots);
        
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 102 + y * 18));
            }
        }
        for (int x = 0; x < 9; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * 18, 160));
        }
    }
    
    private void updateChestState() {
        var hasChest = moa.hasChest();
        if (hasChest == enableMoaInventory) {
            return;
        }
        enableMoaInventory = hasChest;
        
        var inventory = hasChest ? moa.getInventory() : dummy;
        for (var slot : moaChestSlots) {
            ((SlotAccessor) slot).setInventory(inventory);
        }
    }
    
    @Override
    public boolean canUse(PlayerEntity player) {
        return player.squaredDistanceTo(moa) <= 64;
    }
    
    public boolean hasMoaInventory() {
        return enableMoaInventory;
    }
    
    public MoaEntity moa() {
        return moa;
    }
    
    @Override
    public ItemStack transferSlot(PlayerEntity player, int sourceSlot) {
        ItemStack result = ItemStack.EMPTY;
        if (!moa.hasChest()) return result;
        Slot slot = slots.get(sourceSlot);
        if (!slot.hasStack()) {
            return result;
        }
        
        var stack = slot.getStack();
        result = stack.copy();
        if (sourceSlot < 21 ? !insertItem(stack, 22, 57, true) : !insertItem(stack, 2, 22, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
        if (stack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTakeItem(player, stack);
        return result;
    }
}
