package net.id.aether.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.client.rendering.texture.AetherChestTexture;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.CampfireBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class AetherBlockEntityRenderers {

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.INCUBATOR, IncubatorBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.AMBROSIUM_CAMPFIRE, AmbrosiumCampfireBlockEntityRenderer::new);
//        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.DUNGEON_SWITCH, DungeonSwitchBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.SKYROOT_CHEST, ctx -> new AetherChestBlockEntityRenderer(ctx, AetherChestTexture.SKYROOT));
        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.GOLDEN_OAK_CHEST, ctx -> new AetherChestBlockEntityRenderer(ctx, AetherChestTexture.GOLDEN_OAK));
        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.ORANGE_CHEST, ctx -> new AetherChestBlockEntityRenderer(ctx, AetherChestTexture.ORANGE));
        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.CRYSTAL_CHEST, ctx -> new AetherChestBlockEntityRenderer(ctx, AetherChestTexture.CRYSTAL));
        BlockEntityRendererRegistry.register(AetherBlockEntityTypes.WISTERIA_CHEST, ctx -> new AetherChestBlockEntityRenderer(ctx, AetherChestTexture.WISTERIA));
    }
}
