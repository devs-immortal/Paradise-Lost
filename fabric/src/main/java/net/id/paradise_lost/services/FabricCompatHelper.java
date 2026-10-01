package net.id.paradise_lost.services;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.id.paradise_lost.platform.services.ICompatHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricCompatHelper implements ICompatHelper {
    @Override
    public void addValidBlock(BlockEntityType<?> type, Supplier<Block> block) {
        ((FabricBlockEntityType) type).addSupportedBlock(block.get());
    }
}
