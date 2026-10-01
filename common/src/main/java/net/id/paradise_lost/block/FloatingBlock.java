package net.id.paradise_lost.block;

import net.id.paradise_lost.api.FloatingBlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class FloatingBlock extends DropExperienceBlock {
    private final boolean powered;

    public FloatingBlock(boolean powered, Properties properties, UniformInt experienceDropped) {
        super(experienceDropped, properties);
        this.powered = powered;
    }

    public FloatingBlock(boolean powered, Properties properties) {
        this(powered, properties, UniformInt.of(0, 0));
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.scheduleTick(posIn, this, this.getFallDelay());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facingIn, BlockState facingState, LevelAccessor worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.scheduleTick(posIn, this, this.getFallDelay());
        return super.updateShape(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        checkFloatable(world, pos);
    }

    @Override
    public void tick(BlockState stateIn, ServerLevel worldIn, BlockPos posIn, RandomSource randIn) {
        this.checkFloatable(worldIn, posIn);
    }

    private void checkFloatable(Level worldIn, BlockPos pos) {
        if (!this.powered || worldIn.hasNeighborSignal(pos)) {
            if (!worldIn.isClientSide) {
                FloatingBlockHelper.ANY.tryCreate(worldIn, pos);
            }
        }
    }

    protected int getFallDelay() {
        return 2;
    }
}
