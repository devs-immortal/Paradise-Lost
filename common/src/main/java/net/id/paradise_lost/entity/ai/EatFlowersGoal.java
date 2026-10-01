package net.id.paradise_lost.entity.ai;

import net.id.paradise_lost.entity.passive.PopomEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

public class EatFlowersGoal extends MoveToBlockGoal {
    private final PopomEntity goober;
    private final Level world;
    private int timer;
    private boolean eating;

    public EatFlowersGoal(PopomEntity goober, double speed) {
        super(goober, speed, 8, 3);
        this.goober = goober;
        this.world = goober.level();
        this.eating = false;
    }

    @Override
    public boolean canUse() {
        return this.goober.getFurSize() < 3 && !this.goober.isBaby() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.timer = -1;
    }

    @Override
    public void stop() {
        super.stop();
        this.eating = false;
    }

    @Override
    public double acceptedDistance() {
        return 1.6;
    }

    @Override
    public void tick() {
        super.tick();
        if (timer > 0) this.timer = Math.max(0, this.timer - 1);
        if (this.timer == this.adjustedTickDelay(0) && this.eating) {
            if (this.world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                this.world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos.above(), Block.getId(world.getBlockState(blockPos.above())));
                this.world.setBlock(blockPos.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
            this.goober.eat();
            this.stop();
        } else if (this.isReachedTarget() && !this.eating && this.timer == -1) {
            this.world.broadcastEntityEvent(this.mob, EntityEvent.EAT_GRASS);
            this.timer = this.adjustedTickDelay(40);
            this.eating = true;
            goober.lookAt(EntityAnchorArgument.Anchor.EYES, blockPos.above().getCenter());
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.above());
        return !blockState.is(Blocks.WITHER_ROSE) && blockState.is(BlockTags.SMALL_FLOWERS);
    }
}
