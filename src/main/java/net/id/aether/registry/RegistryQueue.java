package net.id.aether.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class RegistryQueue<T> {
    public static final RegistryQueue<Block> BLOCK = new RegistryQueue<>(Registry.BLOCK, 256);
    public static final RegistryQueue<EntityType<?>> ENTITY_TYPE = new RegistryQueue<>(Registry.ENTITY_TYPE, 32);
    public static final RegistryQueue<Item> ITEM = new RegistryQueue<>(Registry.ITEM, 384);

    private static final boolean CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    private final Registry<T> registry;
    private final List<Entry<? extends T>> entries;

    public static <S> Action<S> onClient(Action<S> action) {
        return CLIENT ? action : (id, value) -> {};
    }

    public RegistryQueue(Registry<T> registry, int initialCapacity) {
        this.registry = registry;
        entries = new ArrayList<>(initialCapacity);
    }

    @SafeVarargs
    public final <V extends T> V add(Identifier id, V value, BiConsumer<Identifier, ? super V>... additionalActions) {
        this.entries.add(new Entry<>(id, value, additionalActions));
        return value;
    }

    public void register() {
        for (Entry<? extends T> entry : entries) {
            register(entry);
        }
        entries.clear();
    }

    private <V extends T> void register(Entry<V> entry) {
        Registry.register(this.registry, entry.id, entry.value);
        for (BiConsumer<Identifier, ? super V> action : entry.additionalActions) {
            action.accept(entry.id, entry.value);
        }
    }

    public interface Action<T> extends BiConsumer<Identifier, T> {
    }

    private static record Entry<V>(Identifier id, V value, BiConsumer<Identifier, ? super V>[] additionalActions) {
    }
}
