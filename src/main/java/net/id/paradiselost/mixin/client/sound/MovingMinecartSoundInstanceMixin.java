package net.id.paradiselost.mixin.client.sound;

import net.id.paradiselost.component.ParadiseLostComponents;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.sound.MovingMinecartSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovingMinecartSoundInstance.class)
public abstract class MovingMinecartSoundInstanceMixin extends MovingSoundInstance {

    @Shadow
    @Final
    private AbstractMinecartEntity minecart;

    protected MovingMinecartSoundInstanceMixin(SoundEvent soundEvent, SoundCategory soundCategory, Random random) {
        super(soundEvent, soundCategory, random);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (!this.minecart.isRemoved()) {
            var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(this.minecart);
            if (floatingComponent.getFloating()) {
                this.volume = 0.0F;
            }
        }
    }
}
