package net.id.paradiselost.client.rendering.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.id.incubus_core.blocklikeentities.api.client.BlockLikeEntityRenderer;
import net.id.paradiselost.client.rendering.entity.hostile.HellenroseRenderer;
import net.id.paradiselost.client.rendering.entity.misc.RookRenderer;
import net.id.paradiselost.client.rendering.entity.passive.ParadiseHareRenderer;
import net.id.paradiselost.client.rendering.entity.passive.MoaEntityRenderer;
import net.id.paradiselost.client.rendering.entity.projectile.DartRenderer;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class ParadiseLostEntityRenderers {
    public static void initClient() {
        // block
        register(ParadiseLostEntityTypes.FLOATING_BLOCK, BlockLikeEntityRenderer::new);
        register(ParadiseLostEntityTypes.SLIDER, BlockLikeEntityRenderer::new);

        // hostile
        register(ParadiseLostEntityTypes.HELLENROSE, HellenroseRenderer::new);

        // passive
        register(ParadiseLostEntityTypes.MOA, MoaEntityRenderer::new);
        register(ParadiseLostEntityTypes.PARADISE_HARE, ParadiseHareRenderer::new);
//        register(ParadiseLostEntityTypes.AMBYST, AmbystRenderer::new);
        // projectile
        register(DartRenderer::new,
                ParadiseLostEntityTypes.POISON_DART,
                ParadiseLostEntityTypes.POISON_NEEDLE
        );

        // other
        register(ParadiseLostEntityTypes.ROOK, RookRenderer::new);
    }
    
    @SafeVarargs
    private static <T extends Entity> void register(EntityRendererFactory<T> factory, EntityType<? extends T>... types) {
        for (var type : types) {
            register(type, factory);
        }
    }
    
    private static <T extends Entity> void register(EntityType<? extends T> clazz, EntityRendererFactory<T> factory) {
        EntityRendererRegistry.register(clazz, factory);
    }
}
