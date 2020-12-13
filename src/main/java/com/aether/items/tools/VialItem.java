package com.aether.items.tools;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class VialItem extends Item {

    private final Fluid fluid;

    public VialItem(Fluid fluid, Settings settings) {
        super(settings);
        this.fluid = fluid;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, this.fluid == Fluids.EMPTY ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hitResult.getBlockPos();
            if (this.fluid != Fluids.EMPTY && world.getBlockState(hitPos.up()).canReplace(new ItemPlacementContext(user, hand, itemStack, hitResult))) {
                world.setBlockState(hitPos.up(), fluid.getDefaultState().getBlockState());
                itemStack.decrement(1);
                return TypedActionResult.success(itemStack, world.isClient());
            }
        }
        return TypedActionResult.fail(itemStack);
    }
}
