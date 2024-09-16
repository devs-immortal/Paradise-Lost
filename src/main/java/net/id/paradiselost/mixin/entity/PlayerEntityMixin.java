package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.id.paradiselost.util.ParadiseLostDamageTypes;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ParadiseLostEntityExtensions {

    private boolean paradise_lost$fallen = false;

    public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void increaseStat(Identifier stat, int amount);

    @Shadow public abstract PlayerAbilities getAbilities();

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source == method_48926().getDamageSources().outOfWorld() && getY() < method_48926().getBottomY() - 1 && method_48926().getRegistryKey() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            if (!method_48926().isClient()) {
                setParadiseLostFallen(true);
                ServerWorld overworld = getServer().getWorld(World.OVERWORLD);
                WorldBorder worldBorder = overworld.getWorldBorder();
                double xMin = Math.max(-2.9999872E7D, worldBorder.getBoundWest() + 16.0D);
                double zMin = Math.max(-2.9999872E7D, worldBorder.getBoundNorth() + 16.0D);
                double xMax = Math.min(2.9999872E7D, worldBorder.getBoundEast() - 16.0D);
                double zMax = Math.min(2.9999872E7D, worldBorder.getBoundSouth() - 16.0D);
                double scaleFactor = DimensionType.getCoordinateScaleFactor(method_48926().getDimension(), overworld.getDimension());
                BlockPos blockPos3 = new BlockPos((int) MathHelper.clamp(getX() * scaleFactor, xMin, xMax), method_48926().getTopY() + 128, (int) MathHelper.clamp(getZ() * scaleFactor, zMin, zMax));

                ((ServerPlayerEntity) (Object) this).teleport(overworld, blockPos3.getX(), blockPos3.getY(), blockPos3.getZ(), getYaw(), getPitch());
                StatusEffectInstance ef = new StatusEffectInstance(StatusEffects.NAUSEA, 160, 2, false, false, true);
                addStatusEffect(ef);
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo ci) {
    }

    @Override
    public boolean isParadiseLostFallen() {
        return paradise_lost$fallen;
    }

    @Override
    public void setParadiseLostFallen(boolean value) {
        paradise_lost$fallen = value;
    }

    @Inject(
            method = "handleFallDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (isParadiseLostFallen()) {
            paradise_lost$fallen = false;
            if (getAbilities().allowFlying) {
                cir.setReturnValue(false);
            } else {
                if (fallDistance >= 2.0F) {
                    increaseStat(Stats.FALL_ONE_CM, (int) Math.round((double) fallDistance * 100.0D));
                }
                cir.setReturnValue(super.handleFallDamage(fallDistance, damageMultiplier, ParadiseLostDamageTypes.of(method_48926(), ParadiseLostDamageTypes.FALL_FROM_PARADISE)));
            }
            cir.cancel();
        }
    }
}
