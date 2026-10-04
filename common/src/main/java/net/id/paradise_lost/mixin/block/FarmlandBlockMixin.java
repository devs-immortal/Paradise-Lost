package net.id.paradise_lost.mixin.block;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.pushEntitiesUp;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin {
    @Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
    private static void onSetToDirt(@Nullable Entity entity, BlockState state, Level world, BlockPos pos, CallbackInfo ci) {
        if (state.is(BlockRegistry.FARMLAND.get())) {
            BlockState blockState = pushEntitiesUp(state, BlockRegistry.DIRT.get().defaultBlockState(), world, pos);
            world.setBlockAndUpdate(pos, blockState);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockState));
            ci.cancel();
        }
    }
}
