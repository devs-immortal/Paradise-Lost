package net.id.aether.blocks.natural.plant;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.Random;

public class AetherBrushBlock extends FernBlock {

    private final Tag<Block> validFloors;
    private final boolean override;

    public AetherBrushBlock(Settings settings) {
        this(settings, AetherBlockTags.GENERIC_VALID_GROUND, false);
    }

    public AetherBrushBlock(Settings settings, Tag<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (this == AetherBlocks.AETHER_GRASS) {
            TallPlantBlock tallPlantBlock = AetherBlocks.AETHER_TALL_GRASS;
            BlockState blockState = tallPlantBlock.getDefaultState();
            if (blockState.canPlaceAt(world, pos) && world.isAir(pos.up())) {
                TallPlantBlock.placeAt(world, blockState, pos, 2);
            }
        }
        Iterable<BlockPos> growPos = BlockPos.iterate(pos.add(-5, 3, -5), pos.add(5, -3, 5));
        growPos.forEach(target -> {
            if (world.isAir(target) && canPlaceAt(state, world, target) && random.nextInt(target.getManhattanDistance(pos) + 1) == 0)
                world.setBlockState(target, state);
        });
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if (override)
            return floor.isIn(validFloors);
        return (super.canPlantOnTop(floor, world, pos) || floor.isIn(validFloors)) && floor.isSideSolidFullSquare(world, pos, Direction.UP);
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
