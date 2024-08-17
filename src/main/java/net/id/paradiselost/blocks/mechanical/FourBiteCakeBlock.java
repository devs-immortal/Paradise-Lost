package net.id.paradiselost.blocks.mechanical;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class FourBiteCakeBlock extends Block {
    public static final IntProperty BITES = IntProperty.of("bites", 0, 3);
    protected static final VoxelShape[] BITES_TO_SHAPE = new VoxelShape[]{
        Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
        VoxelShapes.union(
            Block.createCuboidShape(8.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
            Block.createCuboidShape(1.0D, 0.0D, 8.0D, 15.0D, 8.0D, 15.0D)
        ),
        Block.createCuboidShape(8.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
        Block.createCuboidShape(8.0D, 0.0D, 8.0D, 15.0D, 8.0D, 15.0D),
    };
    
    public FourBiteCakeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(BITES, 0));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BITES_TO_SHAPE[state.get(BITES)];
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            if (tryEat(world, pos, state, player).isAccepted()) {
                return ActionResult.SUCCESS;
            }
            if (player.getStackInHand(hand).isEmpty()) {
                return ActionResult.CONSUME;
            }
        }
        return tryEat(world, pos, state, player);
    }
    
    protected static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false)) {
            return ActionResult.PASS;
        } else {
            player.incrementStat(Stats.EAT_CAKE_SLICE);
            player.getHungerManager().add(4, 0.5F);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400));
            int bites = state.get(BITES);
            world.emitGameEvent(player, GameEvent.EAT, pos);
            if (bites < 3) {
                world.setBlockState(pos, state.with(BITES, bites + 1), 3);
            } else {
                world.removeBlock(pos, false);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return ActionResult.SUCCESS;
        }
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BITES});
    }
    
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return getComparatorOutput(state.get(BITES));
    }
    
    public static int getComparatorOutput(int bites) {
        return (3 - bites) * 5;
    }
    
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
    
    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
