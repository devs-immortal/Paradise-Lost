package net.id.paradiselost.entities;

public interface ParadiseLostEntityExtensions {

    boolean flipped = false;
    boolean paradiseLostFallen = false;
    int gravFlipTime = 0;

    default int getFlipTime() {
        return gravFlipTime;
    }

    default boolean getFlipped() {
        return flipped;
    }

    default boolean isParadiseLostFallen() {
        return paradiseLostFallen;
    }

    default void setParadiseLostFallen(boolean value) {
    }

    void setFlipped();

    default void tick() {
    }
}
