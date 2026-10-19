package net.id.paradise_lost.datafixer;

import net.id.paradise_lost.PLConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class LegacyCardinalComponents {
    public static final String ROOT_KEY = "cardinal_components";
    public static final ResourceLocation MOA_GENETICS = PLConstants.id("moa_genetics");
    public static final ResourceLocation MINECART_FLOAT = PLConstants.id("minecart_float");

    private LegacyCardinalComponents() {
    }

    @Nullable
    public static CompoundTag get(CompoundTag entityTag, ResourceLocation componentId) {
        if (entityTag == null || !entityTag.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            return null;
        }
        CompoundTag root = entityTag.getCompound(ROOT_KEY);
        String key = componentId.toString();
        if (!root.contains(key, Tag.TAG_COMPOUND)) {
            return null;
        }
        return root.getCompound(key);
    }

    public static void remove(CompoundTag entityTag, ResourceLocation componentId) {
        if (entityTag == null || !entityTag.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag root = entityTag.getCompound(ROOT_KEY);
        root.remove(componentId.toString());
        if (root.isEmpty()) {
            entityTag.remove(ROOT_KEY);
        }
    }
}
