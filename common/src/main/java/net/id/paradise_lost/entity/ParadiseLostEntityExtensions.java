package net.id.paradise_lost.entity;

public interface ParadiseLostEntityExtensions {

    default int getFlipTime() {
        return 0;
    }

    default boolean getFlipped() {
        return false;
    }

    default boolean isParadiseLostFallen() {
        return false;
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
