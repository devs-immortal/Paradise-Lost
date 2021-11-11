package net.id.aether.entities.util;

/**
 * An interface for entities that need to be ticked after the main tick.
 */
public interface PostTickEntity {
    void postTick();
}