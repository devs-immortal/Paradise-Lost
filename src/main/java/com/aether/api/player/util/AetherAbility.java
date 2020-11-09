package com.aether.api.player.util;

public interface AetherAbility {

    boolean shouldExecute();

    void update();

    default boolean disableFallDamage() {
        return false;
    }

}