package net.id.aether.blocks.dungeon;

import net.id.aether.blocks.blockentity.dungeon.DungeonSwitchBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SculkSensorBlockEntity;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class DungeonSwitchBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty POWERED = Properties.POWERED;

    boolean hitActivates = true;
    boolean interactActivates = true;
    boolean projectileActivates = true;
    boolean explosionActivates = true;

    public DungeonSwitchBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));

    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.isSneaking())
                setLinkedPos(world, pos, player.getBlockPos());
            else {
                if (interactActivates) {
                    BlockState newState = this.togglePower(state, world, pos);
                    triggerLinkedOpen(world, pos, newState.get(POWERED));
                }
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        if (!world.isClient && hitActivates) {
            BlockState newState = this.togglePower(state, world, pos);
            triggerLinkedOpen(world, pos, newState.get(POWERED));
        }
    }

    //called from be even listener
    public void onExplosionEvent(World world, BlockPos pos) {
        System.out.println("boom");
        if (!world.isClient && explosionActivates) {
            BlockState state = world.getBlockState(pos);
            BlockState newState = this.togglePower(state, world, pos);
            triggerLinkedOpen(world, pos, newState.get(POWERED));
        }
    }

    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(World world, T blockEntity) {
        if (blockEntity instanceof DungeonSwitchBlockEntity dungeonSwitchBlockEntity)
            return dungeonSwitchBlockEntity;
        return null;
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        super.onProjectileHit(world, state, hit, projectile);
        if (!world.isClient && projectileActivates) {
            BlockState newState = this.togglePower(state, world, hit.getBlockPos());
            triggerLinkedOpen(world, hit.getBlockPos(), newState.get(POWERED));
        }
    }

    public void setLinkedPos(World world, BlockPos pos, BlockPos linkedPos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof DungeonSwitchBlockEntity dungeonSwitchBlockEntity) {
            dungeonSwitchBlockEntity.setLinkedPos(linkedPos);
        }
    }

    public void triggerLinkedOpen(World world, BlockPos pos, Boolean powered) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof DungeonSwitchBlockEntity dungeonSwitchBlockEntity) {
            BlockPos linkedPos = dungeonSwitchBlockEntity.getLinkedPos();
            BlockState linkedState = world.getBlockState(linkedPos);
            if (linkedState.getBlock() instanceof DoorBlock) {
                world.setBlockState(linkedPos, linkedState.with(DoorBlock.OPEN, powered));
            }
        }
    }

    public BlockState togglePower(BlockState state, World world, BlockPos pos) {
        state = state.cycle(POWERED);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        world.updateNeighborsAlways(pos, this);
        return state;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DungeonSwitchBlockEntity(pos, state);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
