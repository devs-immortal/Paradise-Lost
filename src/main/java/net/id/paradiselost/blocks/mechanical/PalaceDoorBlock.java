package net.id.paradiselost.blocks.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.blockentity.PalaceDoorBlockEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PalaceDoorBlock extends BlockWithEntity {

    public static final MapCodec<PalaceDoorBlock> CODEC = createCodec(PalaceDoorBlock::new);

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;

    private final VoxelShape shapeZ = Block.createCuboidShape(0, 0, 6, 16, 16, 10);
    private final VoxelShape shapeX = Block.createCuboidShape(6, 0, 0, 10, 16, 16);

    public PalaceDoorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    private void clearExtensionBlocks(World world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -4; y <= 2; y++) {
                    var blockAt = world.getBlockState(pos.add(x, y, z)).getBlock();
                    if (blockAt instanceof PalaceDoorExtensionBlock) {
                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        clearExtensionBlocks(world, pos);
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking() && stack.isOf(ParadiseLostItems.PALACE_KEY) && world.getBlockEntity(pos) instanceof PalaceDoorBlockEntity be && !be.isOpen()) {
            stack.decrementUnlessCreative(1, player);
            be.open();
            world.setBlockState(pos, state.with(OPEN, true));
            clearExtensionBlocks(world, pos);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    public static Direction getRotation(BlockState state) {
        if (state.getBlock() != ParadiseLostBlocks.PALACE_DOOR) {
            return Direction.NORTH;
        }
        return state.get(FACING);
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
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(OPEN) ? VoxelShapes.empty() : state.getOutlineShape(world, pos);
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
        builder.add(FACING, OPEN);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PalaceDoorBlockEntity(pos, state);
    }
}
