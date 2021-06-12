package com.aether.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class VialItem extends Item {

    private final Fluid fluid;

    public VialItem(Fluid fluid, Properties settings) {
        super(settings);
        this.fluid = fluid;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(world, user, this.fluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hitResult.getBlockPos();
            if (this.fluid != Fluids.EMPTY && world.getBlockState(hitPos.above()).canBeReplaced(new BlockPlaceContext(user, hand, itemStack, hitResult))) {
                world.setBlockAndUpdate(hitPos.above(), fluid.defaultFluidState().createLegacyBlock());
                if (!user.isCreative()) {
                    itemStack.shrink(1);
                }
                return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }
}
