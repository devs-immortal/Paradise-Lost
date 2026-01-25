package net.id.paradiselost.blocks.mechanical;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PalaceDoorExtensionBlock extends Block {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private final VoxelShape shapeZ = Block.createCuboidShape(0, 0, 6, 16, 16, 10);
    private final VoxelShape shapeX = Block.createCuboidShape(6, 0, 0, 10, 16, 16);

    public PalaceDoorExtensionBlock(Settings settings) {
        super(settings);
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = -6; y <= 6; y++) {
                    var blockAt = world.getBlockState(pos.add(x, y, z)).getBlock();
                    if (blockAt instanceof PalaceDoorBlock || blockAt instanceof PalaceDoorExtensionBlock) {
                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -2; y <= 4; y++) {
                        var blockAt = world.getBlockState(pos.add(x, y, z));
                        if (blockAt.getBlock() instanceof PalaceDoorBlock palaceDoorBlock) {
                            palaceDoorBlock.onUseWithItem(stack, blockAt, world, pos.add(x, y, z), player, hand, hit);
                        }
                    }
                }
            }
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> shapeZ;
            case SOUTH -> shapeZ;
            case EAST -> shapeX;
            case WEST -> shapeX;
            default -> throw new IllegalStateException("Unexpected value: " + state.get(FACING));
        };
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
