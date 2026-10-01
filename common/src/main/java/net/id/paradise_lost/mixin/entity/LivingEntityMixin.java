package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.ParadiseLostEvents;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.MiscUtil;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.id.paradise_lost.util.ParadiseLostVoidEscape;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
public abstract class LivingEntityMixin extends Entity implements ParadiseLostEntityExtensions {
    private boolean flipped = false;
    private int gravFlipTime;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean isDeadOrDying();

    @Shadow
    protected abstract float getDamageAfterMagicAbsorb(DamageSource source, float amount);

    @Shadow
    @Final
    public abstract boolean addEffect(MobEffectInstance effect);

    @Shadow
    public abstract float getHealth();

    @Override
    public boolean getFlipped() {
        return flipped;
    }

    @Override
    public int getFlipTime() {
        return gravFlipTime;
    }

    @Override
    public void setFlipped() {
        flipped = true;
        gravFlipTime = 0;
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

    @Override
    public boolean dampensVibrations() {
        return FloatyBootsItem.isWearing((LivingEntity) (Object) this) || super.dampensVibrations();
    }

    @ModifyVariable(method = "calculateFallDamage", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float paradiseLost$floatyBootsFeatherFalling(float fallDistance) {
        if (FloatyBootsItem.isWearing((LivingEntity) (Object) this)) {
            return Math.max(0.0F, fallDistance - FloatyBootsItem.FEATHER_FALLING_BLOCKS);
        }
        return fallDistance;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (flipped) {
            gravFlipTime++;
            if (gravFlipTime > 20) {
                flipped = false;
                this.fallDistance = 0;
            }
            if (!this.isNoGravity()) {
                Vec3 antiGravity = new Vec3(0, 0.12D, 0);
                this.setDeltaMovement(this.getDeltaMovement().add(antiGravity));
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
        if (this.level().isClientSide() || this.level().dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return;
        }
        Entity self = (Entity) (Object) this;
        if (ParadiseLostVoidEscape.tryEscape(self)) {
            ci.cancel();
            return;
        }
        this.hurt(ParadiseLostDamageTypes.of(this.level(), ParadiseLostDamageTypes.FALL_FROM_PARADISE), 4.0F);
        ci.cancel();
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isInvulnerableTo(source) && !this.level().isClientSide && !this.isDeadOrDying()) {
            if (source.is(DamageTypeTags.IS_FALL)) {
                float modified = this.getDamageAfterMagicAbsorb(source, amount);

                if ((modified >= 10.0 || modified >= getHealth()) && MiscUtil.useLevitationTotem((LivingEntity) (Entity) this)) {

                    level().broadcastEntityEvent(this, ParadiseLostEvents.LEVITATION_TOTEM_USED);
                    setDeltaMovement(this.getDeltaMovement().x, 0.6d, this.getDeltaMovement().z);
                    hurtMarked = true;
                    addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 15, 1));

                    cir.setReturnValue(false);
                }
            } else if (this.level().dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY
                    && (ParadiseLostVoidEscape.isOutOfWorldDamage(source)
                    || source == level().damageSources().fellOutOfWorld())) {
                if (MiscUtil.useLevitationTotem((LivingEntity) (Entity) this)) {

                    level().broadcastEntityEvent(this, ParadiseLostEvents.LEVITATION_TOTEM_USED);
                    setDeltaMovement(this.getDeltaMovement().x, 0.6d, this.getDeltaMovement().z);
                    hurtMarked = true;
                    addEffect(new MobEffectInstance(MobEffects.LEVITATION, 110, 30));
                    addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 320, 1));

                    cir.setReturnValue(false);
                } else if (ParadiseLostVoidEscape.tryEscape((Entity) (Object) this)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "onEquipItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"))
    public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if (slot == EquipmentSlot.HEAD && newStack.is(ItemRegistry.XP_CIRCLET.get())) {
            XpCircletItem.dischargeCirclet(newStack, (Player) (Object) this);
        }
    }

    @Unique
    @Nullable
    private ItemEntity dropStackInternal(ItemStack stack) {
        Optional<RecipeHolder<SmeltingRecipe>> optional = this.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level());
        if (optional.isPresent()) {
            ItemStack itemStack = ((optional.get()).value()).getResultItem(level().registryAccess());
            if (!itemStack.isEmpty()) {
                return this.spawnAtLocation(itemStack.copyWithCount(stack.getCount()), 0.0F);
            }
        }
        return this.spawnAtLocation(stack);
    }

}
