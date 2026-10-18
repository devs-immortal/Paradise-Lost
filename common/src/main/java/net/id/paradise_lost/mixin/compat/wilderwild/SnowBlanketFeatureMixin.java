package net.id.paradise_lost.mixin.compat.wilderwild;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.compat.WilderWildSnowSearch;
import net.minecraft.world.level.WorldGenLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.frozenblock.wilderwild.worldgen.impl.feature.SnowBlanketFeature")
public class SnowBlanketFeatureMixin {
    @WrapOperation(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/frozenblock/wilderwild/worldgen/impl/feature/SnowBlanketFeature;findLowestHeightForSnow(Lnet/minecraft/world/level/WorldGenLevel;II)I"
            ),
            require = 0
    )
    private static int paradiseLost$boundSnowSearch(WorldGenLevel level, int x, int z, Operation<Integer> original) {
        return WilderWildSnowSearch.findLowestHeightForSnow(level, x, z, original);
    }
}
