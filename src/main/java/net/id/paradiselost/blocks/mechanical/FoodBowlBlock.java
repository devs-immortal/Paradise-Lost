package net.id.paradiselost.blocks.mechanical;

import net.id.paradiselost.blocks.blockentity.FoodBowlBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FoodBowlBlock extends ParadiseLostBlockWithEntity {

    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    public static final BooleanProperty FULL = BooleanProperty.of("full");
    private final VoxelShape shapeZ = Block.createCuboidShape(0, 0, 1, 16, 8, 15);
    private final VoxelShape shapeX = Block.createCuboidShape(1, 0, 0, 15, 8, 16);

    public FoodBowlBlock(Settings settings) {
        super(settings, false);
        setDefaultState(getDefaultState().with(AXIS, Direction.Axis.X).with(FULL, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking() && world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
            return ActionResult.success(foodBowl.handleUse(player, hand, player.getStackInHand(hand)) && world.isClient());
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(AXIS, ctx.getPlayerFacing().getAxis());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
        case X -> shapeX;
        case Z -> shapeZ;
        default -> throw new IllegalStateException("Unexpected value: " + state.get(AXIS));
        };
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FoodBowlBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS, FULL);
    }
}
