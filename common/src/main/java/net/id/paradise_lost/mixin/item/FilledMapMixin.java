package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.util.ParadiseLostMapColorUtil;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MapItem.class)
public abstract class FilledMapMixin {

    @Redirect(
            method = "update",
            slice = @Slice(
                from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;checkBanners(Lnet/minecraft/world/level/BlockGetter;II)V"
                )
            ),
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/block/state/BlockState;getMapColor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/MapColor;"
            )
    )
    private MapColor updateColors(BlockState instance, BlockGetter world, BlockPos pos) {
        if (
                ((Level) world).dimension().equals(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY)
                        && pos.getY() == 0
                        && instance.is(Blocks.BEDROCK)
        ) {
            return ParadiseLostMapColorUtil.PARADISE_LOST_BACKGROUND;
        } else {
            return instance.getMapColor(world, pos);
        }
    }
}
