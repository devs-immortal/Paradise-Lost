package com.aether.blocks;

import com.aether.entities.block.FloatingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import java.util.Random;

@SuppressWarnings("deprecation")
public class FloatingBlock extends OreBlock {
    private final boolean powered;

    public FloatingBlock(boolean powered, FabricBlockSettings properties, UniformInt experienceDropped) {
        super(properties, experienceDropped);
        this.powered = powered;
    }

    public FloatingBlock(boolean powered, FabricBlockSettings properties) {
        this(powered, properties, UniformInt.of(0, 0));
    }

    public void onPlace(BlockState state, Level worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.getBlockTicks().scheduleTick(posIn, this, this.getFallDelay());
    }

    public BlockState updateShape(BlockState stateIn, Direction facingIn, BlockState facingState, LevelAccessor worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.getBlockTicks().scheduleTick(posIn, this, this.getFallDelay());
        return super.updateShape(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    public void tick(BlockState stateIn, ServerLevel worldIn, BlockPos posIn, Random randIn) {
        this.checkFloatable(worldIn, posIn);
    }

    private void checkFloatable(Level worldIn, BlockPos pos) {
        if ((worldIn.isEmptyBlock(pos.above()) || canFallThrough(worldIn.getBlockState(pos.above()))) && (!this.powered || worldIn.hasNeighborSignal(pos))) {
            if (!worldIn.isClientSide) {
                FloatingBlockEntity floatingblockentity = new FloatingBlockEntity(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                //this.onStartFloating(floatingblockentity);
                worldIn.addFreshEntity(floatingblockentity);
            }
        }
    }

//    protected void onStartFloating(FloatingBlockEntity entityIn) {
//        entityIn.dropItem = false;
//    }

    public void onEndFloating(Level worldIn, BlockPos posIn, BlockState floatingState, BlockState hitState) {
    }

    public void onBroken(Level worldIn, BlockPos pos) {
    }

    protected int getFallDelay() {
        return 2;
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
    }

    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    @Environment(EnvType.CLIENT)
    public int getColor(BlockState state, BlockGetter world, BlockPos pos) {
        return -16777216;
    }
}