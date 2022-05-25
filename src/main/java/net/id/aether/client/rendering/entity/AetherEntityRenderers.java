package net.id.aether.client.rendering.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.id.aether.client.rendering.entity.hostile.AechorPlantRenderer;
import net.id.aether.client.rendering.entity.hostile.CockatriceRenderer;
import net.id.aether.client.rendering.entity.hostile.SwetRenderer;
import net.id.aether.client.rendering.entity.misc.RookRenderer;
import net.id.aether.client.rendering.entity.passive.AerbunnyRenderer;
import net.id.aether.client.rendering.entity.passive.AerwhaleRenderer;
import net.id.aether.client.rendering.entity.passive.MoaEntityRenderer;
import net.id.aether.client.rendering.entity.projectile.CockatriceSpitRenderer;
import net.id.aether.client.rendering.entity.projectile.DartRenderer;
import net.id.aether.entities.AetherEntityTypes;
import net.id.incubus_core.blocklikeentities.api.client.BlockLikeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class AetherEntityRenderers {
    public static void initClient() {
        // block
        register(AetherEntityTypes.FLOATING_BLOCK, BlockLikeEntityRenderer::new);
        register(AetherEntityTypes.SLIDER, BlockLikeEntityRenderer::new);

        // hostile
        register(SwetRenderer::new,
                AetherEntityTypes.BLUE_SWET,
                AetherEntityTypes.PURPLE_SWET,
                AetherEntityTypes.WHITE_SWET,
                AetherEntityTypes.GOLDEN_SWET,
                AetherEntityTypes.VERMILION_SWET
        );
        register(AetherEntityTypes.AECHOR_PLANT, AechorPlantRenderer::new);
        //register(AetherEntityTypes.CHEST_MIMIC, ChestMimicRenderer::new);
        register(AetherEntityTypes.COCKATRICE, CockatriceRenderer::new);

        // passive
        register(AetherEntityTypes.MOA, MoaEntityRenderer::new);
        register(AetherEntityTypes.AERBUNNY, AerbunnyRenderer::new);
        register(AetherEntityTypes.AERWHALE, AerwhaleRenderer::new);

        // projectile
        register(AetherEntityTypes.COCKATRICE_SPIT, CockatriceSpitRenderer::new);
        register(DartRenderer::new,
                AetherEntityTypes.ENCHANTED_DART,
                AetherEntityTypes.GOLDEN_DART,
                AetherEntityTypes.POISON_DART,
                AetherEntityTypes.POISON_NEEDLE
        );

        // other
        register(AetherEntityTypes.ROOK, RookRenderer::new);
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
