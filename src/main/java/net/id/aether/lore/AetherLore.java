package net.id.aether.lore;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.*;

import net.id.aether.component.AetherComponents;
import net.id.aether.items.AetherItems;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import static net.id.aether.Aether.locate;

public final class AetherLore{
    private static final Map<LoreTriggerType, Set<Pair<Identifier, LoreEntry<?>>>> TRIGGER_MAP = new Object2ObjectOpenHashMap<>();

    public static final Identifier ROOT_ID = locate("root");
    public static final LoreEntry<Void> ROOT = register(ROOT_ID, new LoreEntry<>(0, 0, AetherItems.LORE_BOOK));
    public static final LoreEntry<ItemStack> ITEM_TEST = register("item_test", new LoreEntry<>(0, 0, Items.DIAMOND, LoreTriggerType.ITEM, (stack)->stack.getItem().equals(Items.DIAMOND)));

    public static void init(){}
    
    private static <T> LoreEntry<T> register(@NotNull String name, @NotNull LoreEntry<T> lore){
        return register(locate(name), lore);
    }
    
    public static <T> LoreEntry<T> register(@NotNull Identifier id, @NotNull LoreEntry<T> lore){
        Objects.requireNonNull(id, "id was null");
        Objects.requireNonNull(lore, "lore was null");
        TRIGGER_MAP.computeIfAbsent(lore.triggerType(), (key)->new HashSet<>()).add(new Pair<>(id, lore));
        return Registry.register(AetherRegistries.LORE_REGISTRY, id, lore);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void trigger(@NotNull LoreTriggerType triggerType, @NotNull ServerPlayerEntity player, @NotNull T object){
        var state = AetherComponents.LORE_STATE.get(player);
        for(var lorePair : TRIGGER_MAP.getOrDefault(triggerType, Set.of())){
            var loreId = lorePair.getLeft();
            LoreEntry<T> lore = (LoreEntry<T>)lorePair.getRight();
            
            var status = state.getLoreStatus(loreId);
            if(status == LoreStatus.LOCKED && lore.trigger().test(object)){
                state.setLoreStatus(loreId, LoreStatus.COMPLETED);
            }
        }
    }
}
