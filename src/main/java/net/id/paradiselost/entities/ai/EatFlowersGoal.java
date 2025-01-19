package net.id.paradiselost.entities.ai;

import net.id.paradiselost.entities.passive.PopomEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;

public class EatFlowersGoal extends MoveToTargetPosGoal {
    private final PopomEntity goober;
    private final World world;
    private int timer;
    private boolean eating;

    public EatFlowersGoal(PopomEntity goober, double speed) {
        super(goober, speed, 8, 3);
        this.goober = goober;
        this.world = goober.getWorld();
        this.eating = false;
    }

    @Override
    public boolean canStart() {
        return this.goober.getFurSize() < 3 && super.canStart(); //this.cat.isTamed() && !this.cat.isSitting() && super.canStart();
    }

    @Override
    public void stop() {
        super.stop();
        this.timer = 0;
        this.eating = false;
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 1.4;
    }

    public int getTimer() {
        return this.timer;
    }

    @Override
    public void tick() {
        super.tick();
        this.timer = Math.max(0, this.timer - 1);
        if (this.hasReached() && !this.eating) {
            this.timer = this.getTickCount(40);
            this.eating = true;
            this.world.sendEntityStatus(this.mob, EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART);
        }
        if (this.timer == this.getTickCount(4)) {
            BlockPos pos = null;
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    for (int y = -1; y < 2; y++) {
                        if (world.getBlockState(goober.getBlockPos().add(x, y, z)).isIn(BlockTags.SMALL_FLOWERS)) {
                            pos = goober.getBlockPos().add(x, y, z);
                        }
                    }
                }
            }
            if (pos != null) {
                if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    this.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(world.getBlockState(pos)));
                    this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                this.goober.eat();
                this.stop();
            }
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.up());
        return blockState.isIn(BlockTags.SMALL_FLOWERS);
    }
}
