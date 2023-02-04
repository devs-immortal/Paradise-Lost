package net.id.paradiselost.entities;

public interface ParadiseLostEntityExtensions {

    boolean flipped = false;
    boolean paradiseLostFallen = false;
    boolean corsican_hareFallen = false;
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

    default boolean isParadiseHareFallen() {
        return corsican_hareFallen;
    }

    default void setParadiseHareFallen(boolean corsican_hareFallen) {
    }

    void setFlipped();

    default void tick() {
    }
}
