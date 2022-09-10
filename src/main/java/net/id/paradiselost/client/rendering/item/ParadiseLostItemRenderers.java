package net.id.paradiselost.client.rendering.item;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public final class ParadiseLostItemRenderers {
    private ParadiseLostItemRenderers(){}
    
    public static void initClient(){
        TrinketRendererRegistry.registerRenderer(ParadiseLostItems.CLOUD_PARACHUTE, (TrinketRenderer) ParadiseLostItems.CLOUD_PARACHUTE);
        TrinketRendererRegistry.registerRenderer(ParadiseLostItems.GOLDEN_CLOUD_PARACHUTE, (TrinketRenderer) ParadiseLostItems.GOLDEN_CLOUD_PARACHUTE);
    }
}
