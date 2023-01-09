package net.id.paradiselost.client.rendering.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.projectile.DartEntity;
import net.id.paradiselost.entities.projectile.PoisonNeedleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DartRenderer extends ProjectileEntityRenderer<DartEntity> {

    public DartRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        this.shadowOpacity = 0.0F;
    }

    @Override
    public Identifier getTexture(DartEntity entity) {
        return ParadiseLost.locate("textures/entity/projectile/dart/poison" + (entity instanceof PoisonNeedleEntity ? "_needle" : "_dart") + ".png");
    }
}
