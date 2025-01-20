package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.client.rendering.util.ParadiseLostEvents;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {


    @Shadow
    private ClientWorld world;

    @Shadow
    protected abstract <T extends ParticleEffect> void addParticle(T parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Inject(method = "processWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V", at = @At("TAIL"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo ci) {
        Random random = this.world.random;
        if (eventId == ParadiseLostEvents.NITRA_EXPLODE) {
            this.world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.NEUTRAL, 0.2F, 1.0F + random.nextFloat() * 0.4F, false);
            this.world.playSoundAtBlockCenter(pos, ParadiseLostSoundEvents.ENTITY_NITRA_EXPLODE, SoundCategory.NEUTRAL, 2.0F, 0.5F + random.nextFloat() * 0.4F, false);
            for (int i = 0; i < 4; i++) {
                this.addParticle(ParticleTypes.CLOUD,
                        pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(),
                        random.nextDouble() * 0.05,
                        random.nextDouble() * 0.05,
                        random.nextDouble() * 0.05
                );
            }
            for (int i = 0; i < 8; i++) {
                this.addParticle(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(ParadiseLostItems.NITRA_BULB)),
                        pos.getX() + 0.5,
                        pos.getY(),
                        pos.getZ() + 0.5,
                        random.nextGaussian() * 0.15,
                        random.nextDouble() * 0.2,
                        random.nextGaussian() * 0.15
                );
            }
            ci.cancel();
        }

    }
}
