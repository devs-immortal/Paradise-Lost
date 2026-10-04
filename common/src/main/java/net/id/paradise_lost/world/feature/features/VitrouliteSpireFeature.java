package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VitrouliteSpireFeature extends Feature<NoneFeatureConfiguration> {

    private static final List<BlockState> secStates = new ArrayList<>();

    public VitrouliteSpireFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var origin = context.origin();
        var world = context.level();
        var random = context.random();

        if (world.getBlockState(origin).is(Blocks.POWDER_SNOW)) {
            origin = origin.below();
        }

        if (world.getBlockState(origin.below()).is(ParadiseLostBlockTags.BASE_REPLACEABLES)) {
            var height = random.nextInt(3) + 1;

            for (int i = 0; i <= height; i++) {
                world.setBlock(origin.above(i), Blocks.PACKED_ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
            }

            for (Direction dir : Direction.values()) {
                if (dir.get2DDataValue() >= 0) {

                    Collections.shuffle(secStatesOrInit());

                    var state = secStates.get(0);
                    var offset = origin.relative(dir);

                    if (world.getBlockState(offset).canBeReplaced()) {

                        if (world.getBlockState(offset.below()).isAir()) {
                            world.setBlock(offset.below(), state, Block.UPDATE_CLIENTS);

                            if (world.getBlockState(offset.below(2)).isAir()) {
                                world.setBlock(offset.below(2), state, Block.UPDATE_CLIENTS);
                            }
                        }

                        int secHeight = random.nextInt(height);
                        for (int i = 0; i <= secHeight - random.nextInt(2); i++) {
                            world.setBlock(offset.above(i), state, Block.UPDATE_CLIENTS);
                        }

                        if (secHeight > 0 && random.nextBoolean()) {
                            for (Direction secDir : Direction.values()) {
                                if (secDir.get2DDataValue() >= 0 && secDir.getAxis() != dir.getAxis()) {

                                    var secState = random.nextBoolean() ? Blocks.SNOW_BLOCK.defaultBlockState() : BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState();
                                    var secOffset = offset.relative(secDir);

                                    if (world.getBlockState(secOffset).canBeReplaced()) {

                                        if (world.getBlockState(secOffset.below()).isAir()) {
                                            world.setBlock(secOffset.below(), state, Block.UPDATE_CLIENTS);
                                        }

                                        int tertHeight = random.nextInt(secHeight + random.nextInt(2));
                                        for (int i = 0; i <= tertHeight; i++) {
                                            world.setBlock(secOffset.above(i), secState, Block.UPDATE_CLIENTS);
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

    private static List<BlockState> secStatesOrInit() {
        if (secStates.isEmpty()) {
            secStates.add(Blocks.PACKED_ICE.defaultBlockState());
            secStates.add(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState());
            secStates.add(BlockRegistry.FLOESTONE.get().defaultBlockState());
        }
        return secStates;
    }
}
