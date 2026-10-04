package net.id.paradise_lost.block.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.blockentity.PalaceDoorBlockEntity;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PalaceDoorBlock extends BaseEntityBlock {

    public static final MapCodec<PalaceDoorBlock> CODEC = simpleCodec(PalaceDoorBlock::new);

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private final VoxelShape shapeZ = Block.box(0, 0, 6, 16, 16, 10);
    private final VoxelShape shapeX = Block.box(6, 0, 0, 10, 16, 16);

    public PalaceDoorBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    private void clearExtensionBlocks(Level world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -4; y <= 2; y++) {
                    var blockAt = world.getBlockState(pos.offset(x, y, z)).getBlock();
                    if (blockAt instanceof PalaceDoorExtensionBlock) {
                        world.setBlockAndUpdate(pos.offset(x, y, z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        clearExtensionBlocks(world, pos);
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown() && stack.is(ItemRegistry.PALACE_KEY.get()) && world.getBlockEntity(pos) instanceof PalaceDoorBlockEntity be && !be.isOpen()) {
            stack.consume(1, player);
            be.open();
            world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
            clearExtensionBlocks(world, pos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    public static Direction getRotation(BlockState state) {
        if (state.getBlock() != BlockRegistry.PALACE_DOOR.get()) {
            return Direction.NORTH;
        }
        return state.getValue(FACING);
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : state.getShape(world, pos);
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
        builder.add(FACING, OPEN);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PalaceDoorBlockEntity(pos, state);
    }
}
