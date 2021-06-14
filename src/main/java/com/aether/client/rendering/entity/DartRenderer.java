package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.entities.projectile.DartEntity;
import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonNeedleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class DartRenderer extends ProjectileEntityRenderer<DartEntity> {

    public DartRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        this.shadowOpacity = 0.0F;
    }

    @Override
    public Identifier getTexture(DartEntity entity) {
        String base = entity instanceof GoldenDartEntity ? "golden" : entity instanceof EnchantedDartEntity ? "enchanted" : "poison";

        return Aether.locate("textures/entity/projectile/dart/" + base + (entity instanceof PoisonNeedleEntity ? "_needle" : "_dart") + ".png");
    }
}
