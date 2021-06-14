package com.aether.client.rendering.entity;

import com.aether.entities.AetherEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class AetherEntityRenderers {
    public static void initClient() {
        //register(AetherEntityTypes.MOA, MoaRenderer::new);
        register(AetherEntityTypes.FLOATING_BLOCK, FloatingBlockRenderer::new);
        //register(AetherEntityTypes.FLYING_COW, FlyingCowRenderer::new);
        //register(AetherEntityTypes.SHEEPUFF, SheepuffRenderer::new);
        //register(AetherEntityTypes.AERBUNNY, AerbunnyRenderer::new);
        //register(AetherEntityTypes.AECHOR_PLANT, AechorPlantRenderer::new);
        register(AetherEntityTypes.PHYG, PhygRenderer::new);
        //register(AetherEntityTypes.COCKATRICE, CockatriceRenderer::new);

        register(AetherEntityTypes.ENCHANTED_DART, DartRenderer::new);
        register(AetherEntityTypes.GOLDEN_DART, DartRenderer::new);
        register(AetherEntityTypes.POISON_DART, DartRenderer::new);
        register(AetherEntityTypes.POISON_NEEDLE, DartRenderer::new);
        //register(AetherEntityTypes.AERWHALE, AerwhaleRenderer::new);


        //entityRenderMap.put(EntityMiniCloud.class, new MiniCloudRenderer(renderManager));
        //entityRenderMap.put(EntityAerwhale.class, new AerwhaleRenderer(renderManager));
       //register(AetherEntityTypes.CHEST_MIMIC, ChestMimicRenderer::new);
        //entityRenderMap.put(EntityWhirlwind.class, new WhirlwindRenderer(renderManager));
        //entityRenderMap.put(EntityPhoenixArrow.class, new PhoenixArrowRenderer(renderManager));
        register(AetherEntityTypes.BLUE_SWET, SwetRenderer::new);
        register(AetherEntityTypes.PURPLE_SWET, SwetRenderer::new);
        register(AetherEntityTypes.WHITE_SWET, SwetRenderer::new);
        register(AetherEntityTypes.GOLDEN_SWET, SwetRenderer::new);
    }

    private static void register(EntityType<? extends Entity> clazz, EntityRendererFactory factory) {
        EntityRendererRegistry.INSTANCE.register(clazz, factory);
    }
}
