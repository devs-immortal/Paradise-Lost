package net.id.paradise_lost.block.natural.tree;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ParadiseLostLeavesBlock extends LeavesBlock implements BonemealableBlock {

    protected int speed = 0;

    public ParadiseLostLeavesBlock(Properties settings) {
        super(settings);
    }

    public static BlockState getHanger(BlockState state) {
        if (state.is(BlockRegistry.ROSE_WISTERIA_LEAVES.get())) {
            return BlockRegistry.ROSE_WISTERIA_HANGER.get().defaultBlockState();
        } else if (state.is(BlockRegistry.LAVENDER_WISTERIA_LEAVES.get())) {
            return BlockRegistry.LAVENDER_WISTERIA_HANGER.get().defaultBlockState();
        } else if (state.is(BlockRegistry.FROST_WISTERIA_LEAVES.get())) {
            return BlockRegistry.FROST_WISTERIA_HANGER.get().defaultBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.is(BlockRegistry.MOTHER_AUREL_WOODSTUFF.leaves().get()) && random.nextInt(75) == 0) {
            Direction direction = Direction.DOWN;
            BlockPos blockPos = pos.relative(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!(!blockState.isFaceSturdy(world, blockPos, direction.getOpposite()) && !blockState.propagatesSkylightDown())) {

                if (speed == 0 || world.getGameTime() % 3000 == 0) {
                    speed = world.getRandom().nextInt(4);
                    if (world.isRaining()) {
                        speed += 1;
                    } else if (world.isThundering()) {
                        speed += 2;
                    }
                }

                for (int leaf = 0; leaf < 9; leaf++) {
                    if (world.random.nextInt(3) == 0) {
                        double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepX() * 0.6D;
                        double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepZ() * 0.6D;
                        world.addParticle(ParadiseLostParticleTypes.MOTHER_AUREL_LEAF, (double) pos.getX() + d, pos.getY(), (double) pos.getZ() + f, speed, world.getRandom().nextDouble() / -20.0, 0);
                    }
                }
            }
        }
        super.animateTick(state, world, pos, random);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 0.2F;
    }

    @Override
    public int getLightBlock(BlockState state) {
        return 1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.block();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return !getHanger(state).isAir() && world.getBlockState(pos.below()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return !getHanger(state).isAir() && world.getBlockState(pos.below()).isAir();
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        world.setBlockAndUpdate(pos.below(), getHanger(state));
    }
}
