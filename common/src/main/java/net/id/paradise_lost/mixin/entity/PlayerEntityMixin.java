package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.ParadiseLostEvents;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.util.MiscUtil;
import net.id.paradise_lost.util.ParadiseLostCriteria;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.id.paradise_lost.util.ParadiseLostVoidEscape;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ParadiseLostEntityExtensions {

    private boolean paradise_lost$fallen = false;
    @Unique
    private int paradiseLost$floatyDamageTicker = 0;
    @Unique
    private boolean paradiseLost$floatyHoldingNoGravity = false;

    @Final
    @Shadow
    Inventory inventory;
    @Shadow
    public int experienceLevel;

    public PlayerEntityMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract void awardStat(ResourceLocation stat, int amount);

    @Shadow public abstract Abilities getAbilities();

    @Shadow
    public float experienceProgress;

    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();

    @Inject(
            method = "hurt",
            at = @At("HEAD"),
            cancellable = true
    )
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (level().isClientSide() || level().dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return;
        }
        if (!ParadiseLostVoidEscape.isOutOfWorldDamage(source)
                && source != level().damageSources().fellOutOfWorld()) {
            return;
        }
        if (MiscUtil.useLevitationTotem(this)) {
            level().broadcastEntityEvent(this, ParadiseLostEvents.LEVITATION_TOTEM_USED);
            setDeltaMovement(this.getDeltaMovement().x, 0.6d, this.getDeltaMovement().z);
            hurtMarked = true;
            addEffect(new MobEffectInstance(MobEffects.LEVITATION, 120, 50));
            addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 320, 1));
            cir.setReturnValue(false);
        } else if (ParadiseLostVoidEscape.tryEscape((Entity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void paradiseLost$preTickFloatyLeggings(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!FloatyLeggingsItem.canUseFloaty(player) || player.onGround()) {
            if (isFloatyAnchored()) {
                setFloatyAnchored(false);
            } else {
                paradiseLost$clearFloatyHover(player);
            }
            paradiseLost$floatyDamageTicker = 0;
            return;
        }

        if (isFloatyAnchored() && FloatyLeggingsItem.isFloatyEnabled(player.getItemBySlot(EquipmentSlot.LEGS))) {
            FloatyLeggingsItem.beginAnchoredHover(player);
            paradiseLost$floatyHoldingNoGravity = true;
        } else {
            if (isFloatyAnchored()) {
                setFloatyAnchored(false);
            } else {
                paradiseLost$clearFloatyHover(player);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void paradiseLost$tickFloatyLeggings(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!FloatyLeggingsItem.canUseFloaty(player) || player.onGround()) {
            return;
        }

        if (isFloatyAnchored()) {
            if (!FloatyLeggingsItem.isFloatyEnabled(player.getItemBySlot(EquipmentSlot.LEGS))) {
                setFloatyAnchored(false);
                return;
            }
            if (!player.level().isClientSide() && ++paradiseLost$floatyDamageTicker >= FloatyLeggingsItem.DURABILITY_INTERVAL_TICKS) {
                paradiseLost$floatyDamageTicker = 0;
                FloatyLeggingsItem.hurtWhileAnchored(player);
            }
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$floatyTravel(Vec3 movementInput, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!isFloatyAnchored()
                || !FloatyLeggingsItem.canUseFloaty(player)
                || !FloatyLeggingsItem.isFloatyEnabled(player.getItemBySlot(EquipmentSlot.LEGS))) {
            return;
        }
        FloatyLeggingsItem.travelAnchored(player, this.xxa, this.zza);
        paradiseLost$floatyHoldingNoGravity = true;
        ci.cancel();
    }

    @Unique
    private void paradiseLost$clearFloatyHover(Player player) {
        FloatyLeggingsItem.clearAnchoredHover(player, paradiseLost$floatyHoldingNoGravity);
        paradiseLost$floatyHoldingNoGravity = false;
    }

    @Override
    public boolean isFloatyAnchored() {
        Boolean anchored = Services.ATTACHMENTS.getAttachedValue(this, CommonDataAttachments.FLOATY_ANCHORED);
        return Boolean.TRUE.equals(anchored);
    }

    @Override
    public void setFloatyAnchored(boolean anchored) {
        Player player = (Player) (Object) this;
        Services.ATTACHMENTS.setAttachedValue(player, CommonDataAttachments.FLOATY_ANCHORED, anchored);
        if (!anchored) {
            this.paradiseLost$floatyDamageTicker = 0;
            paradiseLost$clearFloatyHover(player);
        } else {
            FloatyLeggingsItem.onAnchorStarted(player);
            paradiseLost$floatyHoldingNoGravity = true;
            paradiseLost$floatyDamageTicker = 0;
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
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
            method = "causeFallDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (isParadiseLostFallen()) {
            paradise_lost$fallen = false;
            if (getAbilities().mayfly) {
                cir.setReturnValue(false);
            } else {
                if (fallDistance >= 2.0F) {
                    awardStat(Stats.FALL_ONE_CM, (int) Math.round((double) fallDistance * 100.0D));
                }
                cir.setReturnValue(super.causeFallDamage(fallDistance, damageMultiplier, ParadiseLostDamageTypes.of(level(), ParadiseLostDamageTypes.FALL_FROM_PARADISE)));
            }
            cir.cancel();
        }
    }

    @Inject(method = "isScoping", at = @At("TAIL"), cancellable = true)
    public void isUsingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (this.isUsingItem() && this.getUseItem().is(ItemRegistry.OLVITE_SPYGLASS.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "destroyVanishingCursedItems", at = @At("TAIL"))
    public void vanishCursedItems(CallbackInfo ci) {
        for (ItemStack stack : this.getArmorSlots()) {
            if (!stack.isEmpty() && stack.is(ItemRegistry.XP_CIRCLET.get())) {
                XpCircletItem.chargeCirclet(stack, (Player) (Object) this);
                if ((Object) this instanceof ServerPlayer player) {
                    ParadiseLostCriteria.XP_CIRCLET_CHARGED.trigger(player, player.blockPosition(), stack);
                    if (experienceLevel > 29) {
                        ParadiseLostCriteria.XP_CIRCLET_CHARGED_30.trigger(player, player.blockPosition(), stack);
                    }
                }
                this.experienceLevel = 0;
                this.experienceProgress = 0;
                break;
            }
        }
    }

}
