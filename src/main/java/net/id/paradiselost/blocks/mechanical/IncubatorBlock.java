package net.id.paradiselost.blocks.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradiselost.blocks.blockentity.IncubatorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IncubatorBlock extends ParadiseLostBlockWithEntity {

    public static final MapCodec<IncubatorBlock> CODEC = createCodec(IncubatorBlock::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 5, 16);
    private float offsetHeight;

    public IncubatorBlock(Settings settings) {
        super(settings, true);
        this.offsetHeight = 0.55F;
    }

    public IncubatorBlock(Settings settings, float offsetHeight) {
        super(settings, true);
        this.offsetHeight = offsetHeight;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking() && world.getBlockEntity(pos) instanceof IncubatorBlockEntity incubator) {
            incubator.handleUse(player, hand, player.getStackInHand(hand));
            return ItemActionResult.success(world.isClient());
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof IncubatorBlockEntity && ((IncubatorBlockEntity) blockEntity).hasItem()) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((IncubatorBlockEntity) blockEntity).getItem());
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return IncubatorBlockEntity::tickServer;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IncubatorBlockEntity(pos, state, this.offsetHeight);
    }
}
