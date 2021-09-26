package net.id.aether.effect.condition;

/**
 * The temporary persistence is what you expect. It is given by things like
 * venom arrows, or whatever, and goes away with time.
 *
 * The chronic persistence is like the temporary persistence, but it doesn't
 * go down with time (in theory). It is reduced by consumables.
 *
 * The constant persistence is given by things like armors and trinkets that
 * apply conditions. It goes away when the armor or trinket is removed.
 *
 * ~ Jack
 */
public enum Persistence {
    TEMPORARY("condition.persistence.temporary"),
    CHRONIC("condition.persistence.chronic"),
    CONSTANT("condition.persistence.constant");

    public final String translation;

    Persistence(String translation) {
        this.translation = translation;
    }
}
