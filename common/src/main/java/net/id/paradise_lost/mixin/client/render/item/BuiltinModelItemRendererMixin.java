package net.id.paradise_lost.mixin.client.render.item;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.id.paradise_lost.block.decorative.CalciteDecoratedPotBlock;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Unique
    private CalciteDecoratedPotBlockEntity paradiseLost$calcitePot;

    @Definition(id = "is", method = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    @Definition(id = "DECORATED_POT", field = "Lnet/minecraft/world/level/block/Blocks;DECORATED_POT:Lnet/minecraft/world/level/block/Block;")
    @Expression("?.is(DECORATED_POT)")
    @ModifyExpressionValue(method = "renderByItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean paradiseLost$enterPotBranch(boolean original, @Local BlockState state) {
        return original || state.getBlock() instanceof CalciteDecoratedPotBlock;
    }

    @Definition(id = "decoratedPot", field = "Lnet/minecraft/client/renderer/BlockEntityWithoutLevelRenderer;decoratedPot:Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;")
    @Expression("this.decoratedPot")
    @ModifyExpressionValue(method = "renderByItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private DecoratedPotBlockEntity paradiseLost$swapPotEntity(DecoratedPotBlockEntity original, @Local BlockState state) {
        if (state.getBlock() instanceof CalciteDecoratedPotBlock) {
            if (this.paradiseLost$calcitePot == null) {
                this.paradiseLost$calcitePot = new CalciteDecoratedPotBlockEntity(BlockPos.ZERO, state);
            }
            return this.paradiseLost$calcitePot;
        }
        return original;
    }
}
