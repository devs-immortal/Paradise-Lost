package net.id.aether.effect.condition;

public enum Severity {
    NEGLIGIBLE(0.0F, "condition.severity.negligible"),
    MILD(0.2F, "condition.severity.mild"),
    ACUTE(0.525F, "condition.severity.acute"),
    DIRE(0.8F, "condition.severity.dire"),
    EXTREME(0.95F, "condition.severity.extreme");

    public final float triggerPercent;
    public final String translation;

    Severity(float triggerPercent, String translation) {
        this.triggerPercent = triggerPercent;
        this.translation = translation;
    }

    public static Severity getSeverity(float rawSeverity) {
        var severity = NEGLIGIBLE;
        for (Severity value : values()) {
            if(rawSeverity > value.triggerPercent)
                severity = value;
        }
        return severity;
    }

    public boolean isAsOrMoreSevere(Severity severity) {
        return this.triggerPercent >= severity.triggerPercent;
    }
}
