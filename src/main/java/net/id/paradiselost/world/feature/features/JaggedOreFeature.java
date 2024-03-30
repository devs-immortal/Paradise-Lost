package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.blocks.FloatingBlock;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.world.feature.configs.JaggedOreConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class JaggedOreFeature extends Feature<JaggedOreConfig> {
    public JaggedOreFeature(Codec<JaggedOreConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<JaggedOreConfig> context) {
        JaggedOreConfig config = context.getConfig();
        BlockPos center = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        Random rand = context.getRandom();

        int height = center.getY() + config.height().get(rand);
        for (int y = center.getY(); y < height; y++) {
            int sizex = config.width().get(rand);
            int xOffset = rand.nextBetween(0, 2);
            int sizez = config.length().get(rand);
            for (int x = 0; x < sizex; x++) {
                int zOffset = config.lengthOffset().get(rand);
                for (int z = zOffset; z < sizez + zOffset; z++) {
                    BlockPos iPos = new BlockPos(center.getX() + x + xOffset, y, center.getZ() + z);
                    if (world.getBlockState(iPos).isIn(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE)) {
                        BlockState block = config.block().getBlockState(rand, center);
                        if (!(block.getBlock() instanceof FloatingBlock && !world.getBlockState(iPos.up()).isOpaque()))
                            world.setBlockState(iPos, config.block().getBlockState(rand, iPos), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }

        return true;
    }
}
