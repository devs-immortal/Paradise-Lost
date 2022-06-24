package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.client.rendering.texture.ParadiseLostChestTexture;

@Environment(EnvType.CLIENT)
public class ParadiseLostBlockEntityRenderers {

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.INCUBATOR, IncubatorBlockEntityRenderer::new);
//        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.DUNGEON_SWITCH, DungeonSwitchBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.SKYROOT_CHEST, ctx -> new ParadiseLostChestBlockEntityRenderer(ctx, ParadiseLostChestTexture.SKYROOT));
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.GOLDEN_OAK_CHEST, ctx -> new ParadiseLostChestBlockEntityRenderer(ctx, ParadiseLostChestTexture.GOLDEN_OAK));
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.ORANGE_CHEST, ctx -> new ParadiseLostChestBlockEntityRenderer(ctx, ParadiseLostChestTexture.ORANGE));
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.CRYSTAL_CHEST, ctx -> new ParadiseLostChestBlockEntityRenderer(ctx, ParadiseLostChestTexture.CRYSTAL));
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.WISTERIA_CHEST, ctx -> new ParadiseLostChestBlockEntityRenderer(ctx, ParadiseLostChestTexture.WISTERIA));
    }
}
