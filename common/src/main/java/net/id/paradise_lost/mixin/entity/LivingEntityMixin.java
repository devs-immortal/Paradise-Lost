package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.ParadiseLostEvents;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.MiscUtil;
import net.id.paradise_lost.util.ParadiseLostVoidEscape;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ParadiseLostEntityExtensions {

    @Unique
    private boolean paradiseLost$flipped = false;

    @Unique
    private int paradiseLost$gravFlipTime = 0;


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

    @Override
    public boolean getFlipped() {
        return paradiseLost$flipped;
    }

    @Override
    public int getFlipTime() {
        return paradiseLost$gravFlipTime;
    }

    @Override
    public void setFlipped() {
        paradiseLost$flipped = true;
        paradiseLost$gravFlipTime = 0;
    }

    @Unique
    private LivingEntity paradiseLost$self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$handleGravityFlip(byte id, CallbackInfo ci) {
        if (id == ParadiseLostEvents.GRAVITY_FLIP) {
            setFlipped();
            ci.cancel();
        }
    }

    @Inject(method = "updateWalkAnimation", at = @At("HEAD"))
    private void paradiseLost$floatyWalkAnimation(float posDelta, CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            ((ParadiseLostEntityExtensions) player).isFloatyAnchored();
        }
    }

    @ModifyReturnValue(method = "getAttributeValue", at = @At("RETURN"))
    private double paradiseLost$brokenFloatyLeggingsNoArmor(double original, Holder<Attribute> attribute) {
        if (!attribute.is(Attributes.ARMOR)) {
            return original;
        }
        ItemStack legs = (paradiseLost$self()).getItemBySlot(EquipmentSlot.LEGS);
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

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (paradiseLost$flipped) {
            paradiseLost$gravFlipTime++;
            LivingEntity self = paradiseLost$self();
            if (paradiseLost$gravFlipTime > 20) {
                paradiseLost$flipped = false;
                self.fallDistance = 0;
            }
            if (!self.isNoGravity()) {
                Vec3 antiGravity = new Vec3(0, 0.12D, 0);
                self.setDeltaMovement(self.getDeltaMovement().add(antiGravity));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    private void getMoaMaxHealth(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof MoaEntity moa) {
            var genes = moa.getGenes();
            cir.setReturnValue(genes.isInitialized() ? genes.getAttribute(MoaAttributes.MAX_HEALTH) : 40F);
            cir.cancel();
        }
    }

    @ModifyArgs(
            method = "dropFromLootTable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
            )
    )
    private void dropSmeltedLoot(Args args) {
        LootParams lootParams = args.get(0);
        var weapon = lootParams.getParameter(LootContextParams.DAMAGE_SOURCE).getWeaponItem();
        if (weapon != null && weapon.is(ParadiseLostItemTags.IGNITING_TOOLS)) {
            args.set(2, (Consumer<ItemStack>) this::dropStackInternal);
        }
    }

    @Inject(method = "onBelowWorld", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$customVoidDamage(CallbackInfo ci) {
        LivingEntity self = paradiseLost$self();
        if (self.level().isClientSide()
                || self.level().dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return;
        }
        if (this.hasEffect(MobEffects.LEVITATION)) {
            ci.cancel();
            return;
        }
        if (self.isHolding(ItemRegistry.TOTEM_OF_LEVITATION.get())) {
            return;
        }
        // Escape when paradiseVoidKills is false. When the rule is true (or escape fails),
        // leave vanilla void damage alone.
        if (ParadiseLostVoidEscape.tryEscape((Entity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = paradiseLost$self();
        if (!self.isInvulnerableTo(source)
                && !self.level().isClientSide && !this.isDeadOrDying()) {
            if (source.is(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL)) {
                float modified = paradiseLost$previewMagicAbsorb(source, amount);
                if ((modified >= 10.0 || modified >= getHealth()) && MiscUtil.useLevitationTotem(self)) {
                    self.level().broadcastEntityEvent(self, ParadiseLostEvents.LEVITATION_TOTEM_USED);
                    self.setDeltaMovement(self.getDeltaMovement().x, 0.6d,
                            self.getDeltaMovement().z);
                    self.hurtMarked = true;
                    addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 15, 1));
                    cir.setReturnValue(false);
                }
            } else if (self.level().dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY
                    && (ParadiseLostVoidEscape.isOutOfWorldDamage(source)
                    || source == self.level().damageSources().fellOutOfWorld())) {
                if (MiscUtil.useLevitationTotem(self)) {
                    self.level().broadcastEntityEvent(self, ParadiseLostEvents.LEVITATION_TOTEM_USED);
                    self.setDeltaMovement(self.getDeltaMovement().x, 0.6d, self.getDeltaMovement().z);
                    self.hurtMarked = true;
                    addEffect(new MobEffectInstance(MobEffects.LEVITATION, 110, 30));
                    addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 320, 1));

                    cir.setReturnValue(false);
                } else if (ParadiseLostVoidEscape.tryEscape(self)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Unique
    private float paradiseLost$previewMagicAbsorb(DamageSource source, float damageAmount) {
        LivingEntity self = paradiseLost$self();
        if (source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return damageAmount;
        }
        if (hasEffect(MobEffects.DAMAGE_RESISTANCE) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            MobEffectInstance resistance = this.getEffect(MobEffects.DAMAGE_RESISTANCE);
            if (resistance != null) {
                int reduction = (resistance.getAmplifier() + 1) * 5;
                damageAmount = Math.max(damageAmount * (25 - reduction) / 25.0F, 0.0F);
            }
        }
        if (damageAmount <= 0.0F || source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            return damageAmount;
        }
        float protection = 0.0F;
        if (self.level() instanceof ServerLevel serverLevel) {
            protection = EnchantmentHelper.getDamageProtection(serverLevel, self, source);
        }
        if (protection > 0.0F) {
            damageAmount = CombatRules.getDamageAfterMagicAbsorb(damageAmount, protection);
        }
        return damageAmount;
    }

    @Inject(method = "onEquipItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"))
    public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if (slot == EquipmentSlot.HEAD && newStack.is(ItemRegistry.XP_CIRCLET.get()) && paradiseLost$self() instanceof Player player) {
            XpCircletItem.dischargeCirclet(newStack, player);
        }
    }

    @Unique
    @Nullable
    private ItemEntity dropStackInternal(ItemStack stack) {
        LivingEntity self = paradiseLost$self();
        Optional<RecipeHolder<SmeltingRecipe>> optional = self.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING,
                new SingleRecipeInput(stack), self.level());
        if (optional.isPresent()) {
            ItemStack itemStack = ((optional.get()).value()).getResultItem(self.level().registryAccess());
            if (!itemStack.isEmpty()) {
                return self.spawnAtLocation(itemStack.copyWithCount(stack.getCount()), 0.0F);
            }
        }
        return self.spawnAtLocation(stack);
    }
}
