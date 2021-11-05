package net.id.aether.effect.condition;


public interface ConditionModifier {

    default float getDecayMultiplier(Condition condition) {
        return 1;
    }

    default float getScalingMultiplier(Condition condition) {
        return 1;
    }

    default float getScalingOffset(Condition condition) {
        return 0;
    }

    default float getSeverityMultiplier(Condition condition) {
        return 1;
    }

    default float getConstantCondition(Condition condition) {
        return 0;
    }
}