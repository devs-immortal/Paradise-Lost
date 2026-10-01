package net.id.paradise_lost.block.blockentity;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CalciteDecoratedPotBlockEntity extends BlockEntity implements RandomizableContainer, ContainerSingleItem.BlockContainerSingleItem {
    public static final String SHERDS_NBT_KEY = "sherds";
    public static final String ITEM_NBT_KEY = "item";
    public static final int field_46660 = 1;
    public long lastWobbleTime;
    @Nullable
    public WobbleType lastWobbleType;
    private PotDecorations sherds;
    private ItemStack stack = ItemStack.EMPTY;
    @Nullable
    protected ResourceKey<LootTable> lootTableId;
    protected long lootTableSeed;

    public CalciteDecoratedPotBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.CALCITE_DECORATED_POT.get(), pos, state);
        this.sherds = PotDecorations.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        this.sherds.save(nbt);
        if (!this.trySaveLootTable(nbt) && !this.stack.isEmpty()) {
            nbt.put("item", this.stack.save(registryLookup));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.sherds = PotDecorations.load(nbt);
        if (!this.tryLoadLootTable(nbt)) {
            if (nbt.contains("item", Tag.TAG_COMPOUND)) {
                this.stack = ItemStack.parse(registryLookup, nbt.getCompound("item")).orElse(ItemStack.EMPTY);
            } else {
                this.stack = ItemStack.EMPTY;
            }
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveCustomOnly(registryLookup);
    }

    public Direction getHorizontalFacing() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public PotDecorations getSherds() {
        return this.sherds;
    }

    public void readFrom(ItemStack stack) {
        this.applyComponentsFromItemStack(stack);
    }

    public ItemStack asStack() {
        ItemStack itemStack = new ItemStack(BlockRegistry.CALCITE_DECORATED_POT.get());
        itemStack.applyComponents(this.collectComponents());
        return itemStack;
    }

    public static ItemStack getStackWith(PotDecorations sherds) {
        ItemStack itemStack = new ItemStack(BlockRegistry.CALCITE_DECORATED_POT.get());
        itemStack.set(DataComponents.POT_DECORATIONS, sherds);
        return itemStack;
    }

    @Nullable
    @Override
    public ResourceKey<LootTable> getLootTable() {
        return this.lootTableId;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTableId = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        componentMapBuilder.set(DataComponents.POT_DECORATIONS, this.sherds);
        componentMapBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.stack)));
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput components) {
        super.applyImplicitComponents(components);
        this.sherds = components.getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
        this.stack = components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne();
    }

    @Override
    public void removeComponentsFromTag(CompoundTag nbt) {
        super.removeComponentsFromTag(nbt);
        nbt.remove("sherds");
        nbt.remove("item");
    }

    @Override
    public ItemStack getTheItem() {
        this.unpackLootTable(null);
        return this.stack;
    }

    @Override
    public ItemStack splitTheItem(int count) {
        this.unpackLootTable(null);
        ItemStack itemStack = this.stack.split(count);
        if (this.stack.isEmpty()) {
            this.stack = ItemStack.EMPTY;
        }

        return itemStack;
    }

    @Override
    public void setTheItem(ItemStack stack) {
        this.unpackLootTable(null);
        this.stack = stack;
    }

    @Override
    public BlockEntity getContainerBlockEntity() {
        return this;
    }

    public void wobble(WobbleType wobbleType) {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, wobbleType.ordinal());
        }
    }

    @Override
    public boolean triggerEvent(int type, int data) {
        if (this.level != null && type == 1 && data >= 0 && data < WobbleType.values().length) {
            this.lastWobbleTime = this.level.getGameTime();
            this.lastWobbleType = WobbleType.values()[data];
            return true;
        } else {
            return super.triggerEvent(type, data);
        }
    }

    public enum WobbleType {
        POSITIVE(7),
        NEGATIVE(10);

        public final int lengthInTicks;

        WobbleType(final int lengthInTicks) {
            this.lengthInTicks = lengthInTicks;
        }
    }
}
