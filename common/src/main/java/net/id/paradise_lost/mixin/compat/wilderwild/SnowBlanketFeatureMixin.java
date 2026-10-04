package net.id.paradise_lost.mixin.compat.wilderwild;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
        targets = "net.frozenblock.wilderwild.worldgen.impl.feature.SnowBlanketFeature",
        remap = false
)
public class SnowBlanketFeatureMixin {
    @Unique
    private static final TagKey<Block> PARADISE_LOST$SNOW_SEARCH_THROUGH = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("wilderwild", "snow_generation_can_search_through")
    );

    @WrapOperation(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/frozenblock/wilderwild/worldgen/impl/feature/SnowBlanketFeature;findLowestHeightForSnow(Lnet/minecraft/world/level/WorldGenLevel;II)I",
                    remap = false
            ),

            require = 0,
            remap = false
    )
    private static int paradiseLost$boundSnowSearch(WorldGenLevel level, int x, int z, Operation<Integer> original) {
        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int minY = level.getMinY();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, surface, z);

        if (surface - 1 >= minY && !paradiseLost$canSearchThrough(level.getBlockState(cursor.setY(surface - 1)))) {
            return original.call(level, x, z);
        }

        int height = surface;
        while (height > minY && paradiseLost$canSearchThrough(level.getBlockState(cursor.setY(height).move(Direction.DOWN)))) {
            height -= 1;
        }
        return height;
    }

    @Unique
    private static boolean paradiseLost$canSearchThrough(BlockState state) {
        return (state.is(PARADISE_LOST$SNOW_SEARCH_THROUGH) || state.isAir()) && state.getFluidState().isEmpty();
    }
}
