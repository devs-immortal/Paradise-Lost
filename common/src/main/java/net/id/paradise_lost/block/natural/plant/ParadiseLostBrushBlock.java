package net.id.paradise_lost.block.natural.plant;

import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ParadiseLostBrushBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<ParadiseLostBrushBlock> CODEC = simpleCodec(ParadiseLostBrushBlock::new);
    private final TagKey<Block> validFloors;
    private final boolean override;

    public ParadiseLostBrushBlock(Properties settings) {
        this(settings, ParadiseLostBlockTags.GENERIC_VALID_GROUND, false);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public ParadiseLostBrushBlock(Properties settings, TagKey<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (this == BlockRegistry.GRASS.get()) {
            DoublePlantBlock tallPlantBlock = BlockRegistry.TALL_GRASS.get();
            BlockState blockState = tallPlantBlock.defaultBlockState();
            if (blockState.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
                DoublePlantBlock.placeAt(world, blockState, pos, 2);
            }
        }
        Iterable<BlockPos> growPos = BlockPos.betweenClosed(pos.offset(-5, 3, -5), pos.offset(5, -3, 5));
        growPos.forEach(target -> {
            if (world.isEmptyBlock(target) && canSurvive(state, world, target) && random.nextInt(target.distManhattan(pos) + 1) == 0) {
                world.setBlockAndUpdate(target, state);
            }
        });
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        if (override) {
            return floor.is(validFloors);
        }
        return (super.mayPlaceOn(floor, world, pos) || floor.is(validFloors)) && floor.isFaceSturdy(world, pos, Direction.UP);
    }
}
