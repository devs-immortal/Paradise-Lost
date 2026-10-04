package net.id.paradise_lost.block.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.block.blockentity.IncubatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class IncubatorBlock extends ParadiseLostBlockWithEntity {

    public static final MapCodec<IncubatorBlock> CODEC = simpleCodec(IncubatorBlock::new);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 5, 16);
    private float offsetHeight;

    public IncubatorBlock(Properties settings) {
        super(settings, true);
        this.offsetHeight = 0.55F;
    }

    public IncubatorBlock(Properties settings, float offsetHeight) {
        super(settings, true);
        this.offsetHeight = offsetHeight;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown() && world.getBlockEntity(pos) instanceof IncubatorBlockEntity incubator) {
            incubator.handleUse(player, hand, player.getItemInHand(hand));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof IncubatorBlockEntity && ((IncubatorBlockEntity) blockEntity).hasItem()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((IncubatorBlockEntity) blockEntity).getItem());
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return IncubatorBlockEntity::tickServer;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IncubatorBlockEntity(pos, state, this.offsetHeight);
    }
}
