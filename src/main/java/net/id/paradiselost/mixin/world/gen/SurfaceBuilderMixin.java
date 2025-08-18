package net.id.paradiselost.mixin.world.gen;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.world.dimension.ParadiseLostBiomes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.BlockColumn;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SurfaceBuilder.class)
public abstract class SurfaceBuilderMixin {

    @Shadow
    @Final
    private BlockState defaultState;
    @Shadow
    @Final
    private DoublePerlinNoiseSampler badlandsPillarNoise;
    @Shadow
    @Final
    private DoublePerlinNoiseSampler badlandsPillarRoofNoise;
    @Shadow
    @Final
    private DoublePerlinNoiseSampler badlandsSurfaceNoise;

    @Inject(method = "buildSurface", at = @At("TAIL"))
    public void buildSurface(NoiseConfig noiseConfig, BiomeAccess biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, HeightContext heightContext, Chunk chunk, ChunkNoiseSampler chunkNoiseSampler, MaterialRules.MaterialRule materialRule, CallbackInfo ci) {
        final BlockPos.Mutable mutable = new BlockPos.Mutable();
        final ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getStartX();
        int j = chunkPos.getStartZ();
        BlockColumn blockColumn = new BlockColumn() {
            @Override
            public BlockState getState(int y) {
                return chunk.getBlockState(mutable.setY(y));
            }

            @Override
            public void setState(int y, BlockState state) {
                HeightLimitView heightLimitView = chunk.getHeightLimitView();
                if (y >= heightLimitView.getBottomY() && y < heightLimitView.getTopY()) {
                    chunk.setBlockState(mutable.setY(y), state, false);
                    if (!state.getFluidState().isEmpty()) {
                        chunk.markBlockForPostProcessing(mutable);
                    }
                }
            }

            public String toString() {
                return "ChunkBlockColumn " + chunkPos;
            }
        };
        BlockPos.Mutable mutable2 = new BlockPos.Mutable();

        for (int k = 0; k < 16; k++) {
            for (int l = 0; l < 16; l++) {
                int xPos = i + k;
                int zPos = j + l;
                int yPos = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, k, l) + 1;
                mutable.setX(xPos).setZ(zPos);
                RegistryEntry<Biome> registryEntry = biomeAccess.getBiome(mutable2.set(xPos, useLegacyRandom ? 0 : yPos, zPos));
                if (registryEntry.matchesKey(ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY)) {
                    this.placeCragPillar(blockColumn, xPos, zPos, yPos, chunk);
                }
            }
        }
    }

    @Unique
    private void placeCragPillar(BlockColumn column, int x, int z, int surfaceY, HeightLimitView chunk) {
        double e = Math.min(Math.abs(this.badlandsSurfaceNoise.sample(x, 0.0, z) * 8.25), this.badlandsPillarNoise.sample(x * 0.2, 0.0, z * 0.2) * 15.0);
        if (surfaceY > 60 && !(e <= 0.0)) {
            double h = Math.abs(this.badlandsPillarRoofNoise.sample(x * 0.75, 0.0, z * 0.75) * 1.5);
            double preHeight = Math.min(e * e * 2.5, Math.ceil(h * 30.0) + 10.0);
            int j = MathHelper.floor(surfaceY + preHeight);
            if (surfaceY <= j && preHeight > 5) {
                int steps = 0;
                for (int k = j; k >= chunk.getBottomY(); k--) {
                    if (!column.getState(k).isAir()) {
                        column.setState(k, this.defaultState);
                        column.setState(k-1, this.defaultState);
                        break;
                    }
                    if (e > 3.2 && steps == 0) {
                        column.setState(k, ParadiseLostBlocks.HIGHLANDS_GRASS.getDefaultState());
                    } else if (e > 3.3 && steps == 1) {
                        column.setState(k, ParadiseLostBlocks.DIRT.getDefaultState());
                    } else {
                        column.setState(k, (steps > preHeight/2) ? this.defaultState : Blocks.CALCITE.getDefaultState());
                    }
                    steps++;
                }
            }
        }
    }


}
