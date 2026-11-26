package net.id.paradise_lost.mixin.block;

import net.id.paradise_lost.block.decorative.PotMaterial;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block {

    // Required by the Block superclass; never called (mixin classes are not instantiated).
    private FlowerPotBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PotMaterial.PROPERTY);
    }

    @Inject(method = "useWithoutItem", at = @At("RETURN"))
    private void paradiseLost$keepCalcitePot(BlockState state, Level level, BlockPos pos, Player player,
                                             BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.hasProperty(PotMaterial.PROPERTY)
                && state.getValue(PotMaterial.PROPERTY) == PotMaterial.CALCITE
                && cir.getReturnValue().consumesAction()) {
            level.setBlock(pos, BlockRegistry.CALCITE_FLOWER_POT.get().defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}