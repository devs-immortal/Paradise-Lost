package com.aether.mixin.entity;

import com.aether.entities.hostile.swet.SwetEntity;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/SlimeEntity$RandomLookGoal")
public class RandomLookGoalMixin {
    @Shadow
    private int timer;

    @SuppressWarnings("UnnecessaryQualifiedMemberReference")
    @Inject(method = "Lnet/minecraft/entity/mob/SlimeEntity$RandomLookGoal;<init>(Lnet/minecraft/entity/mob/SlimeEntity;)V", at = @At("RETURN"))
    private void saveReference(SlimeEntity slime, CallbackInfo ci) {
        if (slime instanceof SwetEntity swet) {
            swet.setRandomLookTimer = value -> this.timer = value;
        }
    }
}
