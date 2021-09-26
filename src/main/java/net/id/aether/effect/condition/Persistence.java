package net.id.aether.effect.condition;

public enum Persistence {
    TEMPORARY("condition.persistence.temporary"),
    CHRONIC("condition.persistence.chronic"),
    CONSTANT("condition.persistence.constant");

    public final String translation;

    Persistence(String translation) {
        this.translation = translation;
    }
}
