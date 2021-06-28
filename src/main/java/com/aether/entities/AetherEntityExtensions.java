package com.aether.entities;

public interface AetherEntityExtensions {

    boolean flipped = false;
    int gravFlipTime = 0;
    boolean getFlipped();
    void setFlipped();

    void tick();
}
