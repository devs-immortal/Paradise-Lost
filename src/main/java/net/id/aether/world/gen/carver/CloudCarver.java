package net.id.aether.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class CloudCarver extends Carver<CloudCarverConfig> {

    public CloudCarver(Codec<CloudCarverConfig> configCodec) {
        super(configCodec);
        this.alwaysCarvableBlocks = ImmutableSet.of(Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR);
    }

    private static boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ) {
        return scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ >= 0.85D;
    }

    @Override
    public boolean carve(CarverContext context, CloudCarverConfig config, Chunk chunk, Function<BlockPos, Biome> posToBiome, Random random, AquiferSampler sampler, ChunkPos pos, BitSet carvingMask) {

        int mainChunkX = chunk.getPos().x;
        int mainChunkZ = chunk.getPos().z;

        double x = pos.getOffsetX(16);
        double y = config.y.get(random, context);
        double z = pos.getOffsetZ(16);

        int size = (int) Math.round((4 * 2 - 1) * (random.nextInt(4) + 1) * config.sizeMultiplier.get(random) * 1.5);

        int systemCount = 1; // formerly: random.nextInt(random.nextInt(random.nextInt(config.maxSystems.get(random)) + 1) + 1);

        // Generate each cave system
        for (int i = 0; i < systemCount; ++i) {
            // Get the position to spawn

            // Each system generates 1 tunnel by default, more has a chance to be added later
            int tunnelCount = 1;

            float yawToPitchRatio = (random.nextFloat() / 4 + 0.4F);

            // Each system has a 1/4 chance to generate a circular room
            //if (random.nextInt(4) == 0) {
            //    // The room has a variable size, from 1 to 7 units
            //    float size = 1.0F + random.nextFloat() * 6.0F;
            //    carveCaveRoom(chunk, random, mainChunkX, mainChunkZ, x, y, z, size);
            //    // Add 0 to 3 more tunnels coming out from the cave room, to make it more connected and variable
            //    tunnelCount += random.nextInt(4);
            //}

            // Generate each tunnel
            for (int j = 0; j < tunnelCount; j++) {
                // Calculate both the yaw and pitch.
                // The yaw represents the horizontal movement of the cave, as well as the horizontal size.
                // The pitch represents the vertical movement of the cave, as well as the vertical size.

                // Yaw takes up the entire unit circle, so the cave can move in all directions horizontally
                float yaw = (float) (random.nextFloat() * Math.PI - (Math.PI / 2)) * config.yawMultiplier.get(random);
                // Pitch goes from -0.125 to 0.125, so the cave can have a slight vertical shift up or down.
                float pitch = (float) (random.nextFloat() * Math.PI - (Math.PI / 2)) / 2;
                // The width of the cave goes from 1 to 3
                float width = random.nextFloat() * 2.0F + (random.nextFloat() * 3F) + 4.5F;
                // 1/10 chance to drastically increase the size of the cave
                if (random.nextInt(config.engorgementChance.get(random)) == 0) {
                    width *= random.nextFloat() * random.nextFloat() * 1.50F + 1.0F;
                }

                width *= config.widthMultiplier.get(random);

                // Reduce the branch count by 0% to 25%
                int maxBranches = size - random.nextInt(size / 4);

                // Start the recursive tunnel carving
                this.carveTunnels(context, config, chunk, posToBiome, carvingMask, random.nextLong(), sampler, mainChunkX, mainChunkZ, x, y, z, width, yaw, pitch, yawToPitchRatio, 0, maxBranches, ((context1, scaledRelativeX, scaledRelativeY, scaledRelativeZ, y1) -> isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ)));
                //carveRegion(context, config, chunk, posToBiome, random.nextLong(), seaLevel, x, y, z, 2, 0.5, carvingMask, ((context1, scaledRelativeX, scaledRelativeY, scaledRelativeZ, y1) -> false));
            }
        }
        return true;
    }


    //protected void carveCaveRoom(Chunk chunk, Random random, class_6108 config, int seaLevel, int mainChunkX, int mainChunkZ, double x, double y, double z, float yaw) {
    //    // Increase the size of the horizontal
    //    double scaledYaw = 1.5D + yaw;
    //    // Scale the vertical size to be 1/2 the horizontal size
    //    double scaledPitch = scaledYaw * 0.5;
//
    //    // Call the internal carveRegion function with the given yaw and pitch, as well as a slight x offset
    //    this.carveRegion(chunk, config, random, seaLevel, mainChunkX, mainChunkZ, x + 1.0, y, z, scaledYaw, scaledPitch);
    //}

    protected void carveTunnels(CarverContext context, CloudCarverConfig config, Chunk chunk, Function<BlockPos, Biome> posToBiome, BitSet carvingMask, long seed, AquiferSampler sampler, int mainChunkX, int mainChunkZ, double x, double y, double z, float width, float yaw, float pitch, float yawToPitchRatio, int branchStartIndex, int branchCount, SkipPredicate skipPredicate) {
        // Get the position for starting the next branch, from 25% of the total length to 75% to ensure it doesn't branch near the ends

        Random random = new Random(seed);

        int nextBranch = random.nextInt(branchCount / 2) + branchCount / 4;

        // 16% of tunnels are more steeper than normal
        boolean steeperCave = random.nextInt(6) == 0;
        float yawChange = 0.0F;
        float pitchChange = 0.0F;

        for (int i = branchStartIndex; i < branchCount; ++i) {
            // Make the yaw the sin of the cave from [0, pi], making it larger in the middle and smaller at the edges.
            // The horizontal size of the cave ranges from [1.5, 1.5 + width].
            double scaledYaw = 1.5 + (double) (MathHelper.sin(3.1415927F * (float) i / (float) branchCount) * width);
            // Scale the pitch by 1.0. The nether carver uses 2.0.
            double scaledPitch = scaledYaw * yawToPitchRatio;

            // Cosine of pitch is the delta of the horizontal movement.
            // the steeperCave boolean makes this much smaller through reducing the pitch, making the delta smaller, producing steep caves.
            float delta = MathHelper.cos(pitch);

            // Use trig to increment the position of the tunnel
            x += MathHelper.cos(yaw) * delta;
            y += MathHelper.sin(pitch);
            z += MathHelper.sin(yaw) * delta;

            // Modify the pitch based on the steeperCave value, and add the pitch change
            pitch *= steeperCave ? 0.82F : 0.65F;
            pitch += pitchChange * 0.1F;
            // Add the yaw change
            yaw += yawChange * 0.1F;
            // Scale the change values downwards for the next addition
            pitchChange *= 0.9F;
            yawChange *= 0.75F;
            // Add some more values onto the change values, to modify it for the next iteration
            pitchChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat();
            yawChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * config.maxYaw.get(random);

            // Split the cave off into 2 more tunnels and end this tunnel.
            // The width > 1.0 check makes it so only the first tunnel can split.
            if (i == nextBranch && width > 1.0F && random.nextBoolean()) {
                // Change the yaw by pi/2 with different sign for both tunnels, to split them off at a fork and make them go opposite ways.
                // Reduce the pitch by a factor of 3 to flatten out cave tunnel forks.
                this.carveTunnels(context, config, chunk, posToBiome, carvingMask, random.nextLong(), sampler, mainChunkX, mainChunkZ, x, y, z, width * (random.nextFloat() / 4 + 0.75F), yaw - (random.nextFloat() / 3), pitch / 3, yawToPitchRatio, i, branchCount, skipPredicate);
                this.carveTunnels(context, config, chunk, posToBiome, carvingMask, random.nextLong(), sampler, mainChunkX, mainChunkZ, x, y, z, width * (random.nextFloat() / 4 + 0.75F), yaw + (random.nextFloat() / 3), pitch / 3, yawToPitchRatio, i, branchCount, skipPredicate);
                return;
            }

            // 25% of generation is skipped for a more random feeling
            if (random.nextInt(4) != 0) {
                // Carve the region at this position
                carveRegion(context, config, chunk, posToBiome, random.nextLong(), sampler, x, y, z, scaledYaw, scaledPitch, carvingMask, skipPredicate);
            }
        }
    }

    protected boolean carveRegion(CarverContext context, CloudCarverConfig config, Chunk chunk, Function<BlockPos, Biome> posToBiome, long seed, AquiferSampler sampler, double x, double y, double z, double horizontalScale, double verticalScale, BitSet carvingMask, Carver.SkipPredicate skipPredicate) {
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.x;
        int j = chunkPos.z;
        Random random = new Random(seed + (long) i + (long) j);
        double d = chunkPos.getCenterX();
        double e = chunkPos.getCenterZ();
        double f = 16.0D + horizontalScale * 2.0D;
        if (!(Math.abs(x - d) > f) && !(Math.abs(z - e) > f)) {
            int k = chunkPos.getStartX();
            int l = chunkPos.getStartZ();
            int m = Math.max(MathHelper.floor(x - horizontalScale) - k - 1, 0);
            int n = Math.min(MathHelper.floor(x + horizontalScale) - k, 15);
            int o = Math.max(MathHelper.floor(y - verticalScale) - 1, context.getMinY() + 1);
            int p = Math.min(MathHelper.floor(y + verticalScale) + 1, chunk.getHeight());
            int q = Math.max(MathHelper.floor(z - horizontalScale) - l - 1, 0);
            int r = Math.min(MathHelper.floor(z + horizontalScale) - l, 15);
            if (!config.aquifers && this.isRegionUncarvable(chunk, m, n, o, p, q, r)) {
                return false;
            } else {
                boolean bl = false;
                BlockPos.Mutable mutable = new BlockPos.Mutable();
                BlockPos.Mutable mutable2 = new BlockPos.Mutable();

                for (int s = m; s <= n; ++s) {
                    int t = chunkPos.getOffsetX(s);
                    double g = ((double) t + 0.5D - x) / horizontalScale;

                    for (int u = q; u <= r; ++u) {
                        int v = chunkPos.getOffsetZ(u);
                        double h = ((double) v + 0.5D - z) / horizontalScale;
                        if (!(g * g + h * h >= 1.0D)) {
                            MutableBoolean mutableBoolean = new MutableBoolean(false);

                            for (int w = p; w > o; --w) {
                                double aa = ((double) w - 0.5D - y) / verticalScale;
                                if (!skipPredicate.shouldSkip(context, g, aa, h, w)) {
                                    int ab = w - context.getMinY();
                                    int ac = s | u << 4 | ab << 8;
                                    if (!carvingMask.get(ac)) {
                                        carvingMask.set(ac);
                                        mutable.set(t, w, v);
                                        bl |= this.carveAtPoint(context, config, chunk, posToBiome, carvingMask, random, mutable, mutable2, sampler, mutableBoolean);
                                    }
                                }
                            }
                        }
                    }
                }

                return bl;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldCarve(CloudCarverConfig config, Random random) {
        return random.nextFloat() <= config.probability;
    }

    @Override
    protected boolean carveAtPoint(CarverContext context, CloudCarverConfig config, Chunk chunk, Function<BlockPos, Biome> posToBiome, BitSet carvingMask, Random random, BlockPos.Mutable pos, BlockPos.Mutable downPos, AquiferSampler sampler, MutableBoolean foundSurface) {
        BlockState blockState = chunk.getBlockState(pos);
        BlockState blockState2 = chunk.getBlockState(downPos.set(pos, Direction.UP));
        if (blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.MYCELIUM)) {
            foundSurface.setTrue();
        }

        if (!this.canCarveBlock(blockState, blockState2)) {
            return false;
        } else {
            BlockState blockState3 = this.getState(context, config, pos, sampler);
            if (blockState3 == null) {
                return false;
            } else {
                chunk.setBlockState(pos, blockState3, false);
                if (foundSurface.isTrue()) {
                    downPos.set(pos, Direction.DOWN);
                    if (chunk.getBlockState(downPos).isOf(Blocks.DIRT)) {
                        chunk.setBlockState(downPos, posToBiome.apply(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial(), false);
                    }
                }

                return true;
            }
        }
    }

    @Nullable
    private BlockState getState(CarverContext context, CloudCarverConfig config, BlockPos pos, AquiferSampler sampler) {
        return config.cloudState;
    }

    @Override
    protected boolean canCarveBlock(BlockState state, BlockState stateAbove) {
        return state.isAir();
    }
}
