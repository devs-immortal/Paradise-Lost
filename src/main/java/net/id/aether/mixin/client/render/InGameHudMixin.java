package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.client.rendering.ui.AetherOverlayRegistrar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin {

    @Shadow
    protected abstract void renderOverlay(Identifier texture, float opacity);

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    public void renderOverlay(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        var overlays = AetherOverlayRegistrar.getOverlays();
        var entity = MinecraftClient.getInstance().cameraEntity;
        if(entity instanceof LivingEntity player) {
            overlays.forEach(overlay -> {
                if(overlay.renderPredicate().test(player)) {
                    renderOverlay(overlay.path(), overlay.opacityProvider().apply(player));
                }
            });
        }
    }
}
