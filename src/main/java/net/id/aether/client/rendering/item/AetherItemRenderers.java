package net.id.aether.client.rendering.item;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.items.AetherItems;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public final class AetherItemRenderers{
    private AetherItemRenderers(){}
    
    public static void initClient(){
        TrinketRendererRegistry.registerRenderer(AetherItems.CLOUD_PARACHUTE, (TrinketRenderer) AetherItems.CLOUD_PARACHUTE);
        TrinketRendererRegistry.registerRenderer(AetherItems.GOLDEN_CLOUD_PARACHUTE, (TrinketRenderer) AetherItems.GOLDEN_CLOUD_PARACHUTE);
    }
}
