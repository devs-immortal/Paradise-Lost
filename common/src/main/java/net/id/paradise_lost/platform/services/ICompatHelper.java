package net.id.paradise_lost.platform.services;

import net.id.paradise_lost.compat.ParadiseLostEveryCompat;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface ICompatHelper {

    void addValidBlock(BlockEntityType<?> type, Supplier<Block> block);

    default void initCompat() {
        addValidBlock(BlockEntityType.BRUSHABLE_BLOCK, () -> BlockRegistry.SUSPICIOUS_DIRT.get());
        if (Services.PLATFORM.isModLoaded("moonlight")) {
            ParadiseLostEveryCompat.registerWoodAndLeaves();
        }
        if (Services.PLATFORM.isModLoaded("stonezone")) {
            ParadiseLostEveryCompat.registerStoneTypes();
        }
    }
}
