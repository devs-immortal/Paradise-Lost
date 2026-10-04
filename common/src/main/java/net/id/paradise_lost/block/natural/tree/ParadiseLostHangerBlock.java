package net.id.paradise_lost.block.natural.tree;

import net.minecraft.world.level.ScheduledTickAccess;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ParadiseLostHangerBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<ParadiseLostHangerBlock> CODEC = simpleCodec(ParadiseLostHangerBlock::new);
    public static final BooleanProperty TIP = BooleanProperty.create("tip");
    protected static final VoxelShape FULL_SHAPE;
    protected static final VoxelShape TIP_SHAPE;

    static {
        FULL_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        TIP_SHAPE = Block.box(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    }

    public ParadiseLostHangerBlock(Properties settings) {
        super(settings.offsetType(OffsetType.XZ));
        this.registerDefaultState((this.stateDefinition.any()).setValue(TIP, true));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.makeStuckInBlock(state, new Vec3(0.800000011920929D, 0.75D, 0.800000011920929D));
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState ceiling, BlockGetter world, BlockPos pos) {
        return ceiling.getBlock() instanceof ParadiseLostHangerBlock || ceiling.getBlock() instanceof LeavesBlock || ceiling.isFaceSturdy(world, pos, Direction.DOWN);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.mayPlaceOn(world.getBlockState(pos.above()), world, pos.above());
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos posFrom, BlockState newState, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        } else if (!(world.getBlockState(pos.below()).getBlock() instanceof ParadiseLostHangerBlock)) {
            return this.defaultBlockState().setValue(TIP, true);
        } else {
            return this.defaultBlockState().setValue(TIP, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

        builder.add(TIP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(TIP)) {
            return TIP_SHAPE;
        } else {
            return FULL_SHAPE;
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.below()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.below()).isAir();
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(TIP, false));
        world.setBlockAndUpdate(pos.below(), this.defaultBlockState());
    }
}
