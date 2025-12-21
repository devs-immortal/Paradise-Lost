package net.id.paradiselost.blocks.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.blockentity.IncubatorBlockEntity;
import net.id.paradiselost.blocks.blockentity.PalaceDoorBlockEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PalaceDoorBlock extends BlockWithEntity {

    public static final MapCodec<PalaceDoorBlock> CODEC = createCodec(PalaceDoorBlock::new);

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public PalaceDoorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking() && stack.isOf(ParadiseLostItems.PALACE_KEY) && world.getBlockEntity(pos) instanceof PalaceDoorBlockEntity be && !be.isOpen()) {
            stack.decrementUnlessCreative(1, player);
            be.open();
            // TODO!
            return ItemActionResult.CONSUME;
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static Direction getRotation(BlockState state) {
        if (state.getBlock() != ParadiseLostBlocks.PALACE_DOOR) {
            return Direction.NORTH;
        }
        return state.get(FACING);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PalaceDoorBlockEntity(pos, state);
    }
}
