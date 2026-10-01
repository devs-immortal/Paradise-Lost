package net.id.paradise_lost.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LevitaArrowRenderer extends ArrowRenderer<LevitaArrow> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/projectiles/levita_arrow.png");

    public LevitaArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(LevitaArrow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, LightTexture.FULL_BRIGHT);
    }

    @Override
    public ResourceLocation getTextureLocation(LevitaArrow entity) {
        return TEXTURE;
    }
}
