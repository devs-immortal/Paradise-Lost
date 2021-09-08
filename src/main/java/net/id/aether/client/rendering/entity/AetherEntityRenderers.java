package net.id.aether.client.rendering.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class AetherEntityRenderers {
    public static void initClient() {
        register(AetherEntityTypes.MOA, MoaRenderer::new);
        register(AetherEntityTypes.FLOATING_BLOCK, FloatingBlockRenderer::new);
        //register(AetherEntityTypes.FLYING_COW, FlyingCowRenderer::new);
        //register(AetherEntityTypes.SHEEPUFF, SheepuffRenderer::new);
        register(AetherEntityTypes.AERBUNNY, AerbunnyRenderer::new);
        register(AetherEntityTypes.AECHOR_PLANT, AechorPlantRenderer::new);
        register(AetherEntityTypes.PHYG, PhygRenderer::new);
        register(AetherEntityTypes.COCKATRICE, CockatriceRenderer::new);
        register(AetherEntityTypes.COCKATRICE_SPIT, CockatriceSpitRenderer::new);

        register(DartRenderer::new,
            AetherEntityTypes.ENCHANTED_DART,
            AetherEntityTypes.GOLDEN_DART,
            AetherEntityTypes.POISON_DART,
            AetherEntityTypes.POISON_NEEDLE
        );
        register(AetherEntityTypes.AERWHALE, AerwhaleRenderer::new);


        //entityRenderMap.put(EntityMiniCloud.class, new MiniCloudRenderer(renderManager));
        register(AetherEntityTypes.CHEST_MIMIC, ChestMimicRenderer::new);
        //entityRenderMap.put(EntityWhirlwind.class, new WhirlwindRenderer(renderManager));
        //entityRenderMap.put(EntityPhoenixArrow.class, new PhoenixArrowRenderer(renderManager));
        register(SwetRenderer::new,
            AetherEntityTypes.BLUE_SWET,
            AetherEntityTypes.PURPLE_SWET,
            AetherEntityTypes.WHITE_SWET,
            AetherEntityTypes.GOLDEN_SWET
        );
    }
    
    @SafeVarargs
    private static <T extends Entity> void register(EntityRendererFactory<T> factory, EntityType<? extends T>... types) {
        for(var type : types){
            register(type, factory);
        }
    }
    
    private static <T extends Entity> void register(EntityType<? extends T> clazz, EntityRendererFactory<T> factory) {
        EntityRendererRegistry.register(clazz, factory);
    }
}
