package net.id.paradiselost.mixin.client.sound;

import net.id.paradiselost.component.ParadiseLostComponents;
import net.minecraft.client.sound.MinecartInsideSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartInsideSoundInstance.class)
public abstract class MinecartInsideSoundInstanceMixin extends MovingSoundInstance {

    @Shadow
    @Final
    private AbstractMinecartEntity minecart;

    protected MinecartInsideSoundInstanceMixin(SoundEvent soundEvent, SoundCategory soundCategory, Random random) {
        super(soundEvent, soundCategory, random);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (!this.minecart.isRemoved()) {
            var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(this.minecart);
            if (floatingComponent.getFloating() && !floatingComponent.isCartOnRail(this.minecart)) {
                this.volume = 0.0F;
            }
        }
    }
}
