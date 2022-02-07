package net.id.aether.mixin.item;

import net.id.aether.util.MapColorCreator;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.block.*;
import net.minecraft.item.FilledMapItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FilledMapItem.class)
public abstract class FilledMapMixin {

    @Unique
    private final MapColor AETHER_BACKGROUND = MapColorCreator.createMapColor(62, 0xe3fffd);

    /**
     * Changes the color of the aether void to a pleasant blue.
     * @author Jack Papel
     */
    @Redirect(
            method = "updateColors",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/map/MapState;removeBanner(Lnet/minecraft/world/BlockView;II)V"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;getMapColor(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/MapColor;"
            )
    )
    private MapColor fixAetherVoidColor(BlockState instance, BlockView world, BlockPos pos) {
        if (((World) world).getRegistryKey().equals(AetherDimension.AETHER_WORLD_KEY)
                && pos.equals(Vec3i.ZERO) && !world.getBlockState(pos).isOf(Blocks.BEDROCK)){
            return AETHER_BACKGROUND;
        }
        else return instance.getMapColor(world, pos);
    }
}