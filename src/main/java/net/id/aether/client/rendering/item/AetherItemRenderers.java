package net.id.aether.client.rendering.item;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.items.AetherItems;
import net.id.aether.mixin.block.ChestBlockEntityAccessor;
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
        registerChest(AetherBlocks.CRYSTAL_CHEST, AetherBlockEntityTypes.CRYSTAL_CHEST);
        registerChest(AetherBlocks.GOLDEN_OAK_CHEST, AetherBlockEntityTypes.GOLDEN_OAK_CHEST);
        registerChest(AetherBlocks.ORANGE_CHEST, AetherBlockEntityTypes.ORANGE_CHEST);
        registerChest(AetherBlocks.SKYROOT_CHEST, AetherBlockEntityTypes.SKYROOT_CHEST);
        registerChest(AetherBlocks.WISTERIA_CHEST, AetherBlockEntityTypes.WISTERIA_CHEST);
        TrinketRendererRegistry.registerRenderer(AetherItems.CLOUD_PARACHUTE, (TrinketRenderer) AetherItems.CLOUD_PARACHUTE);
        TrinketRendererRegistry.registerRenderer(AetherItems.GOLDEN_CLOUD_PARACHUTE, (TrinketRenderer) AetherItems.GOLDEN_CLOUD_PARACHUTE);
    }

    private static void registerChest(ChestBlock block, BlockEntityType<ChestBlockEntity> type){
        register(block, ChestBlockEntityAccessor.init(type, BlockPos.ORIGIN, block.getDefaultState()));
    }
    
    private static void register(ItemConvertible item, BlockEntity entity){
        BuiltinItemRendererRegistry.INSTANCE.register(item, (stack, mode, matrices, vertexConsumers, light, overlay)->{
            var dispatcher = MinecraftClient.getInstance().getBlockEntityRenderDispatcher();
            dispatcher.renderEntity(entity, matrices, vertexConsumers, light, overlay);
        });
    }
}
