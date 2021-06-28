package com.aether.entities;

public interface AetherEntityExtensions {

    boolean flipped = false;
    int gravFlipTime = 0;
    default int getFlipTime(){ return gravFlipTime; }
    default boolean getFlipped() { return flipped; }

    void setFlipped();

    void tick();
}
