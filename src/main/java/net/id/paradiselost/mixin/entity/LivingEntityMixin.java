package net.id.paradiselost.mixin.entity;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ParadiseLostEntityExtensions {
    private boolean flipped = false;
    private int gravFlipTime;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean getFlipped() {
        return flipped;
    }

    @Override
    public void setFlipped() {
        flipped = true;
        gravFlipTime = 0;
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(
            method = "travel",
            at = @At(
                value = "INVOKE_ASSIGN",
                ordinal = 0,
                target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"
            )
    )
    private double changeGravity(double gravity) {
        LivingEntity entity = (LivingEntity) (Object) this;

        boolean isFalling = this.getVelocity().y <= 0.0D;
        if (isFalling && !this.hasStatusEffect(StatusEffects.SLOW_FALLING) && !isTouchingWater() && !isSneaking()) {
            // Get parachutes from trinket slots
            Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(entity);
            boolean isWearingParachute = componentOptional.isPresent()
                    && componentOptional.get().isEquipped(e -> e.isIn(ParadiseLostItemTags.PARACHUTES));

            if (isWearingParachute) {
                gravity -= 0.07;
                this.fallDistance = 0;
            } else if (entity.hasPassengers() && entity.getPassengerList().stream().anyMatch(passenger ->
                    passenger.getType().equals(ParadiseLostEntityTypes.PARADISE_HARE))) {
                gravity -= 0.05;
                this.fallDistance = 0; // alternatively, remove & replace with fall damage dampener
            }
        }

        return gravity;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (flipped) {
            gravFlipTime++;
            if (gravFlipTime > 20) {
                flipped = false;
                this.fallDistance = 0;
            }
            if (!this.hasNoGravity()) {
                Vec3d antiGravity = new Vec3d(0, 0.12D, 0);
                this.setVelocity(this.getVelocity().add(antiGravity));
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
}
