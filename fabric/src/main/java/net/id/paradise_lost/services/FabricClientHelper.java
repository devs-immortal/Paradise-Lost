package net.id.paradise_lost.services;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.EnvType;
import net.id.paradise_lost.platform.services.IClientHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class FabricClientHelper implements IClientHelper {
    @Override
    public void setRenderLayerCutout(Object block) {
        if (isPhysicalClient() && block instanceof Block b) {
            BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.cutout());
        }
    }

    @Override
    public void setRenderLayerCutoutMipped(Object block) {
        if (isPhysicalClient() && block instanceof Block b) {
            BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.cutoutMipped());
        }
    }

    @Override
    public void setRenderLayerTranslucent(Object block) {
        if (isPhysicalClient() && block instanceof Block b) {
            BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.translucent());
        }
    }

    private static boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
