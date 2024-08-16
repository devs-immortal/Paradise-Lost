package net.id.paradiselost.blocks;

import net.id.paradiselost.api.FloatingBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

@SuppressWarnings("deprecation")
public class FloatingBlock extends ExperienceDroppingBlock {
    private final boolean powered;

    public FloatingBlock(boolean powered, Settings properties, UniformIntProvider experienceDropped) {
        super(properties, experienceDropped);
        this.powered = powered;
    }

    public FloatingBlock(boolean powered, Settings properties) {
        this(powered, properties, UniformIntProvider.create(0, 0));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.scheduleBlockTick(posIn, this, this.getFallDelay());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState facingState, WorldAccess worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.scheduleBlockTick(posIn, this, this.getFallDelay());
        return super.getStateForNeighborUpdate(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        checkFloatable(world, pos);
    }

    @Override
    public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
        this.checkFloatable(worldIn, posIn);
    }

    private void checkFloatable(World worldIn, BlockPos pos) {
        if (!this.powered || worldIn.isReceivingRedstonePower(pos)) {
            if (!worldIn.isClient) {
                FloatingBlockHelper.ANY.tryCreate(worldIn, pos);
                System.out.println("creating float block?");
            }
        }
    }

    protected int getFallDelay() {
        return 2;
    }
}
