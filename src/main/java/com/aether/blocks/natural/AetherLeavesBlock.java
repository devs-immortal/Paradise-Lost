package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.client.rendering.particle.AetherParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class AetherLeavesBlock extends LeavesBlock {

    private int speed = 0;

    public AetherLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((state.isOf(AetherBlocks.GOLDEN_OAK_LEAVES) || state.isOf(AetherBlocks.FROST_WISTERIA_LEAVES) || state.isOf(AetherBlocks.ROSE_WISTERIA_LEAVES) || state.isOf(AetherBlocks.LAVENDER_WISTERIA_LEAVES))) {
            if(!(Boolean) state.get(PERSISTENT) && state.get(DISTANCE) >= 13) {
                dropStacks(state, world, pos);
                world.removeBlock(pos, false);
            }
        }
        else
            super.randomTick(state, world, pos, random);
    }

    public static BlockState getHanger(Block block) {
        if(block.is(AetherBlocks.ROSE_WISTERIA_LEAVES))
            return AetherBlocks.ROSE_WISTERIA_HANGER.getDefaultState();
        else if(block.is(AetherBlocks.LAVENDER_WISTERIA_LEAVES))
            return AetherBlocks.LAVENDER_WISTERIA_HANGER.getDefaultState();
        else if(block.is(AetherBlocks.FROST_WISTERIA_LEAVES))
            return AetherBlocks.FROST_WISTERIA_HANGER.getDefaultState();
        return Blocks.AIR.getDefaultState();
    }

    public static BlockState getHangerTip(Block block) {
        if(block.is(AetherBlocks.ROSE_WISTERIA_LEAVES))
            return AetherBlocks.ROSE_WISTERIA_HANGER_PLANT.getDefaultState();
        else if(block.is(AetherBlocks.LAVENDER_WISTERIA_LEAVES))
            return AetherBlocks.LAVENDER_WISTERIA_HANGER_PLANT.getDefaultState();
        else if(block.is(AetherBlocks.FROST_WISTERIA_LEAVES))
            return AetherBlocks.FROST_WISTERIA_HANGER_PLANT.getDefaultState();
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.isOf(AetherBlocks.GOLDEN_OAK_LEAVES) && random.nextInt(75) == 0) {
            Direction direction = Direction.DOWN;
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!(!blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite()) && !blockState.isTranslucent(world, blockPos))) {

                if (speed == 0 || world.getTime() % 3000 == 0) {
                    speed = world.getRandom().nextInt(4);
                    if (world.isRaining()) speed += 1;
                    else if (world.isThundering()) speed += 2;
                }

                for (int leaf = 0; leaf < 9; leaf++) {
                    if (world.random.nextInt(3) == 0) {
                        double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
                        double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
                        world.addParticle(AetherParticles.GOLDEN_OAK_LEAF, (double) pos.getX() + d, pos.getY(), (double) pos.getZ() + f, speed, world.getRandom().nextDouble() / -20.0, 0);
                    }
                }
            }
        }
        super.randomDisplayTick(state, world, pos, random);
    }
}