package net.id.paradise_lost.services;

import net.id.paradise_lost.platform.services.ICompatHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

import java.util.function.Supplier;

public class NeoForgeCompatHelper implements ICompatHelper {
    @Override
    public void addValidBlock(BlockEntityType<?> type, Supplier<Block> block) {
        NeoForgePlatformHelper.modBus().addListener((BlockEntityTypeAddBlocksEvent event) ->
                event.modify(type, block.get()));
    }
}
