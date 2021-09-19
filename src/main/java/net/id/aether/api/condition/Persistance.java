package net.id.aether.api.condition;

public enum Persistance {
    TEMPORARY("condition.persistance.temporary"),
    CHRONIC("condition.persistance.chronic"),
    CONSTANT("condition.persistance.constant");

    public final String translation;

    Persistance(String translation) {
        this.translation = translation;
    }
}
