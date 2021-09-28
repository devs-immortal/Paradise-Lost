package net.id.aether.effect.condition;

/**
 * {@code Persistence} is how much a {@code Condition} persists.
 * There are three main types: {@link Persistence#TEMPORARY},
 * {@link Persistence#CHRONIC}, and {@link Persistence#CONSTANT}
 * <br><br>
 * ~ Jack
 * @author AzazelTheDemonLord
 * @see Persistence#TEMPORARY
 * @see Persistence#CHRONIC
 * @see Persistence#CONSTANT
 */
public enum Persistence {
    /**
     * This {@code Persistence} is what you'd expect.
     * It's given by things like projectiles, for example, and goes away with time.
     */
    TEMPORARY("condition.persistence.temporary"),
    /**
     * This is like the {@link Persistence#TEMPORARY} {@code Persistence},
     * but it doesn't go down with time (in theory). It is reduced by
     * consumables.
     */
    CHRONIC("condition.persistence.chronic"),
    /**
     * The {@code CONSTANT} {@code Persistence} is given by things like
     * armors and trinkets that apply conditions. It goes away when the
     * armor or trinket is removed.
     */
    CONSTANT("condition.persistence.constant");

    public final String translation;

    /**
     * @param translation The translation key for the {@code Persistence}
     */
    Persistence(String translation) {
        this.translation = translation;
    }
}
