package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.ParadiseHareModel;
import net.id.paradiselost.entities.passive.ParadiseHareEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ParadiseHareRenderer extends MobEntityRenderer<ParadiseHareEntity, ParadiseHareModel> {

    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/corsican_hare.png");

    public ParadiseHareRenderer(EntityRendererFactory.Context context) {
        super(context, new ParadiseHareModel(context.getPart(ParadiseLostModelLayers.PARADISE_HARE)), 0.3F);
    }

    @Override
    public ParadiseHareModel getModel() {
        return super.getModel();
    }

    @Override
    protected void setupTransforms(ParadiseHareEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        if (entity.isBaby()) {
            matrices.scale(0.6F, 0.6F, 0.6F);
        }
    }

    @Override
    public Identifier getTexture(ParadiseHareEntity entity) {
        return TEXTURE;
    }
}
