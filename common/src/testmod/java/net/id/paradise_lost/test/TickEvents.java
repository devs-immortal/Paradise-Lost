package net.id.paradise_lost.test;

import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class TickEvents {
    public static final List<Runnable> CLIENT_END = new CopyOnWriteArrayList<>();
    public static final List<Consumer<MinecraftServer>> SERVER_END = new CopyOnWriteArrayList<>();

    private TickEvents() {
    }
}
