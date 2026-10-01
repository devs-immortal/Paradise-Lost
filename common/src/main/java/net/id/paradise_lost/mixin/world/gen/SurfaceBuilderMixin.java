package net.id.paradise_lost.mixin.world.gen;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.world.dimension.ParadiseLostBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SurfaceSystem.class)
public abstract class SurfaceBuilderMixin {

    @Shadow
    @Final
    private BlockState defaultBlock;
    @Shadow
    @Final
    private NormalNoise badlandsPillarNoise;
    @Shadow
    @Final
    private NormalNoise badlandsPillarRoofNoise;
    @Shadow
    @Final
    private NormalNoise badlandsSurfaceNoise;

    @Inject(method = "buildSurface", at = @At("TAIL"))
    public void buildSurface(RandomState noiseConfig, BiomeManager biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, WorldGenerationContext heightContext, ChunkAccess chunk, NoiseChunk chunkNoiseSampler, SurfaceRules.RuleSource materialRule, CallbackInfo ci) {
        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        final ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        BlockColumn blockColumn = new BlockColumn() {
            @Override
            public BlockState getBlock(int y) {
                return chunk.getBlockState(mutable.setY(y));
            }

            @Override
            public void setBlock(int y, BlockState state) {
                LevelHeightAccessor heightLimitView = chunk.getHeightAccessorForGeneration();
                if (y >= heightLimitView.getMinBuildHeight() && y < heightLimitView.getMaxBuildHeight()) {
                    chunk.setBlockState(mutable.setY(y), state, false);
                    if (!state.getFluidState().isEmpty()) {
                        chunk.markPosForPostprocessing(mutable);
                    }
                }
            }

            public String toString() {
                return "ChunkBlockColumn " + chunkPos;
            }
        };
        BlockPos.MutableBlockPos mutable2 = new BlockPos.MutableBlockPos();

        for (int k = 0; k < 16; k++) {
            for (int l = 0; l < 16; l++) {
                int xPos = i + k;
                int zPos = j + l;
                int yPos = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, k, l) + 1;
                mutable.setX(xPos).setZ(zPos);
                Holder<Biome> registryEntry = biomeAccess.getBiome(mutable2.set(xPos, useLegacyRandom ? 0 : yPos, zPos));
                if (registryEntry.is(ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY)) {
                    this.placeCragPillar(blockColumn, xPos, zPos, yPos, chunk);
                }
            }
        }
    }

    @Unique
    private void placeCragPillar(BlockColumn column, int x, int z, int surfaceY, LevelHeightAccessor chunk) {
        double e = Math.min(Math.abs(this.badlandsSurfaceNoise.getValue(x, 0.0, z) * 8.25), this.badlandsPillarNoise.getValue(x * 0.2, 0.0, z * 0.2) * 15.0);
        if (surfaceY > 60 && !(e <= 0.0)) {
            double h = Math.abs(this.badlandsPillarRoofNoise.getValue(x * 0.75, 0.0, z * 0.75) * 1.5);
            double preHeight = Math.min(e * e * 2.5, Math.ceil(h * 30.0) + 10.0);
            int j = Mth.floor(surfaceY + preHeight);
            if (surfaceY <= j && preHeight > 5) {
                int steps = 0;
                for (int k = j; k >= chunk.getMinBuildHeight(); k--) {
                    if (!column.getBlock(k).isAir()) {
                        column.setBlock(k, this.defaultBlock);
                        column.setBlock(k - 1, this.defaultBlock);
                        break;
                    }
                    if (e > 3.2 && steps == 0) {
                        column.setBlock(k, BlockRegistry.HIGHLANDS_GRASS.get().defaultBlockState());
                    } else if (e > 3.3 && steps == 1) {
                        column.setBlock(k, BlockRegistry.DIRT.get().defaultBlockState());
                    } else {
                        column.setBlock(k, (steps > preHeight / 2) ? this.defaultBlock : Blocks.CALCITE.defaultBlockState());
                    }
                    steps++;
                }
            }
        }
    }

}
