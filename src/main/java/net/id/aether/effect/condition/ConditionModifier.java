package net.id.aether.effect.condition;


public interface ConditionModifier {

    float getDecayMultiplier(Condition condition);

    float getScalingMultiplier(Condition condition);

    float getScalingOffset(Condition condition);

    float getSeverityMultiplier(Condition condition);

    float getConstantCondition(Condition condition);
}