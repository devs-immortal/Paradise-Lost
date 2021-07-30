package com.aether.registry;

import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RegistryQueue<T, R extends Registry<T>> {
    private final R registry;

    RegistryQueue() {

    }


    public record RegistryEntry<T>(String id, T entry, @Nullable Consumer<T> additionalAction) {
    }
}
