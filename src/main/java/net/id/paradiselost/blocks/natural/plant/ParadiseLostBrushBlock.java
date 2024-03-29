package net.id.paradiselost.blocks.natural.plant;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

public class ParadiseLostBrushBlock extends FernBlock {

    private final TagKey<Block> validFloors;
    private final boolean override;

    public ParadiseLostBrushBlock(Settings settings) {
        this(settings.offsetType(OffsetType.XZ), ParadiseLostBlockTags.GENERIC_VALID_GROUND, false);
    }

    public ParadiseLostBrushBlock(Settings settings, TagKey<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (this == ParadiseLostBlocks.GRASS) {
            TallPlantBlock tallPlantBlock = ParadiseLostBlocks.TALL_GRASS;
            BlockState blockState = tallPlantBlock.getDefaultState();
            if (blockState.canPlaceAt(world, pos) && world.isAir(pos.up())) {
                TallPlantBlock.placeAt(world, blockState, pos, 2);
            }
        }
        Iterable<BlockPos> growPos = BlockPos.iterate(pos.add(-5, 3, -5), pos.add(5, -3, 5));
        growPos.forEach(target -> {
            if (world.isAir(target) && canPlaceAt(state, world, target) && random.nextInt(target.getManhattanDistance(pos) + 1) == 0) {
                world.setBlockState(target, state);
            }
        });
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if (override) {
            return floor.isIn(validFloors);
        }
        return (super.canPlantOnTop(floor, world, pos) || floor.isIn(validFloors)) && floor.isSideSolidFullSquare(world, pos, Direction.UP);
    }
}
