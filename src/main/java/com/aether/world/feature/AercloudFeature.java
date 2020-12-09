package com.aether.world.feature;

import com.aether.world.feature.config.AercloudConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.List;
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
        BlockPos.Mutable mut = new BlockPos.Mutable(posIn.getX() + 8, (randIn.nextInt(configIn.isFlat() ? 4 : 16)) + 30 + configIn.getY(), posIn.getZ() + 8);

        ChunkRegion region = (ChunkRegion) worldIn;

        int size = randIn.nextInt(60) + 20;
        double s = Math.sqrt(size);

        List<int[]> positions = new ArrayList<>();
        MutableBoolean fail = new MutableBoolean(false);

        for (int i = 0; i < size; ++i) {
            double v = s * Math.sin((Math.PI * i) / size);
            int base = (int) (s + v);

            int width = (int) (randIn.nextDouble() * base + s);
            int height = (int) (randIn.nextDouble() * base + (s / 2));
            int depth = (int) (randIn.nextDouble() * base + s);
            BlockPos.iterate(mut.add(-width / 2, -height / 2, -depth / 2), mut.add(width / 2, height / 2, depth / 2)).forEach(pos -> {
                if (worldIn.getBlockState(pos).isAir() && region.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4)) {
                    positions.add(new int[] {pos.getX(), pos.getY(), pos.getZ()});
                } else {
                    fail.setTrue();
                }
            });

            int distance = (int) Math.ceil(s + (s - v));

            mut.move(randIn.nextInt(distance) - distance / 2, randIn.nextInt(3) - 1, randIn.nextInt(distance) - (distance / 2));
        }

        if (!fail.booleanValue()) {
            for (int[] pos : positions) {
                mut.set(pos[0], pos[1], pos[2]);
                this.setBlockState(worldIn, mut, configIn.getCloudState());
            }
        }

        return !fail.booleanValue();
    }
}