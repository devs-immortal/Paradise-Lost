package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityExtensions;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.entities.block.FloatingBlockStructure;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.function.Supplier;

public class GravititeTool {
    public static ActionResult flipEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        ((AetherEntityExtensions) entity).setFlipped();
        return ActionResult.SUCCESS;
    }

    public static ActionResult tryFloatBlock(ItemUsageContext context, ActionResult defaultResult) {
        if (defaultResult != ActionResult.PASS || !eligibleToFloat(context)) {
            return defaultResult;
        }
        return createFloatingBlockEntity(context);
    }

    private static boolean eligibleToFloat(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);
        ItemStack heldItem = context.getStack();
        Supplier<Boolean> dropState = () -> {
            int distFromTop = world.getTopY() - pos.getY();
            boolean isFastFloater = (
                    state.getBlock() == AetherBlocks.GRAVITITE_ORE ||
                            state.getBlock() == AetherBlocks.GRAVITITE_LEVITATOR ||
                            state.getBlock() == AetherBlocks.BLOCK_OF_GRAVITITE);

            return !isFastFloater && distFromTop <= 50;
        };

        return (!state.isToolRequired() || heldItem.isSuitableFor(state))
                && FloatingBlockEntity.canMakeBlock(dropState, world.getBlockState(pos.down()), world.getBlockState(pos.up()));
    }

    private static ActionResult createFloatingBlockEntity(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);

        if (world.getBlockEntity(pos) != null || state.getHardness(world, pos) == -1.0F) {
            return ActionResult.FAIL;
        }
        if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.SOUL_FIRE) {
            world.breakBlock(pos, false);
            return ActionResult.SUCCESS;
        }

        if (!world.isClient()) {
            if (state.getProperties().contains(Properties.DOUBLE_BLOCK_HALF)) { // doors and tall grass
                if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                    pos = pos.down();
                    state = world.getBlockState(pos);
                }
                BlockState upperState = world.getBlockState(pos.up());
                FloatingBlockEntity upper = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, upperState);
                FloatingBlockEntity lower = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
                FloatingBlockStructure structure = new FloatingBlockStructure(lower, upper, Vec3i.ZERO.up());
                structure.spawn(world);
            } else { // everything else
                FloatingBlockEntity entity = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
                entity.floatTime = 0;
                if (state.getBlock() == Blocks.TNT) {
                    entity.setOnEndFloating((impact, landed) -> {
                        if (impact >= 0.8) {
                            BlockPos landingPos = entity.getBlockPos();
                            world.breakBlock(landingPos, false);
                            world.createExplosion(entity, landingPos.getX(), landingPos.getY(), landingPos.getZ(), (float) MathHelper.clamp(impact * 5.5, 0, 10), Explosion.DestructionType.BREAK);
                        }
                    });
                }
                if (state.getBlock() == Blocks.LIGHTNING_ROD) {
                    entity.setOnEndFloating((impact, landed) -> {
                        if (world.isThundering() && landed && impact >= 1.1) {
                            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightning.setPosition(Vec3d.ofCenter(entity.getBlockPos()));
                            world.spawnEntity(lightning);
                        }
                    });
                }
                world.spawnEntity(entity);
            }
        }

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            context.getStack().damage(4, context.getPlayer(), (p) -> p.sendToolBreakStatus(context.getHand()));
        }

        return ActionResult.SUCCESS;
    }
}
