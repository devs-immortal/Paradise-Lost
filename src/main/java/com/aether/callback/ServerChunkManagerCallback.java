package com.aether.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerChunkManager;

public interface ServerChunkManagerCallback {
    Event<ServerChunkManagerCallback> EVENT = EventFactory.createArrayBacked(ServerChunkManagerCallback.class, (listeners) -> (manager) -> {
        for (ServerChunkManagerCallback listener : listeners) listener.handle(manager);
    });

    void handle(ServerChunkManager manager);
}