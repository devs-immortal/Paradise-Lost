package com.aether.client.rendering.entity;

import com.aether.entities.AetherEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class AetherEntityRenderers {
    public static void initClient() {
        register(AetherEntityTypes.MOA, (entityRenderDispatcher, context) -> new MoaRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.FLOATING_BLOCK, (entityRenderDispatcher, context) -> new FloatingBlockRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.FLYING_COW, (entityRenderDispatcher, context) -> new FlyingCowRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.SHEEPUFF, (entityRenderDispatcher, context) -> new SheepuffRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.AERBUNNY, (entityRenderDispatcher, context) -> new AerbunnyRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.AECHOR_PLANT, (entityRenderDispatcher, context) -> new AechorPlantRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.PHYG, (entityRenderDispatcher, context) -> new PhygRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.COCKATRICE, (entityRenderDispatcher, context) -> new CockatriceRenderer(entityRenderDispatcher));

        register(AetherEntityTypes.ENCHANTED_DART, (entityRenderDispatcher, context) -> new DartRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.GOLDEN_DART, (entityRenderDispatcher, context) -> new DartRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.POISON_DART, (entityRenderDispatcher, context) -> new DartRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.POISON_NEEDLE, (entityRenderDispatcher, context) -> new DartRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.AERWHALE, (entityRenderDispatcher, context) -> new AerwhaleRenderer(entityRenderDispatcher));


        //entityRenderMap.put(EntityMiniCloud.class, new MiniCloudRenderer(renderManager));
        //entityRenderMap.put(EntityAerwhale.class, new AerwhaleRenderer(renderManager));
        register(AetherEntityTypes.CHEST_MIMIC, (entityRendererDispatcher, context) -> new ChestMimicRenderer(entityRendererDispatcher));
        //entityRenderMap.put(EntityWhirlwind.class, new WhirlwindRenderer(renderManager));
        //entityRenderMap.put(EntityPhoenixArrow.class, new PhoenixArrowRenderer(renderManager));
        register(AetherEntityTypes.BLUE_SWET, (entityRenderDispatcher, context) -> new SwetRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.PURPLE_SWET, (entityRenderDispatcher, context) -> new SwetRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.WHITE_SWET, (entityRenderDispatcher, context) -> new SwetRenderer(entityRenderDispatcher));
        register(AetherEntityTypes.GOLDEN_SWET, (entityRenderDispatcher, context) -> new SwetRenderer(entityRenderDispatcher));
    }

    private static void register(EntityType<? extends Entity> clazz, EntityRendererRegistry.Factory factory) {
        EntityRendererRegistry.INSTANCE.register(clazz, factory);
    }
}
