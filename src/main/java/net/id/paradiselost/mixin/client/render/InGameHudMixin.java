package net.id.paradiselost.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.rendering.ui.ParadiseLostOverlayRegistrar;
import net.id.paradiselost.client.rendering.ui.BloodstoneHUDRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin {

    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    public void renderOverlay(float tickDelta, DrawContext context, CallbackInfo ci) {
        List<ParadiseLostOverlayRegistrar.Overlay> overlays = ParadiseLostOverlayRegistrar.getOverlays();
        Entity entity = MinecraftClient.getInstance().cameraEntity;
        if (entity instanceof LivingEntity player) {
            overlays.forEach(overlay -> {
                if (overlay.renderPredicate().test(player)) {
                    renderOverlay(context, overlay.path(), overlay.opacityProvider().apply(player));
                }
            });
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;render(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void renderBloodstone(DrawContext context, float tickDelta, CallbackInfo ci) {
        // TODO: probably move this inject to somewhere else (PL-1.7)
        BloodstoneHUDRenderer.render(context, tickDelta);
    }
}
