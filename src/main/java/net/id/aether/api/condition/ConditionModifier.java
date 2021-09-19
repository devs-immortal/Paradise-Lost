package net.id.aether.api.condition;


public interface ConditionModifier {

    float getDecayMultiplier(ConditionProcessor condition);

    float getScalingMultiplier(ConditionProcessor condition);

    float getScalingOffset(ConditionProcessor condition);

    float getSeverityMultiplier(ConditionProcessor condition);

    float getConstantCondition(ConditionProcessor conditionProcessor);
}