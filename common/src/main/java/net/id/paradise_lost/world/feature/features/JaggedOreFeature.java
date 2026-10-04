package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.block.FloatingBlock;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.id.paradise_lost.world.feature.configs.JaggedOreConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class JaggedOreFeature extends Feature<JaggedOreConfig> {
    public JaggedOreFeature(Codec<JaggedOreConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<JaggedOreConfig> context) {
        JaggedOreConfig config = context.config();
        BlockPos center = context.origin();
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();

        int height = center.getY() + config.height().sample(rand);
        for (int y = center.getY(); y < height; y++) {
            int sizex = config.width().sample(rand);
            int xOffset = rand.nextIntBetweenInclusive(0, 2);
            int sizez = config.length().sample(rand);
            for (int x = 0; x < sizex; x++) {
                int zOffset = config.lengthOffset().sample(rand);
                for (int z = zOffset; z < sizez + zOffset; z++) {
                    BlockPos iPos = new BlockPos(center.getX() + x + xOffset, y, center.getZ() + z);
                    if (world.getBlockState(iPos).is(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE)) {
                        BlockState block = config.block().getState(rand, center);
                        if (!(block.getBlock() instanceof FloatingBlock && !world.getBlockState(iPos.above()).canOcclude()))
                            world.setBlock(iPos, config.block().getState(rand, iPos), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }

        return true;
    }
}
