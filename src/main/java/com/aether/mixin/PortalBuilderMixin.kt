package com.aether.mixin

import com.aether.Aether
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.RaycastContext.FluidHandling
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(BucketItem::class)
class PortalBuilderMixin : Item(null) {
    @Shadow
    @Final
    private val fluid: Fluid? = null
    @Inject(method = ["use"], at = [At("HEAD")], cancellable = true)
    fun use(world: World, user: PlayerEntity, hand: Hand?, cir: CallbackInfoReturnable<TypedActionResult<ItemStack?>?>?) {
        val hitResult = raycast(world, user, if (fluid === Fluids.EMPTY) FluidHandling.SOURCE_ONLY else FluidHandling.NONE)
        if (fluid === Fluids.WATER && hitResult.type == HitResult.Type.BLOCK) {
            val pos = hitResult.blockPos
            if (world.getBlockState(pos).isOf(Blocks.GLOWSTONE)) {
                for(dir in Direction.values())
                    if(dir.horizontal != -1 && world.getBlockState(pos.offset(dir)).isOf(Blocks.GLOWSTONE)) {
                        cir?.returnValue = TypedActionResult.success(getEmptiedStack(user.getStackInHand(hand), user), world.isClient())
                        buildPortal(pos, dir.axis == Direction.Axis.Z, world)
                        return;
                    }
            }
        }
    }

    fun buildPortal(pos: BlockPos, NS: Boolean, world: World) {
        Aether.AETHER_LOG.error(findFrame(pos, NS, world))
    }

    private fun findFrame(startPos: BlockPos, NS: Boolean = true, world: World): Boolean {
        val moveDirs = if(NS)
            listOf(Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN)
        else
            listOf(Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN)
        val path = mutableSetOf<BlockPos>()
        var curPos = startPos
        var valid = false
        var endpoint = false
        pathfinder@ while(true) {
            for(dir in moveDirs) {
                var probePos = curPos.offset(dir)
                if((path.size >= 8 && probePos == startPos)){
                    valid = true;
                    break@pathfinder;
                }
                if(!path.contains(probePos) && !(path.size < 8 && probePos == startPos) && world.getBlockState(probePos).isOf(Blocks.GLOWSTONE)) {
                    path.add(probePos)
                    curPos = probePos
                    endpoint = false
                }
            }
            if(endpoint)
                break;
            endpoint = true
        }
        return valid;
    }

    @Shadow
    protected fun getEmptiedStack(stack: ItemStack?, player: PlayerEntity): ItemStack? {
        TODO("not really")
    }
}