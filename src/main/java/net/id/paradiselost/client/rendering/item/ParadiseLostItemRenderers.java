package net.id.paradiselost.client.rendering.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.mixin.block.ChestBlockEntityAccessor;
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
        registerChest(ParadiseLostBlocks.CRYSTAL_CHEST, ParadiseLostBlockEntityTypes.CRYSTAL_CHEST);
        registerChest(ParadiseLostBlocks.GOLDEN_OAK_CHEST, ParadiseLostBlockEntityTypes.GOLDEN_OAK_CHEST);
        registerChest(ParadiseLostBlocks.ORANGE_CHEST, ParadiseLostBlockEntityTypes.ORANGE_CHEST);
        registerChest(ParadiseLostBlocks.SKYROOT_CHEST, ParadiseLostBlockEntityTypes.SKYROOT_CHEST);
        registerChest(ParadiseLostBlocks.WISTERIA_CHEST, ParadiseLostBlockEntityTypes.WISTERIA_CHEST);
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
