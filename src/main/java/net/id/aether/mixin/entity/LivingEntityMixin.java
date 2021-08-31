package net.id.aether.mixin.entity;

import com.google.common.collect.Sets;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.id.aether.api.MoaAttributes;
import net.id.aether.entities.AetherEntityExtensions;
import net.id.aether.entities.passive.MoaEntity;
import net.id.aether.items.AetherItems;
import net.id.aether.items.tools.AetherToolMaterials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
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
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements AetherEntityExtensions {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private boolean flipped = false;

    private int gravFlipTime;

    @Override
    public boolean getFlipped(){
        return flipped;
    }

    @Override
    public void setFlipped(){
        flipped = true;
        gravFlipTime = 0;
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(method = "travel", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;isTouchingWater()Z"))
    private double changeGravity(double gravity) {
        boolean isFalling = this.getVelocity().y <= 0.0D;

        if ((Object) this instanceof PlayerEntity playerEntity) {
            Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(playerEntity);

            if (componentOptional.isPresent()) {
                // Get parachutes from trinket slots
                final Set<Item> validItems = Sets.newHashSet(AetherItems.CLOUD_PARACHUTE, AetherItems.GOLDEN_CLOUD_PARACHUTE);
                for (Item item : validItems) {
                    if (componentOptional.get().isEquipped(item)) {
                        if (isFalling && !this.hasStatusEffect(StatusEffects.SLOW_FALLING) && !isTouchingWater() && !playerEntity.isSneaking()) {
                            gravity -= 0.07;
                            this.fallDistance = 0;
                        }
                        break;
                    }
                }
            }
        }

        return gravity;
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (cir.getReturnValue() && attacker instanceof LivingEntity) {
            Item item = ((LivingEntity) attacker).getMainHandStack().getItem();
            if (item instanceof ToolItem tool && tool.getMaterial() == AetherToolMaterials.GRAVITITE) {
                this.addVelocity(0, amount / 20 + 0.1, 0);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci){
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

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    private void getMoaMaxHealth(CallbackInfoReturnable<Float> cir) {
        if((Object) this instanceof MoaEntity moa) {
            var genes = moa.getGenes();
            cir.setReturnValue(genes.isInitialized() ? genes.getAttribute(MoaAttributes.MAX_HEALTH) : 40F);
            cir.cancel();
        }
    }
}
