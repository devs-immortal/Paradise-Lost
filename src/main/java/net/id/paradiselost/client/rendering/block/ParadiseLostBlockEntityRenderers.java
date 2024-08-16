package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class ParadiseLostBlockEntityRenderers {

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        BlockEntityRendererFactories.register(ParadiseLostBlockEntityTypes.INCUBATOR, IncubatorBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, CherineCampfireBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ParadiseLostBlockEntityTypes.TREE_TAP, TreeTapBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ParadiseLostBlockEntityTypes.SIGN, SignBlockEntityRenderer::new);
		// BlockEntityRendererFactories.register(ParadiseLostBlockEntityTypes.DUNGEON_SWITCH, DungeonSwitchBlockEntityRenderer::new);
    }
}
