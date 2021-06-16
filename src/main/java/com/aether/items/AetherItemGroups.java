package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.items.utils.ItemGroupExpansions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AetherItemGroups {
    public static final ItemGroup Blocks = build(
            Aether.locate("aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS_BLOCK));

    public static final ItemGroup Tools = build(
            Aether.locate("aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

    public static final ItemGroup Food = build(
            Aether.locate("aether_food"),
            () -> new ItemStack(AetherItems.BLUEBERRY));

    public static final ItemGroup Resources = build(
            Aether.locate("aether_resources"),
            () -> new ItemStack(AetherItems.AMBROSIUM_SHARD));

    public static final ItemGroup Misc = build(
            Aether.locate("aether_misc"),
            () -> new ItemStack(AetherItems.BRONZE_KEY));

    public static final ItemGroup Wearable = build(
            Aether.locate("aether_wearables"),
            () -> new ItemStack(AetherItems.ZANITE_CHESTPLATE));

    private static ItemGroup build(Identifier id, @Nullable Supplier<ItemStack> stackSupplier, @Nullable Consumer<List<ItemStack>> stacksForDisplay) {
        ((ItemGroupExpansions) ItemGroup.BUILDING_BLOCKS).expandArray();
        return new ItemGroup(ItemGroup.GROUPS.length - 1, String.format("%s.%s", id.getNamespace(), id.getPath())) {
            @Override
            public ItemStack createIcon() {
                if (stackSupplier != null) {
                    return stackSupplier.get();
                } else {
                    return ItemStack.EMPTY;
                }
            }

            @Override
            public void appendStacks(DefaultedList<ItemStack> stacks) {
                if (stacksForDisplay != null) {
                    stacksForDisplay.accept(stacks);
                    return;
                }

                super.appendStacks(stacks);
            }
        };
    }

    private static ItemGroup build(Identifier id, @Nullable Supplier<ItemStack> stackSupplier) {
        return build(id, stackSupplier, null);
    }
}
