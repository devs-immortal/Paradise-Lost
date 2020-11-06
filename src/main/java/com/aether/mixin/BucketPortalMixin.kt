package com.aether.mixin

import com.aether.Aether.PORTAL_BLOCKS
import com.aether.blocks.AetherBlocks
import com.aether.blocks.PortalBlock
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import net.fabricmc.fabric.api.`object`.builder.v1.advancement.CriterionRegistry
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementManager
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
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
import java.util.*
import kotlin.math.sqrt

private val <E> HashSet<E>.awd: Unit
    get() {}

@Mixin(BucketItem::class)
class BucketPortalMixin: Item(Settings()){

    @Shadow
    @Final
    private val fluid: Fluid? = null

    @Inject(method = ["use"], at = [At("HEAD")], cancellable = true)
    fun use(world: World, user: PlayerEntity, hand: Hand?, cir: CallbackInfoReturnable<TypedActionResult<ItemStack?>?>?) {
        if (!user.isSneaking && fluid!!.matchesType(Fluids.WATER)) {
            val result = raycast(world, user, FluidHandling.NONE)
            if(result.type == HitResult.Type.BLOCK) {
                if(world.getBlockState(result.blockPos).block in PORTAL_BLOCKS) {
                    val pos = result.blockPos
                    if(buildPortal(world, user, pos, (world.getBlockState(pos.offset(Direction.NORTH)).block in PORTAL_BLOCKS || world.getBlockState(pos.offset(Direction.SOUTH)).block in PORTAL_BLOCKS)))
                        cir?.returnValue = TypedActionResult.success(ItemUsage.method_30012(user.getStackInHand(hand), user, ItemStack(fluid.bucketItem)), world.isClient())
                    return
                }
            }
        }
    }

    fun buildPortal(world: World, user: PlayerEntity, pos: BlockPos, NS: Boolean): Boolean {
        val dirs = if(NS)
            ImmutableList.of(Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN)
        else
            ImmutableList.of(Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN)
        val frameScan = verifyFrame(world, pos, dirs)
        var valid = false
        if(!frameScan.isEmpty()) {
            var testPos = pos
            while (testPos.y < 256) {
                testPos = testPos.up()
                if(world.getBlockState(testPos).block in PORTAL_BLOCKS && testPos in frameScan) {
                    valid = true
                    break
                }
            }
            if(valid) {
                floodFill(world, pos.up(), dirs, AetherBlocks.BLUE_PORTAL.defaultState.with(PortalBlock.AXIS, if (NS) Direction.Axis.Z else Direction.Axis.X))
            }
        }
        return valid
    }

    private fun floodFill(world: World, start: BlockPos, directions: List<Direction>, portal: BlockState, curpos: BlockPos = start) {
        if(sqrt(curpos.getSquaredDistance(start)) <= 64) {
            world.setBlockState(curpos, portal)
            for(dir in directions) {
                val probePos = curpos.offset(dir)
                if(world.getBlockState(probePos).isAir)
                    floodFill(world, start, directions, portal, probePos)
            }
        }
    }

    private fun verifyFrame(world: World, start: BlockPos, directions: List<Direction>): Set<BlockPos> {
        var pos = start
        var valid = false
        val path = HashSet<BlockPos>()
        var endpoint = false
        pathfinder@ while(true) {
            for(dir in directions) {
                val probePos = pos.offset(dir)
                if((path.size >= 8 && probePos == start)){
                    valid = true;
                    break@pathfinder;
                }
                if(!path.contains(probePos) && !(path.size < 8 && probePos == start) && world.getBlockState(probePos).block in PORTAL_BLOCKS) {
                    path.add(probePos)
                    pos = probePos
                    endpoint = false
                }
            }
            if(endpoint)
                break;
            endpoint = true
        }
        return if(valid) path else ImmutableSet.of()
    }
}