package com.aether.client.rendering.entity.layer;

import com.aether.Aether;
import com.aether.api.AetherAPI;
import com.aether.api.accessories.AccessoryType;
import com.aether.api.player.IPlayerAether;
import com.aether.client.model.entity.PlayerWingModel;
import com.aether.items.AetherItems;
import com.aether.items.accessories.ItemAccessory;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class AccessoryLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final Identifier TEXTURE_VALKYRIE = Aether.locate("textures/entity/valkyrie/valkyrie.png");
    private final boolean slimFit;
    private final PlayerWingModel modelWings;
    public BipedEntityModel<AbstractClientPlayerEntity> modelMisc;
    public PlayerEntityModel<AbstractClientPlayerEntity> modelPlayer;

    public AccessoryLayer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, boolean slimFit) {
        super(context);

        this.modelPlayer = context.getModel();
        this.slimFit = slimFit;
        this.modelWings = new PlayerWingModel(1.0F);
        this.modelMisc = new BipedEntityModel<>(1.0F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float customAngle, float netHeadYaw, float headPitch) {
        IPlayerAether playerAether = AetherAPI.get(player);

        GlStateManager.pushMatrix();

        this.modelMisc.setAttributes(this.modelPlayer);
        this.modelWings.setAttributes(this.modelPlayer);

        this.modelMisc.animateModel(player, limbAngle, limbDistance, tickDelta);
        this.modelWings.animateModel(player, limbAngle, limbDistance, tickDelta);

        GlStateManager.scalef(0.9375F, 0.9375F, 0.9375F);

        if (this.slimFit) {
            GlStateManager.translated(0.0D, 0.024D, 0.0D);
        }

        GlStateManager.enableAlphaTest();

        GlStateManager.translated(0, player.isSneaking() ? 0.25D : 0, 0);

        this.modelMisc.setAngles(player, limbAngle, limbDistance, customAngle, netHeadYaw, headPitch);
        this.modelWings.setAngles(player, limbAngle, limbDistance, customAngle, netHeadYaw, headPitch);

        if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.INVISIBILITY_CAPE))) {
            return;
        }

        if (!playerAether.getAccessoryInventory().getInvStack(AccessoryType.PENDANT).isEmpty()) {
            ItemAccessory pendant = (ItemAccessory) playerAether.getAccessoryInventory().getInvStack(AccessoryType.PENDANT).getItem();

            int color = pendant.getColor();
            float red = ((color >> 16) & 0xff) / 255F;
            float green = ((color >> 8) & 0xff) / 255F;
            float blue = (color & 0xff) / 255F;

            if (player.hurtTime > 0) {
                GlStateManager.color4f(1.0F, 0.5F, 0.5F, 1.0f);
            } else {
                GlStateManager.color4f(red, green, blue, 1.0f);
            }

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(pendant.getTexture()));
            this.modelMisc.torso.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);

            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
        }

        if (!playerAether.getAccessoryInventory().getInvStack(AccessoryType.CAPE).isEmpty()) {
            ItemAccessory cape = (ItemAccessory) playerAether.getAccessoryInventory().getInvStack(AccessoryType.CAPE).getItem();

            if (player.canRenderCapeTexture() && !player.isInvisible()) {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.pushMatrix();
                GlStateManager.translatef(0.0F, 0.0F, 0.125F);

                double double_1 = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp(tickDelta, player.prevX, player.getX());
                double double_2 = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp(tickDelta, player.prevY, player.getY());
                double double_3 = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
                float float_8 = player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw);
                double double_4 = MathHelper.sin(float_8 * 0.017453292F);
                double double_5 = -MathHelper.cos(float_8 * 0.017453292F);
                float float_9 = (float) double_2 * 10.0F;
                float_9 = MathHelper.clamp(float_9, -6.0F, 32.0F);
                float float_10 = (float) (double_1 * double_4 + double_3 * double_5) * 100.0F;
                float_10 = MathHelper.clamp(float_10, 0.0F, 150.0F);
                float float_11 = (float) (double_1 * double_5 - double_3 * double_4) * 100.0F;
                float_11 = MathHelper.clamp(float_11, -20.0F, 20.0F);

                if (float_10 < 0.0F) {
                    float_10 = 0.0F;
                }

                float float_12 = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
                float_9 += MathHelper.sin(MathHelper.lerp(tickDelta, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * float_12;

                if (player.isSneaking()) {
                    float_9 += 25.0F;
                }

                int color = cape.getColor();

                float red = ((color >> 16) & 0xff) / 255F;
                float green = ((color >> 8) & 0xff) / 255F;
                float blue = (color & 0xff) / 255F;

                if (player.hurtTime > 0) {
                    GlStateManager.color4f(1.0F, 0.5F, 0.5F, 1.0f);
                } else {
                    GlStateManager.color4f(red, green, blue, 1.0f);
                }

                Identifier TEXTURE;
                if (player.getUuid().toString().equals("47ec3a3b-3f41-49b6-b5a0-c39abb7b51ef")) {
                    TEXTURE = Aether.locate("textures/armor/accessory_swuff.png");
                } else {
                    TEXTURE = cape.getTexture();
                }

                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
                this.modelPlayer.renderCape(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }
        }

        if (!playerAether.getAccessoryInventory().getInvStack(AccessoryType.GLOVES).isEmpty()) {
            ItemAccessory gloves = (ItemAccessory) playerAether.getAccessoryInventory().getInvStack(AccessoryType.GLOVES).getItem();

            int color = gloves.getColor();

            float red = ((color >> 16) & 0xff) / 255F;
            float green = ((color >> 8) & 0xff) / 255F;
            float blue = (color & 0xff) / 255F;

            if (player.hurtTime > 0) {
                GlStateManager.color4f(1.0F, 0.5F, 0.5F, 1.0f);
            } else {
                if (gloves != AetherItems.PHOENIX_GLOVES) {
                    GlStateManager.color4f(red, green, blue, 1.0f);
                }
            }

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(gloves.getTexture()));
            this.modelMisc.leftArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
            this.modelMisc.rightArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
        }

        if (!playerAether.getAccessoryInventory().getInvStack(AccessoryType.SHIELD).isEmpty()) {
            ItemAccessory shield = (ItemAccessory) playerAether.getAccessoryInventory().getInvStack(AccessoryType.SHIELD).getItem();

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.scalef(1.1F, 1.1F, 1.1F);

            if (player.hurtTime > 0) {
                GlStateManager.color4f(1.0F, 0.5F, 0.5F, 1.0f);
            }

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(shield.getTexture()));
            this.modelMisc.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);

            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        if (playerAether.getAccessoryInventory().isWearingValkyrieSet()) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE_VALKYRIE));
            this.modelWings.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
            //this.modelWings.wingLeft.render(scale);
            //this.modelWings.wingRight.render(scale);

            if (player.hurtTime > 0) {
                GlStateManager.color4f(1.0F, 0.5F, 0.5F, 1.0f);
            } else {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
            }
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();

        GlStateManager.popMatrix();
    }

    // TODO: ???
    /*@Override
    public boolean hasHurtOverlay()
    {
        return true;
    }*/

}
