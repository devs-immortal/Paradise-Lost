package net.id.paradiselost.entities;

public interface ParadiseLostEntityExtensions {

    boolean flipped = false;
    boolean paradiseLostFallen = false;
    boolean aerbunnyFallen = false;
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

    default boolean isAerbunnyFallen() {
        return aerbunnyFallen;
    }

    default void setAerbunnyFallen(boolean aerbunnyFallen) {
    }

    void setFlipped();

    default void tick() {
    }
}
