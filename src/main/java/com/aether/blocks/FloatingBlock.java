package com.aether.blocks;

import com.aether.entities.block.FloatingBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class FloatingBlock extends FallingBlock {
    private final boolean powered;

    public FloatingBlock(boolean powered, FabricBlockSettings properties) {
        super(properties);
        this.powered = powered;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState facingState, WorldAccess worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
        return super.getStateForNeighborUpdate(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    @Override
    public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
        this.checkFloatable(worldIn, posIn);
    }

    private void checkFloatable(World worldIn, BlockPos pos) {
        if ((worldIn.isAir(pos.up()) || canFallThrough(worldIn.getBlockState(pos.up()))) && (!this.powered || worldIn.isReceivingRedstonePower(pos))) {
            if (!worldIn.isClient) {
                FloatingBlockEntity floatingblockentity = new FloatingBlockEntity(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                this.onStartFloating(floatingblockentity);
                worldIn.spawnEntity(floatingblockentity);
            }
        }
    }

    protected void onStartFloating(FloatingBlockEntity entityIn) {
    }

    public void onEndFloating(World worldIn, BlockPos posIn, BlockState floatingState, BlockState hitState) {
    }

    public void onBroken(World worldIn, BlockPos pos) {
    }

    @Override
    protected int getFallDelay() {
        return 2;
    }
}