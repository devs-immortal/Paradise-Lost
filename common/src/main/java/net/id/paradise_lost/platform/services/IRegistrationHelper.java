package net.id.paradise_lost.platform.services;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;

public interface IRegistrationHelper {
    void setFlammable(Object block, int encouragement, int flammability);

    void registerStrippable(Object log, Object stripped);

    void registerTillable(Object block, Object farmlandResult);

    void registerFlattenable(Object block, Object pathResult);

    void registerFuel(ItemLike item, int burnTime);

    void registerCompostable(ItemLike item, float chance);

    GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue);

    void registerCreativeTabEntries();
}
