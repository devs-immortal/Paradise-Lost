package com.aether.blocks;

import com.aether.entities.block.FloatingBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class FloatingBlock extends Block {
    public static boolean floatInstantly;
    private boolean constantlyPowered;

    public FloatingBlock(FabricBlockSettings properties, boolean constantlyPowered) {
        super(properties.ticksRandomly());
        this.constantlyPowered = constantlyPowered;
    }

    public static boolean canFloatThrough(BlockState state) {
        return state.isAir() || state.getBlock() == Blocks.FIRE || !state.getFluidState().isEmpty();
    }

    @Override
    public void onPlaced(World worldIn, BlockPos posIn, BlockState stateIn, LivingEntity entityIn, ItemStack itemIn) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getTickRate(worldIn));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState neighborIn, WorldAccess worldIn, BlockPos posIn, BlockPos neighborPosIn) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getTickRate(worldIn));
        // TODO: VERIFY
        return super.getStateForNeighborUpdate(stateIn, facingIn, neighborIn, worldIn, posIn, neighborPosIn);
    }

    @Override
    public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
        if (!worldIn.isClient) {
            if (this.constantlyPowered || worldIn.isReceivingRedstonePower(posIn)) {
                this.checkFloatable(worldIn, posIn);
            }
        }
    }

    private void checkFloatable(World worldIn, BlockPos posIn) {
        if (canFloatThrough(worldIn.getBlockState(posIn.up())) && posIn.getY() <= worldIn.getHeight()) {
            if (!floatInstantly && worldIn.isRegionLoaded(posIn.add(-32, -32, -32), posIn.add(32, 32, 32))) {
                if (!worldIn.isClient) {
                    FloatingBlockEntity floatingBlock = new FloatingBlockEntity(worldIn, (double) posIn.getX() + 0.5D, posIn.getY(), (double) posIn.getZ() + 0.5D, worldIn.getBlockState(posIn));
                    this.onStartFloating(floatingBlock);
                    worldIn.spawnEntity(floatingBlock);
                }
            } else {
                if (worldIn.getBlockState(posIn).getBlock() == this) {
                    worldIn.isAir(posIn);
                }
                BlockPos blockpos;
                for (blockpos = posIn.up(); canFloatThrough(worldIn.getBlockState(blockpos)) && blockpos.getY() <= worldIn.getHeight(); blockpos = blockpos.up()) {
                }
                if (blockpos.getY() < worldIn.getHeight()) {
                    worldIn.setBlockState(blockpos.up(), this.getDefaultState());
                }
            }
        }
    }

    public void onStartFloating(FloatingBlockEntity entityIn) {
    }

    public void onStopFloating(World worldIn, BlockPos posIn, BlockState stateIn, BlockState ceilingStateIn) {
    }

    public int getTickRate(WorldView worldIn) {
        return 2;
    }
}


