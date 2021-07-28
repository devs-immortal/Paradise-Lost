package com.aether.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/entity/mob/SlimeEntity$RandomLookGoal")
public interface RandomLookGoalAccessor {
    @Accessor
    void setTimer(int value);
}
