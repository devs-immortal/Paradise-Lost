package com.aether.items.utils;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Arrays;

public record StackableVariantColorizer(String[] tintFields, int[] defaultColors) {
    private static final String[] DEFAULT_TINT_FIELDS = new String[]{"primaryColor", "secondaryColor", "tertiaryColor", "quaternaryColor", "quinaryColor", "senaryColor"};

    public StackableVariantColorizer {
        assert tintFields.length == defaultColors.length;
    }

    public StackableVariantColorizer(int... defaultColors) {
        this(Arrays.copyOf(DEFAULT_TINT_FIELDS, defaultColors.length), defaultColors);
        assert defaultColors.length <= DEFAULT_TINT_FIELDS.length;
    }

    public void register(Item item) {
        ColorProviderRegistry.ITEM.register(this::getFromTag, item);
    }

    private int getFromTag(ItemStack stack, int tintIndex) {
        NbtCompound sv = stack.getSubNbt("stackableVariant");
        if (sv != null && sv.contains(this.tintFields[tintIndex])) {
            return sv.getInt(this.tintFields[tintIndex]);
        }
        return this.defaultColors[tintIndex];
    }
}
