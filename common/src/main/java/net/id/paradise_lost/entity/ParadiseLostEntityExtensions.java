package net.id.paradise_lost.entity;

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

    default boolean isFloatyAnchored() {
        return false;
    }

    default void setFloatyAnchored(boolean anchored) {
    }

    void setFlipped();

    default void tick() {
    }
}
