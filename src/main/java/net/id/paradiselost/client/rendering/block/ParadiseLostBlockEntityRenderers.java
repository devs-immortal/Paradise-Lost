package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;

@Environment(EnvType.CLIENT)
public class ParadiseLostBlockEntityRenderers {

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.INCUBATOR, IncubatorBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.AMBROSIUM_CAMPFIRE, AmbrosiumCampfireBlockEntityRenderer::new);
//        BlockEntityRendererRegistry.register(ParadiseLostBlockEntityTypes.DUNGEON_SWITCH, DungeonSwitchBlockEntityRenderer::new);
    }
}
