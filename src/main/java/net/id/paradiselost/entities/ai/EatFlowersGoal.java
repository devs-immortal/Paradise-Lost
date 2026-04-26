package net.id.paradiselost.entities.ai;

import net.id.paradiselost.entities.passive.PopomEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;

public class EatFlowersGoal extends MoveToTargetPosGoal {
    private final PopomEntity goober;
    private final ServerWorld world;
    private int timer;
    private boolean eating;

    public EatFlowersGoal(PopomEntity goober, double speed) {
        super(goober, speed, 8, 3);
        this.goober = goober;
        this.world = getServerWorld(goober);
        this.eating = false;
    }

    @Override
    public boolean canStart() {
        return this.goober.getFurSize() < 3 && !this.goober.isBaby() && super.canStart(); // this.cat.isTamed() && !this.cat.isSitting() && super.canStart();
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
    public double getDesiredDistanceToTarget() {
        return 1.6;
    }

    @Override
    public void tick() {
        super.tick();
        if (timer > 0) this.timer = Math.max(0, this.timer - 1);
        if (this.timer == this.getTickCount(0) && this.eating) {
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                this.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, targetPos.up(), Block.getRawIdFromState(world.getBlockState(targetPos.up())));
                this.world.setBlockState(targetPos.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            this.goober.eat();
            this.stop();
        } else if (this.hasReached() && !this.eating && this.timer == -1) {
            this.world.sendEntityStatus(this.mob, EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART);
            this.timer = this.getTickCount(40);
            this.eating = true;
            goober.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, targetPos.up().toCenterPos());
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.up());
        return !blockState.isOf(Blocks.WITHER_ROSE) && blockState.isIn(BlockTags.SMALL_FLOWERS);
    }
}
