package net.id.paradise_lost.screen.handler;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.mixin.util.SlotAccessor;
import net.id.paradise_lost.screen.ParadiseLostScreens;
import net.id.paradise_lost.screen.slot.FakeSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.level.block.AbstractChestBlock;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.id.paradise_lost.ModConstants.id;
import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

public class MoaScreenHandler extends AbstractContainerMenu {
    private final SimpleContainer dummy = new SimpleContainer(20) {
        @Override
        public void setItem(int slot, ItemStack stack) {
            if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
                stack = stack.copy();
                stack.setCount(this.getMaxStackSize());
            }
            super.setItem(slot, stack);
            this.setChanged();
        }
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return false;
        }
    };
    private final MoaEntity moa;
    private final Set<Slot> moaChestSlots;
    private boolean enableMoaInventory;

    public MoaScreenHandler(int syncId, Inventory playerInventory, Container moaInventory, MoaEntity moa) {
        super(ParadiseLostScreens.MOA.get(), syncId);
        this.moa = moa;

        addSlot(new FakeSlot(
                8, 18,
                () -> moa.isSaddled() ? new ItemStack(Items.SADDLE) : ItemStack.EMPTY,
                (stack) -> moa.setSaddled(!stack.isEmpty() && stack.getItem() instanceof SaddleItem),
                (stack) -> stack.getItem() instanceof SaddleItem
            ) {
                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(BLOCK_ATLAS, id("item/slot/empty_slot_saddle"));
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
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public void setChanged() {
                    updateChestState();
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(BLOCK_ATLAS, id("item/slot/empty_slot_chest"));
                }
            }
        );

        if (moa.level().isClientSide) {
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
                    public boolean isActive() {
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
            ((SlotAccessor) slot).setContainer(inventory);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(moa) <= 64;
    }

    public boolean hasMoaInventory() {
        return enableMoaInventory;
    }

    public MoaEntity moa() {
        return moa;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int sourceSlot) {
        ItemStack result = ItemStack.EMPTY;
        if (!moa.hasChest()) return result;
        Slot slot = slots.get(sourceSlot);
        if (!slot.hasItem()) {
            return result;
        }

        var stack = slot.getItem();
        result = stack.copy();
        if (sourceSlot < 22 ? !moveItemStackTo(stack, 22, 58, true) : !moveItemStackTo(stack, 2, 22, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return result;
    }

    public record MoaScreenData(int entityId) implements CustomPacketPayload {

        public static Type<MoaScreenData> ID = new Type<>(id("moa_data"));

        public static final Codec<MoaScreenData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.INT.fieldOf("entity_id").forGetter(MoaScreenData::entityId)
        ).apply(instance, MoaScreenData::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, MoaScreenData> PACKET_CODEC;

        static {
            PACKET_CODEC = StreamCodec.composite(
                    ByteBufCodecs.INT, MoaScreenData::entityId,
                    MoaScreenData::new
            );
        }

        public int entityId() {
            return this.entityId;
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}
