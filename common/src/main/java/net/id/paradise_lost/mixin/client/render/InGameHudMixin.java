package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.client.rendering.ui.ParadiseLostOverlayRegistrar;
import net.id.paradise_lost.client.rendering.ui.BloodstoneHUDRenderer;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

@Mixin(Gui.class)
public abstract class InGameHudMixin {

    @Unique
    private static final ResourceLocation OLVITE_SPYGLASS_SCOPE = id("textures/hud/olvite_spyglass_scope.png");

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void renderTextureOverlay(GuiGraphics context, ResourceLocation texture, float opacity);

    @Inject(method = "renderItemHotbar", at = @At("HEAD"))
    public void renderOverlay(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        List<ParadiseLostOverlayRegistrar.Overlay> overlays = ParadiseLostOverlayRegistrar.getOverlays();
        Entity entity = Minecraft.getInstance().cameraEntity;
        if (entity instanceof LivingEntity player) {
            overlays.forEach(overlay -> {
                if (overlay.renderPredicate().test(player)) {
                    renderTextureOverlay(context, overlay.path(), overlay.opacityProvider().apply(player));
                }
            });
        }
        BloodstoneHUDRenderer.render(context);
    }

    @WrapOperation(
            method = "renderPortalOverlay",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;"
                            + "getParticleIcon(Lnet/minecraft/world/level/block/state/BlockState;)"
                            + "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"
            )
    )
    private TextureAtlasSprite paradiseLost$bluePortalSprite(
            BlockModelShaper shaper, BlockState state, Operation<TextureAtlasSprite> original
    ) {
        if (paradiseLost$isBluePortal()) {
            return shaper.getParticleIcon(BlockRegistry.BLUE_PORTAL.get().defaultBlockState());
        }
        return original.call(shaper, state);
    }

    @Unique
    private boolean paradiseLost$isBluePortal() {
        if (this.minecraft.player == null) {
            return false;
        }
        if (this.minecraft.player.portalProcess != null) {
            return this.minecraft.player.portalProcess.isSamePortal(BlockRegistry.BLUE_PORTAL.get());
        }
        return this.minecraft.player.level()
                .getBlockState(this.minecraft.player.blockPosition())
                .is(BlockRegistry.BLUE_PORTAL.get());
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void renderSpyglassOverlay(GuiGraphics context, float scale, CallbackInfo ci) {
        if (this.minecraft.player.getUseItem().is(ItemRegistry.OLVITE_SPYGLASS.get())) {
            float f = (float) Math.min(context.guiWidth(), context.guiHeight());
            float h = Math.min((float) context.guiWidth() / f, (float) context.guiHeight() / f) * scale;
            int i = Mth.floor(f * h);
            int j = Mth.floor(f * h);
            int k = (context.guiWidth() - i) / 2;
            int l = (context.guiHeight() - j) / 2;
            int m = k + i;
            int n = l + j;
            RenderSystem.enableBlend();
            context.blit(OLVITE_SPYGLASS_SCOPE, k, l, -90, 0.0F, 0.0F, i, j, i, j);
            RenderSystem.disableBlend();
            context.fill(RenderType.guiOverlay(), 0, n, context.guiWidth(), context.guiHeight(), -90, CommonColors.BLACK);
            context.fill(RenderType.guiOverlay(), 0, 0, context.guiWidth(), l, -90, CommonColors.BLACK);
            context.fill(RenderType.guiOverlay(), 0, l, k, n, -90, CommonColors.BLACK);
            context.fill(RenderType.guiOverlay(), m, l, context.guiWidth(), n, -90, CommonColors.BLACK);
            ci.cancel();
        }
    }
}
