package net.id.aether.blocks.decorative;

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
    public static final DirectionProperty FACING;

    public SixFacingBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch(state.get(FACING)) {
                    case NORTH:
                        return state.with(FACING, Direction.EAST);
                    case EAST:
                        return state.with(FACING, Direction.SOUTH);
                    case SOUTH:
                        return state.with(FACING, Direction.WEST);
                    case WEST:
                        return state.with(FACING, Direction.NORTH);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
    }

    static {
        FACING = Properties.FACING;
    }
}
