package net.id.paradise_lost.mixin.world.gen;

import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.id.paradise_lost.world.feature.RiverField;
import net.id.paradise_lost.world.feature.configs.RiverConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Surface features sample the heightmap and will happily grow in a channel the river
 * carved earlier in the same chunk. Skip any placement whose column is in the water.
 * Underground positions are left alone, and the river feature itself is not filtered.
 */
@Mixin(ConfiguredFeature.class)
public class ConfiguredFeatureMixin {
    private static final RiverConfiguration RIVER = RiverConfiguration.noiseDefaults();
    private static final int SURFACE_REACH = 12;

    @Shadow
    @Final
    private FeatureConfiguration config;

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipRiverColumn(
            WorldGenLevel level,
            net.minecraft.world.level.chunk.ChunkGenerator generator,
            RandomSource random,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (this.config instanceof RiverConfiguration) {
            return;
        }
        if (!level.getLevel().dimension().equals(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY)) {
            return;
        }
        int surface = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
        if (pos.getY() < surface - SURFACE_REACH) {
            return;
        }
        RiverField.Noise noise = RiverField.noise(level.getSeed(), RIVER.salt());
        if (RiverField.covers(noise, RIVER, pos.getX(), pos.getZ(), 1.0)) {
            cir.setReturnValue(false);
        }
    }
}
