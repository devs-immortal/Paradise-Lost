package net.id.paradiselost.entities.ai;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

public class EatParadiseLostGrassGoal extends Goal {
    private static final Predicate<BlockState> grass = BlockStatePredicate.forBlock(ParadiseLostBlocks.GRASS_BLOCK);

    private final MobEntity owner;
    private final World world;
    private int timer;

    public EatParadiseLostGrassGoal(MobEntity entity) {
        owner = entity;
        world = entity.world;

        setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
    }

    @Override
    public boolean canStart() {
        if (owner.getRandom().nextInt(owner.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos pos = new BlockPos(owner.getBlockPos());

            if (grass.test(world.getBlockState(pos))) {
                return true;
            } else {
                return world.getBlockState(pos.down()).getBlock() == ParadiseLostBlocks.GRASS_BLOCK;
            }
        }
    }

    @Override
    public void start() {
        timer = 40;
        world.sendEntityStatus(owner, (byte) 10);
        owner.getNavigation().stop();
    }

    @Override
    public void stop() {
        timer = 0;
    }

    @Override
    public boolean shouldContinue() {
        return timer > 0;
    }

    @Override
    public void tick() {
        timer = Math.max(0, timer - 1);

        if (timer == 4) {
            BlockPos pos = new BlockPos(owner.getBlockPos());

            if (grass.test(world.getBlockState(pos))) {
                if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    world.breakBlock(pos, false);
                }

                owner.onEatingGrass();
            } else {
                BlockPos downPos = pos.down();

                if (world.getBlockState(downPos).getBlock() == ParadiseLostBlocks.GRASS_BLOCK) {
                    if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                        world.syncGlobalEvent(2001, downPos, Block.getRawIdFromState(ParadiseLostBlocks.GRASS_BLOCK.getDefaultState()));
                        world.setBlockState(downPos, ParadiseLostBlocks.DIRT.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }

                    owner.onEatingGrass();
                }
            }
        }
    }

    public int getTimer() {
        return timer;
    }
}
