package com.aether.entities.ai;

import com.aether.blocks.AetherBlocks;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class EatAetherGrassGoal extends Goal {
    private static final Predicate<BlockState> grass = BlockStatePredicate.forBlock(AetherBlocks.AETHER_GRASS_BLOCK);

    private final Mob owner;
    private final Level world;
    private int timer;

    public EatAetherGrassGoal(Mob entity) {
        this.owner = entity;
        this.world = entity.level;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.owner.getRandom().nextInt(this.owner.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos pos = new BlockPos(this.owner.getX(), this.owner.getY(), this.owner.getZ());

            if (grass.test(this.world.getBlockState(pos))) return true;
            else return this.world.getBlockState(pos.below(1)).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK;
        }
    }

    @Override
    public void start() {
        this.timer = 40;
        this.world.broadcastEntityEvent(this.owner, (byte) 10);
        this.owner.getNavigation().stop();
    }

    // TODO: ???
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
            BlockPos pos = new BlockPos(this.owner.getX(), this.owner.getY(), this.owner.getZ());

            if (grass.test(this.world.getBlockState(pos))) {
                if (this.world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) this.world.destroyBlock(pos, false);

                this.owner.ate();
            } else {
                BlockPos downPos = pos.below(1);

                if (this.world.getBlockState(downPos).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK) {
                    if (this.world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.world.globalLevelEvent(2001, downPos, Block.getId(AetherBlocks.AETHER_GRASS_BLOCK.defaultBlockState()));
                        this.world.setBlock(downPos, AetherBlocks.AETHER_DIRT.defaultBlockState(), 2);
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