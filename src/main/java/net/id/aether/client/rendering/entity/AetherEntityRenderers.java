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
        // block
        register(AetherEntityTypes.FLOATING_BLOCK, FloatingBlockRenderer::new);
        // hostile
        register(SwetRenderer::new,
                AetherEntityTypes.BLUE_SWET,
                AetherEntityTypes.PURPLE_SWET,
                AetherEntityTypes.WHITE_SWET,
                AetherEntityTypes.GOLDEN_SWET
        );
        register(AetherEntityTypes.AECHOR_PLANT, AechorPlantRenderer::new);
        register(AetherEntityTypes.CHEST_MIMIC, ChestMimicRenderer::new);
        register(AetherEntityTypes.COCKATRICE, CockatriceRenderer::new);
        // passive
        register(AetherEntityTypes.MOA, MoaRenderer::new);
        register(AetherEntityTypes.AERBUNNY, AerbunnyRenderer::new);
        register(AetherEntityTypes.AERWHALE, AerwhaleRenderer::new);
//        register(AetherEntityTypes.FLYING_COW, FlyingCowRenderer::new);
//        register(AetherEntityTypes.PHYG, PhygRenderer::new);
//        register(AetherEntityTypes.SHEEPUFF, SheepuffRenderer::new);
        // projectile
        register(AetherEntityTypes.COCKATRICE_SPIT, CockatriceSpitRenderer::new);
        register(DartRenderer::new,
                AetherEntityTypes.ENCHANTED_DART,
                AetherEntityTypes.GOLDEN_DART,
                AetherEntityTypes.POISON_DART,
                AetherEntityTypes.POISON_NEEDLE
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
