package net.id.aether.mixin.entity;

import net.id.aether.entities.AetherEntityExtensions;
import net.id.aether.util.AetherDamageSources;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements AetherEntityExtensions {

    private boolean aetherFallen = false;
    public boolean aerbunnyFallen = false;

    public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void increaseStat(Identifier stat, int amount);

    @Shadow public abstract PlayerAbilities getAbilities();

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOutOfWorld() && getY() < world.getBottomY() - 1 && world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            if (!world.isClient()) {
                setAetherFallen(true);
                ((ServerPlayerEntity) (Object) this).teleport(getServer().getWorld(World.OVERWORLD), this.getX() * 10, world.getTopY() + 128, this.getZ() * 10, this.getYaw(), this.getPitch());
                StatusEffectInstance ef = new StatusEffectInstance(StatusEffect.byRawId(9), 160, 2, false, false, true);
                this.addStatusEffect(ef);
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo ci) {
    }

    @Override
    public boolean isAetherFallen() {
        return aetherFallen;
    }

    @Override
    public void setAetherFallen(boolean aetherFallen) {
        this.aetherFallen = aetherFallen;
    }

    @Override
    public boolean isAerbunnyFallen() {
        return aerbunnyFallen;
    }

    @Override
    public void setAerbunnyFallen(boolean aerbunnyFallen) {
        this.aerbunnyFallen = aerbunnyFallen;
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (isAetherFallen()) {
            aetherFallen = false;
            if (getAbilities().allowFlying) {
                cir.setReturnValue(false);
            } else {
                if (fallDistance >= 2.0F) {
                    increaseStat(Stats.FALL_ONE_CM, (int) Math.round((double) fallDistance * 100.0D));
                }
                cir.setReturnValue(super.handleFallDamage(fallDistance, damageMultiplier, AetherDamageSources.AETHER_FALL));
            }
            cir.cancel();
        }
        if (aerbunnyFallen) {
            aerbunnyFallen = false;
            if (getAbilities().allowFlying) {
                cir.setReturnValue(false);
            } else {
                if (fallDistance >= 2.0F) {
                    increaseStat(Stats.FALL_ONE_CM, (int) Math.round((double) fallDistance * 100.0D));
                }
                cir.setReturnValue(super.handleFallDamage(fallDistance, damageMultiplier, AetherDamageSources.AERBUNNY_FALL));
            }
            cir.cancel();
        }
    }
}