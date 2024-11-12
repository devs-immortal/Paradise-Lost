package net.id.paradiselost.client.sound;

import net.id.paradiselost.component.ParadiseLostComponents;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class LevitaMovingMinecartSoundInstance extends MovingSoundInstance {
    private final AbstractMinecartEntity minecart;
    private float distance = 0.0F;

    public LevitaMovingMinecartSoundInstance(AbstractMinecartEntity minecart) {
        super(ParadiseLostSoundEvents.ENTITY_MINECART_ROLLING_LEVITATING, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.minecart = minecart;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.0F;
        this.x = ((float)minecart.getX());
        this.y = ((float)minecart.getY());
        this.z = ((float)minecart.getZ());
    }

    @Override
    public boolean canPlay() {
        return !this.minecart.isSilent();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public void tick() {
        if (this.minecart.isRemoved()) {
            this.setDone();
        } else {
            this.x = this.minecart.getX();
            this.y = this.minecart.getY();
            this.z = this.minecart.getZ();
            float f = (float) this.minecart.getVelocity().horizontalLength();
            var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(minecart);
            if (f >= 0.01F && this.minecart.getWorld().getTickManager().shouldTick() && floatingComponent.getFloating()) {
                this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
                this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.0F, 0.5F), 0.0F, 0.7F);
            } else {
                this.distance = 0.0F;
                this.volume = 0.0F;
            }
        }
    }

}
