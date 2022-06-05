package net.id.aether.blocks.natural.plant;

import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class AetherTallBrushBlock extends TallPlantBlock {

    private final TagKey<Block> validFloors;
    private final boolean override;

    public AetherTallBrushBlock(Settings settings) {
        this(settings, AetherBlockTags.GENERIC_VALID_GROUND, false);
    }

    public AetherTallBrushBlock(Settings settings, TagKey<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if (override)
            return floor.isIn(validFloors);
        return (super.canPlantOnTop(floor, world, pos) || floor.isIn(validFloors)) && floor.isSideSolidFullSquare(world, pos, Direction.UP);
    }
}
