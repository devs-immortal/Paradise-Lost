package net.id.aether.blocks.natural.plant;

import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class AetherTallBrushBlock extends TallPlantBlock {

    private final Tag<Block> validFloors;
    private final boolean override;

    public AetherTallBrushBlock(Settings settings) {
        this(settings, AetherBlockTags.GENERIC_VALID_GROUND, false);
    }

    public AetherTallBrushBlock(Settings settings, Tag<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if (override)
            return validFloors.contains(floor.getBlock());
        return (super.canPlantOnTop(floor, world, pos) || validFloors.contains(floor.getBlock())) && floor.isSideSolidFullSquare(world, pos, Direction.UP);
    }
}
