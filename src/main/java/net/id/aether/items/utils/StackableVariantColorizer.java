package net.id.aether.items.utils;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public record StackableVariantColorizer(String[] tintFields, int[] defaultColors) implements Action<ItemConvertible> {
    private static final String[] DEFAULT_TINT_FIELDS = new String[]{"primaryColor", "secondaryColor", "tertiaryColor", "quaternaryColor", "quinaryColor", "senaryColor"};

    public StackableVariantColorizer {
        assert tintFields.length == defaultColors.length;
    }

    public StackableVariantColorizer(int... defaultColors) {
        this(Arrays.copyOf(DEFAULT_TINT_FIELDS, defaultColors.length), defaultColors);
        assert defaultColors.length <= DEFAULT_TINT_FIELDS.length;
    }

    @Override
    public void accept(Identifier id, ItemConvertible item) {
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
