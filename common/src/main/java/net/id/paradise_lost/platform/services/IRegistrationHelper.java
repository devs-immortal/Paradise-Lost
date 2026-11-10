package net.id.paradise_lost.platform.services;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

public interface IRegistrationHelper {
    void setFlammable(Object block, int encouragement, int flammability);

    void registerTillable(Object block, Object farmlandResult);

    void registerFlattenable(Object block, Object pathResult);

    GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(
            String name,
            GameRules.Category category,
            boolean defaultValue,
            BiConsumer<MinecraftServer, GameRules.BooleanValue> onChanged
    );

    default GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return registerBooleanGameRule(name, category, defaultValue, (server, value) -> {
        });
    }

    void registerCreativeTabEntries();
}
