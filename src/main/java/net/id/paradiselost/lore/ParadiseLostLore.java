package net.id.paradiselost.lore;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.id.paradiselost.component.ParadiseLostComponents;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.registry.ParadiseLostRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static net.id.paradiselost.ParadiseLost.locate;

public final class ParadiseLostLore {
    private static final Map<LoreTriggerType, Set<LoreEntry<?>>> TRIGGER_MAP = new Object2ObjectOpenHashMap<>();

    public static final LoreEntry<Void> ROOT = register("root", new LoreEntry<>(0, 0, ParadiseLostItems.LORE_BOOK));
    public static final LoreEntry<ItemStack> ITEM_TEST = register("item_test", new LoreEntry<>(0, 0, Items.DIAMOND, LoreTriggerType.ITEM, (stack) -> stack.getItem().equals(Items.DIAMOND)));

    public static void init() {
    }
    
    private static <T> LoreEntry<T> register(@NotNull String name, @NotNull LoreEntry<T> lore) {
        return register(locate(name), lore);
    }
    
    public static <T> LoreEntry<T> register(@NotNull Identifier id, @NotNull LoreEntry<T> lore) {
        Objects.requireNonNull(id, "id was null");
        Objects.requireNonNull(lore, "lore was null");
        TRIGGER_MAP.computeIfAbsent(lore.triggerType(), (key) -> new HashSet<>()).add(lore);
        return Registry.register(ParadiseLostRegistries.LORE_REGISTRY, id, lore);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void trigger(@NotNull LoreTriggerType triggerType, @NotNull ServerPlayerEntity player, @NotNull T object) {
        var state = ParadiseLostComponents.LORE_STATE.get(player);
        for (LoreEntry<?> lore : TRIGGER_MAP.getOrDefault(triggerType, Set.of())) {
            Identifier id = lore.getId();
            var status = state.getLoreStatus(id);

            if (status == LoreStatus.LOCKED && ((LoreEntry<T>) lore).trigger().test(object)) {
                state.setLoreStatus(id, LoreStatus.COMPLETED);
            }
        }
    }
}
