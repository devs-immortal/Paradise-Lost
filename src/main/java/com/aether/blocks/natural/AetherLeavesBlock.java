package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.client.rendering.particle.AetherParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class  AetherLeavesBlock extends LeavesBlock {

    protected int speed = 0;
    protected final boolean collidable;

    public AetherLeavesBlock(Properties settings, boolean collidable) {
        super(settings);
        this.collidable = collidable;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if ((state.is(AetherBlocks.GOLDEN_OAK_LEAVES) || state.is(AetherBlocks.FROST_WISTERIA_LEAVES) || state.is(AetherBlocks.BOREAL_WISTERIA_LEAVES) || state.is(AetherBlocks.ROSE_WISTERIA_LEAVES) || state.is(AetherBlocks.LAVENDER_WISTERIA_LEAVES))) {
            if(!(Boolean) state.getValue(PERSISTENT) && state.getValue(DISTANCE) >= 13) {
                dropResources(state, world, pos);
                world.removeBlock(pos, false);
            }
        }
        else
            super.randomTick(state, world, pos, random);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!hasCollision && entity instanceof LivingEntity) {
            entity.fallDistance = 0;
            entity.makeStuckInBlock(state, new Vec3(0.99D, 0.9D, 0.99D));
        }
    }

    public static BlockState getHanger(BlockState state) {
        if(state.is(AetherBlocks.ROSE_WISTERIA_LEAVES))
            return AetherBlocks.ROSE_WISTERIA_HANGER.defaultBlockState();
        else if(state.is(AetherBlocks.LAVENDER_WISTERIA_LEAVES))
            return AetherBlocks.LAVENDER_WISTERIA_HANGER.defaultBlockState();
        else if(state.is(AetherBlocks.FROST_WISTERIA_LEAVES))
            return AetherBlocks.FROST_WISTERIA_HANGER.defaultBlockState();
        else if(state.is(AetherBlocks.BOREAL_WISTERIA_LEAVES))
            return AetherBlocks.BOREAL_WISTERIA_HANGER.defaultBlockState();
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (state.is(AetherBlocks.GOLDEN_OAK_LEAVES) && random.nextInt(75) == 0) {
            Direction direction = Direction.DOWN;
            BlockPos blockPos = pos.relative(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!(!blockState.isFaceSturdy(world, blockPos, direction.getOpposite()) && !blockState.propagatesSkylightDown(world, blockPos))) {

                if (speed == 0 || world.getGameTime() % 3000 == 0) {
                    speed = world.getRandom().nextInt(4);
                    if (world.isRaining()) speed += 1;
                    else if (world.isThundering()) speed += 2;
                }

                for (int leaf = 0; leaf < 9; leaf++) {
                    if (world.random.nextInt(3) == 0) {
                        double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepX() * 0.6D;
                        double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepZ() * 0.6D;
                        world.addParticle(AetherParticles.GOLDEN_OAK_LEAF, (double) pos.getX() + d, pos.getY(), (double) pos.getZ() + f, speed, world.getRandom().nextDouble() / -20.0, 0);
                    }
                }
            }
        }
        super.animateTick(state, world, pos, random);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 0.2F;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return 1;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }
}