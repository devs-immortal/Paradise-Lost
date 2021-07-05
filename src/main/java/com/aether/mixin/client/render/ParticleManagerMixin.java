package com.aether.mixin.client.render;

import com.aether.client.rendering.particle.AetherParticles;
import com.aether.client.rendering.particle.FallingOrangePetalParticle;
import com.aether.client.rendering.particle.GoldenOakLeafParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Shadow
    protected abstract <T extends ParticleEffect> void registerFactory(ParticleType<T> type, ParticleManager.SpriteAwareFactory<T> factory);

    @Inject(method = "registerDefaultFactories", at = @At("TAIL"))
    private void extendDefaults(CallbackInfo ci) {
        this.registerFactory(AetherParticles.GOLDEN_OAK_LEAF, GoldenOakLeafParticle.DefaultFactory::new);
        this.registerFactory(AetherParticles.FALLING_ORANGE_PETAL, FallingOrangePetalParticle.DefaultFactory::new);
    }
}
