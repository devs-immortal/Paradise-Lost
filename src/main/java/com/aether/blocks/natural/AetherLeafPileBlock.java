package com.aether.blocks.natural;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AetherLeafPileBlock extends FallingBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public AetherLeafPileBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (isFree(world.getBlockState(pos.below()))) {
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, world.getBlockState(pos));
            this.falling(fallingBlockEntity);
            world.addFreshEntity(fallingBlockEntity);
        }
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
