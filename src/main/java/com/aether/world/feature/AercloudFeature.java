package com.aether.world.feature;

import com.aether.world.feature.config.AercloudConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Consumer;

public class AercloudFeature extends Feature<AercloudConfig> {
    private static final Codec<AercloudConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(AercloudConfig::getCloudState),
            Codec.BOOL.fieldOf("flat").forGetter(AercloudConfig::isFlat),
            Codec.INT.fieldOf("maxRadius").forGetter(AercloudConfig::maxSegments),
            Codec.INT.fieldOf("y").forGetter(AercloudConfig::getY)
    ).apply(instance, AercloudConfig::new));

    public AercloudFeature() {
        super(CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess worldIn, ChunkGenerator generator, Random randIn, BlockPos posIn, AercloudConfig configIn) {
        BlockPos origin = new BlockPos(posIn.getX(), (randIn.nextInt(configIn.isFlat() ? 4 : 64)) + configIn.getY(), posIn.getZ());
        BlockPos.Mutable mut0 = new BlockPos.Mutable();
        BlockPos.Mutable mut1 = new BlockPos.Mutable();

        int x = 0, y = 0, z = 0;
        int xTrend = randIn.nextInt() % 2 == 0 ? -1 : 1;
        int zTrend = randIn.nextInt() % 2 == 0 ? -1 : 1;
        int stride = randIn.nextInt(2) + 1;
        boolean which = randIn.nextBoolean();

        origin = origin.add(-64 * xTrend, 0, -64 * zTrend);

        for (int i = 0; i < configIn.maxSegments(); ++i) {
            int dX = randIn.nextInt(3);
            int dY = randIn.nextInt(20) == 0 ? 0 : randIn.nextInt(3);
            int dZ = randIn.nextInt(3);

            int xMax = randIn.nextInt(4) + 9;
            int yMax = randIn.nextInt(1) + 2;
            int zMax = randIn.nextInt(4) + 9;

            int radius = randIn.nextInt(4) + 4;

            x += dX + (xTrend * (which ? stride : 1));
            y += dY - 1;
            z += dZ + (zTrend * (which ? 1 : stride));

            mut0.set(origin);
            mut0.move(x, y, z);

            for (int x0 = x - xMax; x0 < x + xMax; ++x0) {
                for (int y0 = y; y0 < y + yMax; ++y0) {
                    for (int z0 = z - zMax; z0 < z + zMax; ++z0) {
                        mut1.set(origin);
                        mut1.move(x0, y0, z0);

                        try {
                            if (worldIn.getBlockState(mut1).isAir() && mut1.isWithinDistance(mut0, radius)) {
                                this.setBlockState(worldIn, mut1, configIn.getCloudState());
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
        return true;
    }
}