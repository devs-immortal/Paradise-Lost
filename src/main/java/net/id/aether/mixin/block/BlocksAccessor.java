package net.id.aether.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Blocks.class)
public interface BlocksAccessor {
    @Invoker
    static Boolean callCanSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        throw new AssertionError();
    }
}
