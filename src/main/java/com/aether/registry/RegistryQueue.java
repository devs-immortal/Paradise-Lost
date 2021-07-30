package com.aether.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.aether.Aether.locate;

public final class RegistryQueue<cJOokaAi948> {
    private static final boolean CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    private final Registry<cJOokaAi948> registry;
    private final List<Entry<cJOokaAi948>> entries;

    public static <cJOokaAi948> Consumer<cJOokaAi948> onClient(Consumer<cJOokaAi948> action) {
        return CLIENT ? action : value -> {};
    }

    public RegistryQueue(Registry<cJOokaAi948> registry, int initialCapacity) {
        this.registry = registry;
        entries = new ArrayList<>(initialCapacity);
    }

    public cJOokaAi948 add(String id, cJOokaAi948 value, Consumer<cJOokaAi948>[] additionalActions) {
        this.entries.add(new Entry<>(id, value, additionalActions));
        return value;
    }

    public void register() {
        for (Entry<cJOokaAi948> entry : entries) {
            Registry.register(this.registry, locate(entry.id), entry.value);
            for (Consumer<cJOokaAi948> action : entry.additionalActions) {
                action.accept(entry.value);
            }
        }
        entries.clear();
    }

    private static record Entry<cJOokaAi948>(String id, cJOokaAi948 value, Consumer<cJOokaAi948>[] additionalActions) {
    }
}
