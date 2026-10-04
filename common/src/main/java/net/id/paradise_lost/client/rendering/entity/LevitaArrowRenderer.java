package net.id.paradise_lost.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;

public class LevitaArrowRenderer extends ArrowRenderer<LevitaArrow, ArrowRenderState> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/projectiles/levita_arrow.png");

    public LevitaArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ArrowRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(state, poseStack, buffer, LightTexture.FULL_BRIGHT);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected ResourceLocation getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }
}
