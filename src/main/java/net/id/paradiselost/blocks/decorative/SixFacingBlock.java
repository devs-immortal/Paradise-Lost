package net.id.paradiselost.blocks.decorative;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class SixFacingBlock extends Block {
    public static final DirectionProperty FACING = Properties.FACING;
    
    public SixFacingBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }
    
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(FACING)) {
                case NORTH -> state.with(FACING, Direction.EAST);
                case EAST -> state.with(FACING, Direction.SOUTH);
                case SOUTH -> state.with(FACING, Direction.WEST);
                case WEST -> state.with(FACING, Direction.NORTH);
                default -> state;
            };
            default -> state;
        };
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide());
    }
}
