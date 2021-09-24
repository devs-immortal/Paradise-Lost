package net.id.aether.blocks.natural.plant;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class GiantLilypadBlock extends LilyPadBlock implements Fertilizable {

    public static final BooleanProperty FLOWERING = BooleanProperty.of("flowering");

    // TODO: this collision shape doesn't work. see github issues page. Change to something else
    private static final VoxelShape SHAPE = Block.createCuboidShape(-8.0D, 0.0D, -8.0D, 24.0D, 1.5D, 24.0D);

    public GiantLilypadBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FLOWERING, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return super.getRaycastShape(state, world, pos);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return BlockPos.stream(pos.add(-1, 0, -1), pos.add(1, 0, 1)).allMatch(floorPos -> {
            var upFluid = world.getFluidState(floorPos.up());
            var fluid = world.getFluidState(floorPos);
            return (fluid.getFluid() == Fluids.WATER || world.getBlockState(floorPos).getMaterial() == Material.ICE) && upFluid.isEmpty();
        });
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FLOWERING);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if(!state.get(FLOWERING)) {
            world.setBlockState(pos, state.with(FLOWERING, true));
        }
        else {
            dropStack(world, pos, new ItemStack(this));
        }
    }
}
