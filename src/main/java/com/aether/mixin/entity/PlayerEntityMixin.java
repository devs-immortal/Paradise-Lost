package com.aether.mixin.entity;

import com.aether.entities.AetherEntityExtensions;
import com.aether.util.AetherDamageSources;
import com.aether.util.CustomStatusEffectInstance;
import com.aether.world.dimension.AetherDimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity implements AetherEntityExtensions {

    @Shadow public abstract void increaseStat(Identifier stat, int amount);

    @Shadow @Final private PlayerAbilities abilities;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private boolean flipped = false;
    private boolean aetherFallen = false;

    private int gravFlipTime;

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOutOfWorld() && getY() < -1 && world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            if (!world.isClient()) {
                setAetherFallen(true);
                ((ServerPlayerEntity) (Object) this).teleport(getServer().getWorld(World.OVERWORLD), this.getX() * 16, world.getHeight(), this.getZ() * 16, this.getYaw(), this.getPitch());
                CustomStatusEffectInstance ef = new CustomStatusEffectInstance(StatusEffect.byRawId(9), 160, 2);
                ef.ShowParticles = false;
                ((ServerPlayerEntity) (Object) this).addStatusEffect(ef);
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo ci) {
    }

    @Override
    public void setAetherFallen(boolean aetherFallen) {
        this.aetherFallen = aetherFallen;
    }

    @Override
    public boolean isAetherFallen() {
        return aetherFallen;
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if(isAetherFallen()) {

            aetherFallen = false;

            if (abilities.allowFlying) {
                cir.setReturnValue(false);
            } else {
                if (fallDistance >= 2.0F) {
                    increaseStat(Stats.FALL_ONE_CM, (int)Math.round((double)fallDistance * 100.0D));
                }
                cir.setReturnValue(super.handleFallDamage(fallDistance, damageMultiplier, AetherDamageSources.AETHER_FALL));
            }
            cir.cancel();
        }


    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        if(flipped){
            gravFlipTime++;
            if(gravFlipTime > 20){
                flipped = false;
                this.fallDistance = 0;
            }
            if(!this.hasNoGravity()) {
                Vec3d antiGravity = new Vec3d(0, 0.12D, 0);
                this.setVelocity(this.getVelocity().add(antiGravity));
            }
        }
    }
}