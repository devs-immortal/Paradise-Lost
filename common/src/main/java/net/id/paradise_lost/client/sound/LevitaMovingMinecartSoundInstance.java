package net.id.paradise_lost.client.sound;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class LevitaMovingMinecartSoundInstance extends AbstractTickableSoundInstance {
    private final AbstractMinecart minecart;
    private float distance = 0.0F;

    public LevitaMovingMinecartSoundInstance(AbstractMinecart minecart, boolean inside) {
        super(inside ? ParadiseLostSoundEvents.ENTITY_MINECART_INSIDE_LEVITATING : ParadiseLostSoundEvents.ENTITY_MINECART_ROLLING_LEVITATING, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.minecart = minecart;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.x = ((float) minecart.getX());
        this.y = ((float) minecart.getY());
        this.z = ((float) minecart.getZ());
    }

    @Override
    public boolean canPlaySound() {
        return !this.minecart.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.minecart.isRemoved()) {
            this.stop();
        } else {
            this.x = this.minecart.getX();
            this.y = this.minecart.getY();
            this.z = this.minecart.getZ();
            float f = (float) this.minecart.getDeltaMovement().horizontalDistance();
            if (f >= 0.01F && this.minecart.level().tickRateManager().runsNormally() && MinecartFloating.isFloating(minecart) && !MinecartFloating.isCartOnRail(this.minecart)) {
                this.distance = Mth.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
                this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 0.5F), 0.0F, 0.7F);
            } else {
                this.distance = 0.0F;
                this.volume = 0.0F;
            }
        }
    }

}
