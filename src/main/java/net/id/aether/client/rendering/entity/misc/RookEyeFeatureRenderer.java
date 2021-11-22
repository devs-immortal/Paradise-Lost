package net.id.aether.client.rendering.entity.misc;

import com.google.common.collect.ImmutableList;
import net.id.aether.Aether;
import net.id.aether.client.model.entity.RookModel;
import net.id.aether.entities.misc.RookEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.List;

public class RookEyeFeatureRenderer extends EyesFeatureRenderer<RookEntity, RookModel> {

    public static final List<RenderLayer> EYES;
    private final FeatureRendererContext<RookEntity, RookModel> context;

    public RookEyeFeatureRenderer(FeatureRendererContext<RookEntity, RookModel> featureRendererContext) {
        super(featureRendererContext);
        this.context = featureRendererContext;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, RookEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getEyesTexture(entity));
        this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, getContextModel().lookAlpha);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return EYES.get(0);
    }

    public RenderLayer getEyesTexture(RookEntity entity) {
        return EYES.get(entity.getAscencion());
    }

    static {
        EYES = ImmutableList.of(
                RenderLayer.getEyes(Aether.locate("textures/entity/corvid/rook_eye1.png")),
                RenderLayer.getEyes(Aether.locate("textures/entity/corvid/rook_eye2.png")),
                RenderLayer.getEyes(Aether.locate("textures/entity/corvid/rook_eye3.png")),
                RenderLayer.getEyes(Aether.locate("textures/entity/corvid/rook_eye4.png"))
        );
    }
}
