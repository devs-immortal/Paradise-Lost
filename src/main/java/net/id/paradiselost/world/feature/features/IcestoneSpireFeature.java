package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IcestoneSpireFeature extends Feature<DefaultFeatureConfig> {

    private static final List<BlockState> secStates = new ArrayList<>();

    public IcestoneSpireFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        var origin = context.getOrigin();
        var world = context.getWorld();
        var random = context.getRandom();

        if (world.getBlockState(origin).isOf(Blocks.POWDER_SNOW)) {
            origin = origin.down();
        }

        if (world.getBlockState(origin.down()).isIn(ParadiseLostBlockTags.BASE_REPLACEABLES)) {
            var height = random.nextInt(3) + 1;

            for (int i = 0; i <= height; i++) {
                world.setBlockState(origin.up(i), ParadiseLostBlocks.ICESTONE.getDefaultState(), Block.NOTIFY_LISTENERS);
            }

            for (Direction dir : Direction.values()) {
                if (dir.getHorizontal() >= 0) {

                    Collections.shuffle(secStates);

                    var state = secStates.get(0);
                    var offset = origin.offset(dir);

                    if (world.getBlockState(offset).getMaterial().isReplaceable()) {

                        if (world.getBlockState(offset.down()).isAir()) {
                            world.setBlockState(offset.down(), state, Block.NOTIFY_LISTENERS);

                            if (world.getBlockState(offset.down(2)).isAir()) {
                                world.setBlockState(offset.down(2), state, Block.NOTIFY_LISTENERS);
                            }
                        }

                        int secHeight = random.nextInt(height);
                        for (int i = 0; i <= secHeight - random.nextInt(2); i++) {
                            world.setBlockState(offset.up(i), state, Block.NOTIFY_LISTENERS);
                        }

                        if (secHeight > 0 && random.nextBoolean()) {
                            for (Direction secDir : Direction.values()) {
                                if (secDir.getHorizontal() >= 0 && secDir.getAxis() != dir.getAxis()) {

                                    var secState = random.nextBoolean() ? Blocks.SNOW_BLOCK.getDefaultState() : ParadiseLostBlocks.COBBLED_HOLYSTONE.getDefaultState();
                                    var secOffset = offset.offset(secDir);

                                    if (world.getBlockState(secOffset).getMaterial().isReplaceable()) {

                                        if (world.getBlockState(secOffset.down()).isAir()) {
                                            world.setBlockState(secOffset.down(), state, Block.NOTIFY_LISTENERS);
                                        }

                                        int tertHeight = random.nextInt(secHeight + random.nextInt(2));
                                        for (int i = 0; i <= tertHeight; i++) {
                                            world.setBlockState(secOffset.up(i), secState, Block.NOTIFY_LISTENERS);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    static {
        secStates.add(ParadiseLostBlocks.ICESTONE.getDefaultState());
        secStates.add(ParadiseLostBlocks.COBBLED_HOLYSTONE.getDefaultState());
        secStates.add(ParadiseLostBlocks.HOLYSTONE.getDefaultState());
    }
}
