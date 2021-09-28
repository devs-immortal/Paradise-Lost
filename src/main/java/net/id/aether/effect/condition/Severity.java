package net.id.aether.effect.condition;

/**
 * The severity intervals at which a {@code Condition} is processed.
 * <br> {@code Condition}s may use these to apply certain affects when
 * the {@code Severity} is greater than {@code Severity.EXTREME}, rather
 * than a lower {@code Severity}, for example.
 * @author AzazelTheDemonLord
 */
public enum Severity {
    /**
     * The lowest {@code Severity}. The intent of this is to describe when
     * a {@code Condition} is practically zero - hence the name {@code NEGLIGIBLE}.
     */
    NEGLIGIBLE(0.0F, "condition.severity.negligible"),
    /**
     * The intent of this is to be the lowest {@code Severity} that
     * makes a {@code Condition} do things. For example, {@link VenomCondition#tick}
     * applies a very slight poison effect for this severity.
     */
    MILD(0.2F, "condition.severity.mild"),
    /**
     * The intent of this is to be the {@code Severity} at which the
     * {@code Condition} has "gotten pretty bad".
     */
    ACUTE(0.525F, "condition.severity.acute"),
    /**
     * The {@code Severity} at which the {@code Condition} has "gotten
     * really, really, bad".
     */
    DIRE(0.8F, "condition.severity.dire"),
    /**
     * The last {@code Severity} level. This is as bad as the
     * {@code Condition} gets.
     */
    EXTREME(0.95F, "condition.severity.extreme");

    /**
     * The percent at which the {@code Severity} becomes this {@code Severity}.
     * @see Severity#getSeverity
     */
    public final float triggerPercent;
    /**
     * The translation key for this {@oode Severity}.
     */
    public final String translation;
    /**
     * @param triggerPercent {@link Severity#triggerPercent}
     * @param translation {@link Severity#translation}
     */
    Severity(float triggerPercent, String translation) {
        this.triggerPercent = triggerPercent;
        this.translation = translation;
    }
    /**
     * Converts a raw severity percent to a {@code Severity}.
     * @param rawSeverity The raw percent of how strong the {@code Condition} is
     * @return The greatest {@code Severity} less than {@code rawSeverity}
     */
    public static Severity getSeverity(float rawSeverity) {
        var severity = NEGLIGIBLE;
        for (Severity value : values()) {
            if(rawSeverity > value.triggerPercent)
                severity = value;
        }
        return severity;
    }
    /**
     * Returns whether a given {@code Severity} is equal or more severe that this {@code Severity}
     * @param severity The {@code Severity} to test
     * @return Whether {@code severity} is equal or more severe than this {@code Severity}
     */
    public boolean isAsOrMoreSevere(Severity severity) {
        return this.triggerPercent >= severity.triggerPercent;
    }
}
