package net.id.paradise_lost.block.natural.plant;

import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ParadiseLostTallBrushBlock extends DoublePlantBlock {

    private final TagKey<Block> validFloors;
    private final boolean override;

    public ParadiseLostTallBrushBlock(Properties settings) {
        this(settings, ParadiseLostBlockTags.GENERIC_VALID_GROUND, false);
    }

    public ParadiseLostTallBrushBlock(Properties settings, TagKey<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        if (override) {
            return floor.is(validFloors);
        }
        return (super.mayPlaceOn(floor, world, pos) || floor.is(validFloors)) && floor.isFaceSturdy(world, pos, Direction.UP);
    }
}
