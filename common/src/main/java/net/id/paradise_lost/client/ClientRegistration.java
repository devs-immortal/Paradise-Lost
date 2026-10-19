package net.id.paradise_lost.client;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;

import net.minecraft.world.entity.vehicle.Boat;

import net.id.paradise_lost.entity.vehicle.ParadiseLostBoatType;
import net.id.paradise_lost.client.rendering.entity.ParadiseLostBoatRenderer;

import net.id.paradise_lost.block.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradise_lost.client.rendering.block.CalciteDecoratedPotBlockEntityRenderer;
import net.id.paradise_lost.client.rendering.block.CherineCampfireBlockEntityRenderer;
import net.id.paradise_lost.client.rendering.block.IncubatorBlockEntityRenderer;
import net.id.paradise_lost.client.rendering.block.TreeTapBlockEntityRenderer;
import net.id.paradise_lost.client.rendering.entity.BlockLikeEntityRenderer;
import net.id.paradise_lost.client.rendering.entity.LevitaArrowRenderer;
import net.id.paradise_lost.client.rendering.entity.hostile.EnvoyEntityRenderer;
import net.id.paradise_lost.client.rendering.entity.hostile.QuintEntityRenderer;
import net.id.paradise_lost.client.rendering.entity.hostile.SentinelEntityRenderer;
import net.id.paradise_lost.client.rendering.entity.passive.MoaEntityRenderer;
import net.id.paradise_lost.client.rendering.entity.passive.PopomEntityRenderer;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.block.blockentity.PalaceDoorBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ClientRegistration {
    private ClientRegistration() {}

    @FunctionalInterface
    public interface EntityRendererRegistrar {
        <E extends Entity> void register(EntityType<? extends E> type, EntityRendererProvider<E> provider);
    }

    @FunctionalInterface
    public interface BlockEntityRendererRegistrar {
        <E extends BlockEntity> void register(BlockEntityType<? extends E> type, BlockEntityRendererProvider<E> provider);
    }

    public static void registerRenderers(
            EntityRendererRegistrar entities,
            BlockEntityRendererRegistrar blockEntities,
            BlockEntityRendererProvider<PalaceDoorBlockEntity> palaceDoorRenderer
    ) {
        entities.register(EntityRegistry.FLOATING_BLOCK.get(), BlockLikeEntityRenderer::new);
        entities.register(EntityRegistry.SLIDER.get(), BlockLikeEntityRenderer::new);
        entities.register(EntityRegistry.ENVOY.get(), EnvoyEntityRenderer::new);
        entities.register(EntityRegistry.SENTINEL.get(), SentinelEntityRenderer::new);
        entities.register(EntityRegistry.MOA.get(), MoaEntityRenderer::new);
        entities.register(EntityRegistry.POPOM.get(), PopomEntityRenderer::new);
        entities.register(EntityRegistry.QUINT.get(), QuintEntityRenderer::new);
        entities.register(EntityRegistry.THROWN_NITRA.get(), ThrownItemRenderer::new);
        entities.register(EntityRegistry.LEVITA_ARROW.get(), LevitaArrowRenderer::new);
        for (ParadiseLostBoatType wood : ParadiseLostBoatType.values()) {
            registerBoat(entities, EntityRegistry.boatType(wood), false);
            registerBoat(entities, EntityRegistry.chestBoatType(wood), true);
        }

        blockEntities.register(ParadiseLostBlockEntityTypes.INCUBATOR.get(), IncubatorBlockEntityRenderer::new);
        blockEntities.register(ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), CherineCampfireBlockEntityRenderer::new);
        blockEntities.register(ParadiseLostBlockEntityTypes.TREE_TAP.get(), TreeTapBlockEntityRenderer::new);
        blockEntities.register(ParadiseLostBlockEntityTypes.SIGN.get(), SignRenderer::new);
        blockEntities.register(ParadiseLostBlockEntityTypes.HANGING_SIGN.get(), HangingSignRenderer::new);
        blockEntities.register(ParadiseLostBlockEntityTypes.CALCITE_DECORATED_POT.get(), CalciteDecoratedPotBlockEntityRenderer::new);
        blockEntities.register(ParadiseLostBlockEntityTypes.PALACE_DOOR.get(), palaceDoorRenderer);
    }

    public static void registerItemProperties() {
        ItemProperties.register(ItemRegistry.XP_CIRCLET.get(), ModConstants.id("charged"),
                (stack, level, entity, seed) -> XpCircletItem.isCharged(stack) ? 1.0F : 0.0F);
        ItemProperties.register(ItemRegistry.FLOATY_LEGGINGS.get(), ModConstants.id("broken"),
                (stack, level, entity, seed) -> FloatyLeggingsItem.isFloatyEnabled(stack) ? 0.0F : 1.0F);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerBoat(EntityRendererRegistrar entities, EntityType<?> type, boolean chestBoat) {

        EntityRendererProvider<Boat> provider = context -> new ParadiseLostBoatRenderer(context, chestBoat);
        entities.register((EntityType) type, (EntityRendererProvider) provider);
    }
}
