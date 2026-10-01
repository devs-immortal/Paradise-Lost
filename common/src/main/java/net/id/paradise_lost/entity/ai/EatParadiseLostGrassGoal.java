package net.id.paradise_lost.entity.ai;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import java.util.EnumSet;
import java.util.function.Predicate;

public class EatParadiseLostGrassGoal extends Goal {
    private static final Predicate<BlockState> grass = BlockStatePredicate.forBlock(BlockRegistry.HIGHLANDS_GRASS.get());

    private final Mob owner;
    private final Level world;
    private int timer;

    public EatParadiseLostGrassGoal(Mob entity) {
        this.owner = entity;
        this.world = entity.level();

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.owner.getRandom().nextInt(this.owner.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos pos = new BlockPos(this.owner.blockPosition());

            if (grass.test(world.getBlockState(pos))) {
                return true;
            } else {
                return world.getBlockState(pos.below()).getBlock() == BlockRegistry.HIGHLANDS_GRASS.get();
            }
        }
    }

    @Override
    public void start() {
        this.timer = 40;
        this.world.broadcastEntityEvent(this.owner, (byte) 10);
        this.owner.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.timer = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer > 0;
    }

    @Override
    public void tick() {
        this.timer = Math.max(0, this.timer - 1);

        if (this.timer == 4) {
            BlockPos pos = new BlockPos(this.owner.blockPosition());

            if (grass.test(world.getBlockState(pos))) {
                if (world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    world.destroyBlock(pos, false);
                }

                this.owner.ate();
            } else {
                BlockPos downPos = pos.below();

                if (this.world.getBlockState(downPos).getBlock() == BlockRegistry.HIGHLANDS_GRASS.get()) {
                    if (this.world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.world.globalLevelEvent(2001, downPos, Block.getId(BlockRegistry.HIGHLANDS_GRASS.get().defaultBlockState()));
                        this.world.setBlock(downPos, BlockRegistry.DIRT.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                    }

                    this.owner.ate();
                }
            }
        }
    }

    public int getTimer() {
        return this.timer;
    }
}
