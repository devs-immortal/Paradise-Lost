package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.client.rendering.util.ParadiseLostEvents;
import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.armor.XpCircletItem;
import net.id.paradiselost.util.MiscUtil;
import net.id.paradiselost.util.ParadiseLostCriteria;
import net.id.paradiselost.util.ParadiseLostDamageTypes;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

import static net.id.paradiselost.world.ParadiseLostGameRules.PARADISE_VOID_KILLS;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ParadiseLostEntityExtensions {

    @Unique
    private boolean paradise_lost$fallen = false;

    @Shadow
    public int experienceLevel;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void increaseStat(Identifier stat, int amount);

    @Shadow
    public abstract PlayerAbilities getAbilities();

    @Shadow
    public float experienceProgress;

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source != world.getDamageSources().outOfWorld() || world.getRegistryKey() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return;
        }
        if (MiscUtil.useLevitationTotem(this)) {
            world.sendEntityStatus(this, ParadiseLostEvents.LEVITATION_TOTEM_USED); // custom totem animation
            setVelocity(getVelocity().x, 0.6d, getVelocity().z);
            velocityModified = true;
            addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 120, 50));
            addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 320, 1));
        } else if (world.getGameRules().getBoolean(PARADISE_VOID_KILLS)) {
            return;
        } else if (getY() < world.getBottomY() - 80) {
            fallToOverworld(world);
        }
        cir.setReturnValue(false);
    }

    @Unique
    private void fallToOverworld(ServerWorld world) {
        ServerWorld overworld = world.getServer().getOverworld();
        double scale = DimensionType.getCoordinateScaleFactor(world.getDimension(), overworld.getDimension());
        BlockPos pos = overworld.getWorldBorder().clampFloored(getX() * scale, world.getTopYInclusive() + 129, getZ() * scale);
        setParadiseLostFallen(true);
        teleport(overworld, pos.getX(), pos.getY(), pos.getZ(), Set.of(), getYaw(), getPitch(), true);
        addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 160, 2, false, false, true));
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
                    increaseStat(Stats.FALL_ONE_CM, (int) Math.round(fallDistance * 100.0D));
                }
                cir.setReturnValue(super.handleFallDamage(fallDistance, damageMultiplier, ParadiseLostDamageTypes.of(getWorld(), ParadiseLostDamageTypes.FALL_FROM_PARADISE)));
            }
        }
    }

    // olvite spyglass shenanigans
    @Inject(method = "isUsingSpyglass", at = @At("TAIL"), cancellable = true)
    public void isUsingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (this.isUsingItem() && this.getActiveItem().isOf(ParadiseLostItems.OLVITE_SPYGLASS)) {
            cir.setReturnValue(true);
        }
    }

    // charge equipped circlets
    @Inject(method = "vanishCursedItems", at = @At("TAIL"))
    public void vanishCursedItems(CallbackInfo ci) {
        for (ItemStack stack : this.getArmorItems()) {
            if (!stack.isEmpty() && stack.isOf(ParadiseLostItems.XP_CIRCLET)) {
                PlayerEntity self = (PlayerEntity) (Object) this;
                XpCircletItem.chargeCirclet(stack, self);
                if (self instanceof ServerPlayerEntity player) {
                    ParadiseLostCriteria.XP_CIRCLET_CHARGED.trigger(player, player.getBlockPos(), stack);
                    if (experienceLevel > 29) {
                        ParadiseLostCriteria.XP_CIRCLET_CHARGED_30.trigger(player, player.getBlockPos(), stack);
                    }
                }
                this.experienceLevel = 0;
                this.experienceProgress = 0;
                break;
            }
        }
    }

}
