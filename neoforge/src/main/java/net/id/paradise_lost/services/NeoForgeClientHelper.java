package net.id.paradise_lost.services;

import net.id.paradise_lost.platform.services.IClientHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NeoForgeClientHelper implements IClientHelper {
    private static final List<Runnable> CLIENT_RENDER_LAYERS = new ArrayList<>();

    @Override
    public void setRenderLayerCutout(Object block) {
        deferRenderLayer(block, RenderType.cutout());
    }

    @Override
    public void setRenderLayerCutoutMipped(Object block) {
        deferRenderLayer(block, RenderType.cutoutMipped());
    }

    @Override
    public void setRenderLayerTranslucent(Object block) {
        deferRenderLayer(block, RenderType.translucent());
    }

    private void deferRenderLayer(Object block, RenderType type) {
        if (!(block instanceof Block b) || FMLEnvironment.dist != Dist.CLIENT) {
            return;
        }
        CLIENT_RENDER_LAYERS.add(() -> {
            ItemBlockRenderTypes.setRenderLayer(b, type);
            putMovingBlockRenderType(b, type);
        });
    }

    @SuppressWarnings("unchecked")
    private static void putMovingBlockRenderType(Block block, RenderType type) {
        try {
            var field = ItemBlockRenderTypes.class.getDeclaredField("TYPE_BY_BLOCK");
            field.setAccessible(true);
            ((Map<Block, RenderType>) field.get(null)).put(block, type);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set moving-block render type for " + block, e);
        }
    }

    public static void applyDeferredRenderLayers() {
        CLIENT_RENDER_LAYERS.forEach(Runnable::run);
        CLIENT_RENDER_LAYERS.clear();
    }
}
