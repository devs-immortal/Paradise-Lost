package net.id.paradise_lost.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {
    @WrapOperation(
            method = "fallOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V")
    )
    private void paradiseLost$floatyBootsSpareFarmland(@Nullable Entity entity, BlockState state, Level level, BlockPos pos, Operation<Void> trample) {
        if (!(entity instanceof LivingEntity living && FloatyBootsItem.isWearing(living))) {
            trample.call(entity, state, level, pos);
        }
    }
}
