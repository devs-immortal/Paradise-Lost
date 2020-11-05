package com.aether.mixin;

import java.util.List;

import com.aether.Aether;
import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item {
    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    @Final
    private Fluid fluid;

    @Shadow
    protected abstract ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player);

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        BlockHitResult hitResult = raycast(world, user, (fluid == Fluids.EMPTY) ? FluidHandling.SOURCE_ONLY : FluidHandling.NONE);
        if (fluid == Fluids.WATER && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            if (world.getBlockState(pos).isOf(Blocks.GLOWSTONE)) {
                for (Direction dir : Direction.values()) {
                    if (dir.getHorizontal() != -1 && world.getBlockState(pos.offset(dir)).isOf(Blocks.GLOWSTONE)) {
                        cir.setReturnValue(TypedActionResult.success(getEmptiedStack(user.getStackInHand(hand), user), world.isClient()));
                        buildPortal(pos, dir.getAxis() == Direction.Axis.Z, world);
                        break;
                    }
                }
            }
        }
    }

    private void buildPortal(BlockPos pos, boolean NS, World world) {
        Aether.AETHER_LOG.error(findFrame(pos, NS, world));
    }

    private boolean findFrame(BlockPos startPos, boolean NS, World world) {
        List<Direction> moveDirs = NS ? Lists.newArrayList(Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN) : Lists.newArrayList(Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN);
        List<BlockPos> path = Lists.newArrayList();
        BlockPos curPos = startPos;
        boolean valid = false;
        boolean endpoint = false;
        pathfinder: while(true) {
            for(Direction dir : moveDirs) {
                BlockPos probePos = curPos.offset(dir);
                if ((path.size() >= 8 && probePos == startPos)) {
                    valid = true;
                    break pathfinder;
                }
                if (!path.contains(probePos) && !(path.size() < 8 && probePos == startPos) && world.getBlockState(probePos).isOf(Blocks.GLOWSTONE)) {
                    path.add(probePos);
                    curPos = probePos;
                    endpoint = false;
                }
            }
            if (endpoint)
                break;
            endpoint = true;
        }
        return valid;
    }
}
