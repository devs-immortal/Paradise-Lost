package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.entities.projectile.DartEntity;
import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonNeedleEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DartRenderer extends EntityRenderer<DartEntity> {

    public DartRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.shadowStrength = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(DartEntity entity) {
        String base = entity instanceof GoldenDartEntity ? "golden" : entity instanceof EnchantedDartEntity ? "enchanted" : "poison";

        return Aether.locate("textures/entity/projectile/dart/" + base + (entity instanceof PoisonNeedleEntity ? "_needle" : "_dart") + ".png");
    }
}
