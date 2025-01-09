package net.id.paradiselost.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.rendering.ui.ParadiseLostOverlayRegistrar;
import net.id.paradiselost.client.rendering.ui.BloodstoneHUDRenderer;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.id.paradiselost.ParadiseLost.locate;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin {

    @Unique
    private static final Identifier OLVITE_SPYGLASS_SCOPE = locate("textures/hud/olvite_spyglass_scope.png");

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    public void renderOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        List<ParadiseLostOverlayRegistrar.Overlay> overlays = ParadiseLostOverlayRegistrar.getOverlays();
        Entity entity = MinecraftClient.getInstance().cameraEntity;
        if (entity instanceof LivingEntity player) {
            overlays.forEach(overlay -> {
                if (overlay.renderPredicate().test(player)) {
                    renderOverlay(context, overlay.path(), overlay.opacityProvider().apply(player));
                }
            });
        }
        BloodstoneHUDRenderer.render(context);
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void renderSpyglassOverlay(DrawContext context, float scale, CallbackInfo ci) {
        if (this.client.player.getActiveItem().isOf(ParadiseLostItems.OLVITE_SPYGLASS)) {
            float f = (float)Math.min(context.getScaledWindowWidth(), context.getScaledWindowHeight());
            float h = Math.min((float)context.getScaledWindowWidth() / f, (float)context.getScaledWindowHeight() / f) * scale;
            int i = MathHelper.floor(f * h);
            int j = MathHelper.floor(f * h);
            int k = (context.getScaledWindowWidth() - i) / 2;
            int l = (context.getScaledWindowHeight() - j) / 2;
            int m = k + i;
            int n = l + j;
            RenderSystem.enableBlend();
            context.drawTexture(OLVITE_SPYGLASS_SCOPE, k, l, -90, 0.0F, 0.0F, i, j, i, j);
            RenderSystem.disableBlend();
            context.fill(RenderLayer.getGuiOverlay(), 0, n, context.getScaledWindowWidth(), context.getScaledWindowHeight(), -90, Colors.BLACK);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), l, -90, Colors.BLACK);
            context.fill(RenderLayer.getGuiOverlay(), 0, l, k, n, -90, Colors.BLACK);
            context.fill(RenderLayer.getGuiOverlay(), m, l, context.getScaledWindowWidth(), n, -90, Colors.BLACK);
            ci.cancel();
        }
    }
}
