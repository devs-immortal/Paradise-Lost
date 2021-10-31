package net.id.aether.entities;

public interface AetherEntityExtensions {

    boolean flipped = false;
    boolean aetherFallen = false;
    boolean aerbunnyFallen = false;
    int gravFlipTime = 0;

    default int getFlipTime() {
        return gravFlipTime;
    }

    default boolean getFlipped() {
        return flipped;
    }

    default boolean isAetherFallen() {
        return aetherFallen;
    }

    default void setAetherFallen(boolean aetherFallen) {
    }

    default boolean isAerbunnyFallen() {
        return aerbunnyFallen;
    }

    default void setAerbunnyFallen(boolean aerbunnyFallen) {
    }

    void setFlipped();

    default void tick(){
    }
}
