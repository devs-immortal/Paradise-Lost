package com.aether.blocks.natural;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class AetherHangerBlock extends PlantBlock {
    public static final BooleanProperty TIP = BooleanProperty.of("tip");
    protected static final VoxelShape FULL_SHAPE;
    protected static final VoxelShape TIP_SHAPE;

    public AetherHangerBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(TIP, true));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.slowMovement(state, new Vec3d(0.800000011920929D, 0.75D, 0.800000011920929D));
        }
    }

    @Override
    protected boolean canPlantOnTop(BlockState ceiling, BlockView world, BlockPos pos) {
        return ceiling.getBlock() instanceof AetherHangerBlock || ceiling.getBlock() instanceof LeavesBlock || ceiling.isSideSolidFullSquare(world, pos, Direction.DOWN);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.canPlantOnTop(world.getBlockState(pos.up()), world, pos.up());
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else if (!(world.getBlockState(pos.down()).getBlock() instanceof AetherHangerBlock)) {
            return this.getDefaultState().with(TIP, true);
        } else {
            return this.getDefaultState().with(TIP, false);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TIP);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(TIP)) {
            return TIP_SHAPE;
        } else {
            return FULL_SHAPE;
        }
    }

    static {
        FULL_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        TIP_SHAPE = Block.createCuboidShape(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    }
}
