package com.aether.items.tools;

import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.utils.AetherTiers;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public interface IAetherTool {
    float getMiningSpeedMultiplier(ItemStack item, BlockState state);

    AetherTiers getTier();

    Logger log = LogManager.getLogger(IAetherTool.class);

    default ActionResult useOnBlock(ItemUsageContext context, @Nullable ActionResult defaultResult) {
        if (this.getTier() == AetherTiers.Gravitite) {
            BlockPos pos = context.getBlockPos();
            World world = context.getWorld();
            BlockState state = world.getBlockState(pos);
            ItemStack heldItem = context.getStack();

            if ((!state.isToolRequired() || heldItem.isSuitableFor(state)) && FallingBlock.canFallThrough(world.getBlockState(pos.up()))) {
                if (world.getBlockEntity(pos) != null || state.getHardness(world, pos) == -1.0F) {
                    return ActionResult.FAIL;
                }

                if (!world.isClient()) {
                    FloatingBlockEntity entity = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
                    entity.floatTime = 0;
                    world.spawnEntity(entity);
                }

                if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                    context.getStack().damage(4, context.getPlayer(), (p) -> p.sendToolBreakStatus(context.getHand()));
                }

                return ActionResult.SUCCESS;
            }
        }

        return defaultResult != null ? defaultResult : defaultItemUse(context);
    }

    default float calculateIncrease(ItemStack tool) {
        int current = tool.getDamage();
        int maxDamage = tool.getMaxDamage();

        if (maxDamage - 50 <= current) {
            return 7.0F;
        } else if (maxDamage - 110 <= current) {
            return 6.0F;
        } else if (maxDamage - 200 <= current) {
            return 5.0F;
        } else if (maxDamage - 239 <= current) {
            return 4.0F;
        } else {
            return 3.0F;
        }
    }

    default ActionResult defaultItemUse(ItemUsageContext context) {
        return ActionResult.SUCCESS;
    }
}