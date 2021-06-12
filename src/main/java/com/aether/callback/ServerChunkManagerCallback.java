package com.aether.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerChunkCache;

public interface ServerChunkManagerCallback {
    Event<ServerChunkManagerCallback> EVENT = EventFactory.createArrayBacked(ServerChunkManagerCallback.class, (listeners) -> (manager) -> {
        for (ServerChunkManagerCallback listener : listeners) listener.handle(manager);
    });

    void handle(ServerChunkCache manager);
}