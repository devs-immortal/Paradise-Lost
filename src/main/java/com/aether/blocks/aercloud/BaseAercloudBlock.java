package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlockProperties;
import com.aether.blocks.AetherBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseAercloudBlock extends HalfTransparentBlock {

    private static final BooleanProperty DOUBLE_DROPS = AetherBlockProperties.DOUBLE_DROPS;
    protected static VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 0.01, 16.0);

    public BaseAercloudBlock(Properties properties) {
        super(properties.isRedstoneConductor(AetherBlocks::never).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never));
        this.registerDefaultState(this.defaultBlockState().setValue(DOUBLE_DROPS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DOUBLE_DROPS);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;

        if (entity.getDeltaMovement().y < 0.0) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.005, 1.0));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
