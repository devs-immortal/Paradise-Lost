package net.id.paradise_lost.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.MiscUtil;
import net.id.paradise_lost.util.ParadiseLostEvents;
import net.id.paradise_lost.util.ParadiseLostVoidEscape;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ParadiseLostEntityExtensions {
    @Unique
    private static final int GRAVITY_FLIP_TICKS = 20;
    @Unique
    private static final double GRAVITY_FLIP_LIFT = 0.12D;
    @Unique
    private static final float TOTEM_FALL_DAMAGE = 10.0F;

    @Unique
    private boolean paradiseLost$flipped = false;
    @Unique
    private int paradiseLost$flipTime;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean isDeadOrDying();

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);

    @Shadow
    @Final
    public abstract boolean addEffect(MobEffectInstance effect);

    @Shadow
    public abstract float getHealth();

    @Unique
    private LivingEntity paradiseLost$self() {
        return (LivingEntity) (Object) this;
    }

    @Override
    public boolean getFlipped() {
        return paradiseLost$flipped;
    }

    @Override
    public int getFlipTime() {
        return paradiseLost$flipTime;
    }

    @Override
    public void setFlipped() {
        paradiseLost$flipped = true;
        paradiseLost$flipTime = 0;
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$handleGravityFlip(byte id, CallbackInfo ci) {
        if (id == ParadiseLostEvents.GRAVITY_FLIP) {
            setFlipped();
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void paradiseLost$tickGravityFlip(CallbackInfo ci) {
        if (!paradiseLost$flipped) {
            return;
        }
        if (++paradiseLost$flipTime > GRAVITY_FLIP_TICKS) {
            paradiseLost$flipped = false;
            fallDistance = 0;
        }
        if (!isNoGravity()) {
            setDeltaMovement(getDeltaMovement().add(0, GRAVITY_FLIP_LIFT, 0));
        }
    }

    @Override
    public boolean dampensVibrations() {
        return FloatyBootsItem.isWearing(paradiseLost$self()) || super.dampensVibrations();
    }

    @ModifyReturnValue(method = "getAttributeValue", at = @At("RETURN"))
    private double paradiseLost$brokenFloatyLeggingsNoArmor(double original, Holder<Attribute> attribute) {
        if (!attribute.is(Attributes.ARMOR)) {
            return original;
        }
        ItemStack legs = paradiseLost$self().getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof FloatyLeggingsItem leggings && FloatyLeggingsItem.isBroken(legs)) {
            return Math.max(0.0D, original - FloatyLeggingsItem.getDefensePoints(leggings));
        }
        return original;
    }

    @ModifyVariable(method = "calculateFallDamage", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float paradiseLost$floatyBootsFeatherFalling(float fallDistance) {
        if (FloatyBootsItem.isWearing(paradiseLost$self())) {
            return Math.max(0.0F, fallDistance - FloatyBootsItem.FEATHER_FALLING_BLOCKS);
        }
        return fallDistance;
    }

    @ModifyReturnValue(method = "getMaxHealth", at = @At("RETURN"))
    private float paradiseLost$moaMaxHealth(float original) {
        if (paradiseLost$self() instanceof MoaEntity moa) {
            var genes = moa.getGenes();
            return genes.isInitialized() ? genes.getAttribute(MoaAttributes.MAX_HEALTH) : 40F;
        }
        return original;
    }

    @ModifyArg(
            method = "dropFromLootTable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
            ),
            index = 2
    )
    private Consumer<ItemStack> paradiseLost$smeltIgnitedDrops(LootParams params, long seed, Consumer<ItemStack> output) {
        ItemStack weapon = params.getParameter(LootContextParams.DAMAGE_SOURCE).getWeaponItem();
        if (weapon != null && weapon.is(ParadiseLostItemTags.IGNITING_TOOLS)) {
            return this::paradiseLost$dropSmelted;
        }
        return output;
    }

    @Unique
    private void paradiseLost$dropSmelted(ItemStack stack) {
        Level level = level();
        ItemStack drop = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level)
                .map(recipe -> recipe.value().getResultItem(level.registryAccess()))
                .filter(result -> !result.isEmpty())
                .map(result -> result.copyWithCount(stack.getCount()))
                .orElse(stack);
        spawnAtLocation(drop);
    }

    @Inject(method = "onBelowWorld", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$customVoidDamage(CallbackInfo ci) {
        if (level().isClientSide() || level().dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return;
        }
        if (hasEffect(MobEffects.LEVITATION)) {
            ci.cancel();
            return;
        }
        if (paradiseLost$self().isHolding(ItemRegistry.TOTEM_OF_LEVITATION.get())) {
            return;
        }
        // Escape when paradiseVoidKills is false. When the rule is true (or escape fails),
        // leave vanilla void damage alone.
        if (ParadiseLostVoidEscape.tryEscape(this)) {
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$levitationTotem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (isInvulnerableTo(source) || level().isClientSide() || isDeadOrDying()) {
            return;
        }
        if (source.is(DamageTypeTags.IS_FALL)) {
            float damage = paradiseLost$previewMagicAbsorb(source, amount);
            if ((damage >= TOTEM_FALL_DAMAGE || damage >= getHealth())
                    && paradiseLost$useLevitationTotem(new MobEffectInstance(MobEffects.SLOW_FALLING, 15, 1))) {
                cir.setReturnValue(false);
            }
        } else if (level().dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY
                && (ParadiseLostVoidEscape.isOutOfWorldDamage(source) || source == level().damageSources().fellOutOfWorld())) {
            boolean player = paradiseLost$self() instanceof Player;
            if (paradiseLost$useLevitationTotem(
                    new MobEffectInstance(MobEffects.LEVITATION, player ? 120 : 110, player ? 50 : 30),
                    new MobEffectInstance(MobEffects.SLOW_FALLING, 320, 1))
                    || ParadiseLostVoidEscape.tryEscape(this)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Unique
    private boolean paradiseLost$useLevitationTotem(MobEffectInstance... effects) {
        if (!MiscUtil.useLevitationTotem(paradiseLost$self())) {
            return false;
        }
        level().broadcastEntityEvent(this, ParadiseLostEvents.LEVITATION_TOTEM_USED);
        setDeltaMovement(getDeltaMovement().x, 0.6D, getDeltaMovement().z);
        hurtMarked = true;
        for (MobEffectInstance effect : effects) {
            addEffect(effect);
        }
        return true;
    }

    @Unique
    private float paradiseLost$previewMagicAbsorb(DamageSource source, float damageAmount) {
        if (source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return damageAmount;
        }
        MobEffectInstance resistance = getEffect(MobEffects.DAMAGE_RESISTANCE);
        if (resistance != null && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            int reduction = (resistance.getAmplifier() + 1) * 5;
            damageAmount = Math.max(damageAmount * (25 - reduction) / 25.0F, 0.0F);
        }
        if (damageAmount <= 0.0F || source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            return damageAmount;
        }
        float protection = 0.0F;
        if (level() instanceof ServerLevel serverLevel) {
            protection = EnchantmentHelper.getDamageProtection(serverLevel, paradiseLost$self(), source);
        }
        if (protection > 0.0F) {
            damageAmount = CombatRules.getDamageAfterMagicAbsorb(damageAmount, protection);
        }
        return damageAmount;
    }

    @Inject(method = "onEquipItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"))
    private void paradiseLost$dischargeXpCirclet(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if (slot == EquipmentSlot.HEAD && newStack.is(ItemRegistry.XP_CIRCLET.get()) && paradiseLost$self() instanceof Player player) {
            XpCircletItem.dischargeCirclet(newStack, player);
        }
    }
}
