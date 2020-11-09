package com.aether.api.player;

import com.aether.api.player.util.AccessoryInventory;
import net.minecraft.entity.player.PlayerEntity;

public interface IPlayerAether {

    boolean disableFallDamage();

    float setReachDistance(float distance);

    void setInPortal();

    void inflictCure(int ticks);

    void inflictPoison(int ticks);

    int getShardsUsed();

    void increaseHealth(int amount);

    boolean isJumping();

    AccessoryInventory getAccessoryInventory();

    PlayerEntity getPlayer();

}