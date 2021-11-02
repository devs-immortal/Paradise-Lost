package net.id.aether.blocks.blockentity;

import java.util.function.Supplier;
import net.id.aether.mixin.block.ChestBlockEntityAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;

public class AetherChestBlock extends ChestBlock{
    public AetherChestBlock(AbstractBlock.Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> typeSupplier){
        super(settings, typeSupplier);
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state){
        return ChestBlockEntityAccessor.init(entityTypeRetriever.get(), pos, state);
    }
}
