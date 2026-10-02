package net.id.paradiselost.client.rendering.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.rendering.entity.hostile.EnvoyEntityRenderer;
import net.id.paradiselost.client.rendering.entity.hostile.SentinelEntityRenderer;
import net.id.paradiselost.client.rendering.entity.hostile.QuintEntityRenderer;
import net.id.paradiselost.client.rendering.entity.passive.MoaEntityRenderer;
import net.id.paradiselost.client.rendering.entity.passive.PopomEntityRenderer;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;

@Environment(EnvType.CLIENT)
public class ParadiseLostEntityRenderers {
    public static void initClient() {
        // block
        register(ParadiseLostEntityTypes.FLOATING_BLOCK, BlockLikeEntityRenderer::new);
        register(ParadiseLostEntityTypes.SLIDER, BlockLikeEntityRenderer::new);

        // hostile
        register(ParadiseLostEntityTypes.ENVOY, EnvoyEntityRenderer::new);
        register(ParadiseLostEntityTypes.SENTINEL, SentinelEntityRenderer::new);

        // passive
        register(ParadiseLostEntityTypes.MOA, MoaEntityRenderer::new);
        register(ParadiseLostEntityTypes.POPOM, PopomEntityRenderer::new);

        register(ParadiseLostEntityTypes.QUINT, QuintEntityRenderer::new);

        // projectile
        register(ParadiseLostEntityTypes.THROWN_NITRA, FlyingItemEntityRenderer::new);

        // boat
        registerBoat(ParadiseLostEntityTypes.AUREL_BOAT, ParadiseLostModelLayers.AUREL_BOAT);
        registerBoat(ParadiseLostEntityTypes.AUREL_CHEST_BOAT, ParadiseLostModelLayers.AUREL_CHEST_BOAT);
        registerBoat(ParadiseLostEntityTypes.MOTHER_AUREL_BOAT, ParadiseLostModelLayers.MOTHER_AUREL_BOAT);
        registerBoat(ParadiseLostEntityTypes.MOTHER_AUREL_CHEST_BOAT, ParadiseLostModelLayers.MOTHER_AUREL_CHEST_BOAT);
        registerBoat(ParadiseLostEntityTypes.MENTH_BOAT, ParadiseLostModelLayers.MENTH_BOAT);
        registerBoat(ParadiseLostEntityTypes.MENTH_CHEST_BOAT, ParadiseLostModelLayers.MENTH_CHEST_BOAT);
        registerBoat(ParadiseLostEntityTypes.WISTERIA_BOAT, ParadiseLostModelLayers.WISTERIA_BOAT);
        registerBoat(ParadiseLostEntityTypes.WISTERIA_CHEST_BOAT, ParadiseLostModelLayers.WISTERIA_CHEST_BOAT);
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

    private static void registerBoat(EntityType<? extends AbstractBoatEntity> type, EntityModelLayer layer) {
        register(type, context -> new BoatEntityRenderer(context, layer));
    }
}
