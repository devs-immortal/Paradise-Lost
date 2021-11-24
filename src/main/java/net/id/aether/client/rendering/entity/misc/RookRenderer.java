package net.id.aether.client.rendering.entity.misc;

import com.google.common.collect.ImmutableList;
import net.id.aether.Aether;
import net.id.aether.client.model.AetherModelLayers;
import net.id.aether.client.model.entity.RookModel;
import net.id.aether.entities.misc.RookEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RookRenderer extends MobEntityRenderer<RookEntity, RookModel> {

    public static final List<Identifier> TEXTURES;

    public RookRenderer(EntityRendererFactory.Context context) {
        super(context, new RookModel(context.getPart(AetherModelLayers.ROOK)), 0);
        //addFeature(new RookEyeFeatureRenderer(this));
    }

    @Override
    public void render(RookEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(RookEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return RenderLayer.getEntityTranslucent(getTexture(entity));
    }

    @Override
    public Identifier getTexture(RookEntity entity) {
        return TEXTURES.get(entity.getAscencion());
    }

    static {
        TEXTURES = ImmutableList.of(
                Aether.locate("textures/entity/corvid/rook1.png"),
                Aether.locate("textures/entity/corvid/rook2.png"),
                Aether.locate("textures/entity/corvid/rook3.png"),
                Aether.locate("textures/entity/corvid/rook4.png")
        );
    }
}
