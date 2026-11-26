package net.id.paradise_lost.block.decorative;

import net.id.paradise_lost.block.PotMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class CalciteFlowerPotBlock extends FlowerPotBlock {

    public CalciteFlowerPotBlock(Properties settings) {
        super(Blocks.AIR, settings);
        registerDefaultState(defaultBlockState().setValue(PotMaterial.PROPERTY, PotMaterial.CALCITE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PotMaterial.PROPERTY);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        ItemInteractionResult result = super.useItemOn(stack, state, level, pos, player, hand, hit);

        if (result.consumesAction()) {
            BlockState placed = level.getBlockState(pos);
            if (placed.getBlock() != this && placed.hasProperty(PotMaterial.PROPERTY)) {
                level.setBlock(pos, placed.setValue(PotMaterial.PROPERTY, PotMaterial.CALCITE), Block.UPDATE_CLIENTS);
            }
        }
        return result;
    }
}
