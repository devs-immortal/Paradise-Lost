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

public class AercloudFeature extends Feature<AercloudConfig> {

    private static final Codec<AercloudConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(AercloudConfig::getCloudState),
            Codec.BOOL.fieldOf("flat").forGetter(AercloudConfig::isFlat),
            Codec.INT.fieldOf("amount").forGetter(AercloudConfig::cloudModifier),
            Codec.INT.fieldOf("y").forGetter(AercloudConfig::getY)
    ).apply(instance, AercloudConfig::new));

    public AercloudFeature() {
        super(CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess worldIn, ChunkGenerator generator, Random randIn, BlockPos posIn, AercloudConfig configIn) {
        BlockPos origin = new BlockPos(posIn.getX() - 14, (configIn.isFlat() ? 0 : randIn.nextInt(64)) + configIn.getY(), posIn.getZ() - 14);
        BlockPos position = new BlockPos(origin);

        for (int amount = 0; amount < configIn.cloudAmount(); ++amount) {
            boolean offsetY = ((randIn.nextBoolean() && !configIn.isFlat()) || (configIn.isFlat() && randIn.nextInt(10) == 0));

            int xOffset = randIn.nextInt(2);
            int yOffset = (offsetY ? randIn.nextInt(3) - 1 : 0);
            int zOffset = randIn.nextInt(2);

            position = position.add(xOffset, yOffset, zOffset);

            for (int x = position.getX(); x < position.getX() + randIn.nextInt(2) + 3 * configIn.cloudModifier(); ++x) {
                for (int y = position.getY(); y < position.getY() + randIn.nextInt(1) + 2; ++y) {
                    for (int z = position.getZ(); z < position.getZ() + randIn.nextInt(2) + 3 * configIn.cloudModifier(); ++z) {
                        BlockPos pos = new BlockPos(x, y, z);

                        if (worldIn.isAir(pos)) {
                            if (Math.abs(x - position.getX()) + Math.abs(y - position.getY()) + Math.abs(z - position.getZ()) < 4 * configIn.cloudModifier() + randIn.nextInt(2)) {
                                this.setBlockState(worldIn, pos, configIn.getCloudState());
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
