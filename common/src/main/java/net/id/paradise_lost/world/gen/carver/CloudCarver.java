package net.id.paradise_lost.world.gen.carver;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

public class CloudCarver extends WorldCarver<CloudCarverConfig> {

    public CloudCarver(Codec<CloudCarverConfig> configCodec) {
        super(configCodec);
    }

    private static boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ) {
        return scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ >= 0.85D;
    }

    @Override
    public boolean carve(CarvingContext context, CloudCarverConfig config, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> posToBiome, RandomSource random, Aquifer sampler, ChunkPos pos, CarvingMask carvingMask) {
        int mainChunkX = chunk.getPos().x;
        int mainChunkZ = chunk.getPos().z;

        double x = pos.getBlockX(16);
        double y = config.y.sample(random, context);
        double z = pos.getBlockZ(16);

        int size = (int) Math.round((4 * 2 - 1) * (random.nextInt(4) + 1) * config.sizeMultiplier.sample(random) * 1.5);

        int systemCount = 1;

        for (int i = 0; i < systemCount; ++i) {

            int tunnelCount = 1;

            float yawToPitchRatio = (random.nextFloat() / 4 + 0.4F);

            for (int j = 0; j < tunnelCount; j++) {

                float yaw = (float) (random.nextFloat() * Math.PI - (Math.PI / 2)) * config.yawMultiplier.sample(random);

                float pitch = (float) (random.nextFloat() * Math.PI - (Math.PI / 2)) / 2;

                float width = random.nextFloat() * 2.0F + (random.nextFloat() * 3F) + 4.5F;

                if (random.nextInt(config.engorgementChance.sample(random)) == 0) {
                    width *= random.nextFloat() * random.nextFloat() * 1.50F + 1.0F;
                }

                width *= config.widthMultiplier.sample(random);

                int maxBranches = size - random.nextInt(size / 4);

                this.carveTunnels(context, config, chunk, posToBiome, carvingMask, random.nextLong(), sampler, mainChunkX, mainChunkZ, x, y, z, width, yaw, pitch, yawToPitchRatio, 0, maxBranches, ((context1, scaledRelativeX, scaledRelativeY, scaledRelativeZ, y1) -> isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ)));

            }
        }
        return true;
    }

    protected void carveTunnels(CarvingContext context, CloudCarverConfig config, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> posToBiome, CarvingMask carvingMask, long seed, Aquifer sampler, int mainChunkX, int mainChunkZ, double x, double y, double z, float width, float yaw, float pitch, float yawToPitchRatio, int branchStartIndex, int branchCount, CarveSkipChecker skipPredicate) {

        RandomSource random = RandomSource.create(seed);

        int nextBranch = random.nextInt(branchCount / 2) + branchCount / 4;

        boolean steeperCave = random.nextInt(6) == 0;
        float yawChange = 0.0F;
        float pitchChange = 0.0F;

        for (int i = branchStartIndex; i < branchCount; ++i) {

            double scaledYaw = 1.5 + (double) (Mth.sin(3.1415927F * (float) i / (float) branchCount) * width);

            double scaledPitch = scaledYaw * yawToPitchRatio;

            float delta = Mth.cos(pitch);

            x += Mth.cos(yaw) * delta;
            y += Mth.sin(pitch);
            z += Mth.sin(yaw) * delta;

            pitch *= steeperCave ? 0.82F : 0.65F;
            pitch += pitchChange * 0.1F;

            yaw += yawChange * 0.1F;

            pitchChange *= 0.9F;
            yawChange *= 0.75F;

            pitchChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat();
            yawChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * random.nextFloat() * config.maxYaw.sample(random);

            if (i == nextBranch && width > 1.0F && random.nextBoolean()) {

                return;
            }

            if (random.nextInt(4) != 0) {

                carveEllipsoid(context, config, chunk, posToBiome, sampler, x, y, z, scaledYaw, scaledPitch, carvingMask, skipPredicate);
            }
        }
    }

    @Override
    public boolean isStartChunk(CloudCarverConfig config, RandomSource random) {
        return random.nextFloat() <= config.probability;
    }

    @Nullable
    @Override
    protected BlockState getCarveState(CarvingContext context, CloudCarverConfig config, BlockPos pos, Aquifer sampler) {
        return config.cloudState.getState(null, pos);
    }
}
