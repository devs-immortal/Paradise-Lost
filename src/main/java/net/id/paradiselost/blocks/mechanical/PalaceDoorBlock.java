package net.id.paradiselost.blocks.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradiselost.blocks.blockentity.IncubatorBlockEntity;
import net.id.paradiselost.blocks.blockentity.PalaceDoorBlockEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PalaceDoorBlock extends BlockWithEntity {

    public static final MapCodec<PalaceDoorBlock> CODEC = createCodec(PalaceDoorBlock::new);

    public PalaceDoorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking() && stack.isOf(ParadiseLostItems.PALACE_KEY) && world.getBlockEntity(pos) instanceof PalaceDoorBlockEntity be) {
            stack.decrementUnlessCreative(1, player);
            be.open(true);
            // TODO!
            return ItemActionResult.CONSUME;
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
