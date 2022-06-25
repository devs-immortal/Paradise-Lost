package net.id.paradiselost.client.rendering.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.projectile.DartEntity;
import net.id.paradiselost.entities.projectile.EnchantedDartEntity;
import net.id.paradiselost.entities.projectile.GoldenDartEntity;
import net.id.paradiselost.entities.projectile.PoisonNeedleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DartRenderer extends ProjectileEntityRenderer<DartEntity> {

    public DartRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        shadowOpacity = 0.0F;
    }

    @Override
    public Identifier getTexture(DartEntity entity) {
        String base = entity instanceof GoldenDartEntity ? "golden" : entity instanceof EnchantedDartEntity ? "enchanted" : "poison";

        return ParadiseLost.locate("textures/entity/projectile/dart/" + base + (entity instanceof PoisonNeedleEntity ? "_needle" : "_dart") + ".png");
    }
}
