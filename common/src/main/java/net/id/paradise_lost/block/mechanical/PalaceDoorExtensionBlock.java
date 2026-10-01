package net.id.paradise_lost.block.mechanical;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PalaceDoorExtensionBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final VoxelShape shapeZ = Block.box(0, 0, 6, 16, 16, 10);
    private final VoxelShape shapeX = Block.box(6, 0, 0, 10, 16, 16);

    public PalaceDoorExtensionBlock(Properties settings) {
        super(settings);
    }

    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = -6; y <= 6; y++) {
                    var blockAt = world.getBlockState(pos.offset(x, y, z)).getBlock();
                    if (blockAt instanceof PalaceDoorBlock || blockAt instanceof PalaceDoorExtensionBlock) {
                        world.setBlockAndUpdate(pos.offset(x, y, z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -2; y <= 4; y++) {
                        var blockAt = world.getBlockState(pos.offset(x, y, z));
                        if (blockAt.getBlock() instanceof PalaceDoorBlock palaceDoorBlock) {
                            palaceDoorBlock.useItemOn(stack, blockAt, world, pos.offset(x, y, z), player, hand, hit);
                        }
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> shapeZ;
            case SOUTH -> shapeZ;
            case EAST -> shapeX;
            case WEST -> shapeX;
            default -> throw new IllegalStateException("Unexpected value: " + state.getValue(FACING));
        };
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
