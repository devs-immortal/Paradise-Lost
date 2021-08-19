package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.client.rendering.particle.AetherParticles;
import com.aether.entities.hostile.TransformableSwetEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class  AetherLeavesBlock extends LeavesBlock {

    protected int speed = 0;
    protected final boolean collidable;

    public AetherLeavesBlock(Settings settings, boolean collidable) {
        super(settings);
        this.collidable = collidable;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!collidable && entity instanceof LivingEntity) {
            entity.fallDistance = 0;
            entity.slowMovement(state, new Vec3d(0.99D, 0.9D, 0.99D));
        }
        if (this == AetherBlocks.GOLDEN_OAK_LEAVES && entity instanceof TransformableSwetEntity swet) {
            swet.suggestTypeChange(state);
        }
    }

    public static BlockState getHanger(BlockState state) {
        if(state.isOf(AetherBlocks.ROSE_WISTERIA_LEAVES))
            return AetherBlocks.ROSE_WISTERIA_HANGER.getDefaultState();
        else if(state.isOf(AetherBlocks.LAVENDER_WISTERIA_LEAVES))
            return AetherBlocks.LAVENDER_WISTERIA_HANGER.getDefaultState();
        else if(state.isOf(AetherBlocks.FROST_WISTERIA_LEAVES))
            return AetherBlocks.FROST_WISTERIA_HANGER.getDefaultState();
        else if(state.isOf(AetherBlocks.BOREAL_WISTERIA_LEAVES))
            return AetherBlocks.BOREAL_WISTERIA_HANGER.getDefaultState();
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

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 0.2F;
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }
}