package net.id.paradise_lost.platform.services;

import net.minecraft.world.level.GameRules;

public interface IRegistrationHelper {
    void setFlammable(Object block, int encouragement, int flammability);

    void registerTillable(Object block, Object farmlandResult);

    void registerFlattenable(Object block, Object pathResult);

    GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue);

    void registerCreativeTabEntries();
}
