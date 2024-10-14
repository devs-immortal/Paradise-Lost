package net.id.paradiselost.mixin.block;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.Block.pushEntitiesUpBeforeBlockChange;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {
    @Inject(method = "setToDirt", at = @At("HEAD"), cancellable = true)
    private static void onSetToDirt(@Nullable Entity entity, BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (state.isOf(ParadiseLostBlocks.FARMLAND)) {
            BlockState blockState = pushEntitiesUpBeforeBlockChange(state, ParadiseLostBlocks.DIRT.getDefaultState(), world, pos);
            world.setBlockState(pos, blockState);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, blockState));
            ci.cancel();
        }
    }
}
