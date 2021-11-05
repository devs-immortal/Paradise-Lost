package net.id.aether.mixin.entity;

import net.id.aether.api.ConditionAPI;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.component.ConditionManager;
import net.id.aether.effect.AetherStatusEffects;
import net.id.aether.effect.condition.Conditions;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.tag.AetherFluidTags;
import net.id.aether.world.AetherGameRules;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(method = "getVelocityMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getVelocityMultiplier()F"), require = 2)
    private float getVelocityMultiplier(Block target) {
        if (target == AetherBlocks.QUICKSOIL || target == AetherBlocks.QUICKSOIL_GLASS || target == AetherBlocks.QUICKSOIL_GLASS_PANE) {
            double maxSpeed = ((Entity) (Object) this).world.getGameRules().get(AetherGameRules.MAX_QUICKSOIL_SPEED).get();
            return (float) (1 + Math.max(
                    (maxSpeed - ((Entity) (Object) this).getVelocity().horizontalLength()) / maxSpeed * 0.102,
                    0));
        }
        return target.getVelocityMultiplier();
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateSwimming()V"))
    private void springWaterEffects(CallbackInfo ci) {
        Entity entity = ((Entity)(Object)this);
        if (entity.world.getStatesInBoxIfLoaded(entity.getBoundingBox().contract(1.0E-6D)).anyMatch(
                (state) -> state.getBlock().equals(AetherBlocks.SPRING_WATER))
        ) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(AetherStatusEffects.SIMMERING, 30));
            }
        }
    }
}
