package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityExtensions;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.entities.block.FloatingBlockStructure;
import com.aether.items.utils.AetherTiers;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface IAetherTool {
    float getMiningSpeedMultiplier(ItemStack item, BlockState state);

    AetherTiers getTier();

    Logger log = LogManager.getLogger(IAetherTool.class);

    default boolean eligibleToFloat(ItemUsageContext context) {
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
                && FloatingBlockEntity.canMakeBlock(dropState, world.getBlockState(pos.down()),world.getBlockState(pos.up()));
    }

    default ActionResult useOnBlock(ItemUsageContext context, @Nullable ActionResult defaultResult) {
        if (this.getTier() == AetherTiers.GRAVITITE) {
            if (eligibleToFloat(context)) {
                return createFloatingBlockEntity(context);
            }
        }
        return defaultResult != null ? defaultResult : defaultItemUse(context);
    }

    private ActionResult createFloatingBlockEntity(ItemUsageContext context){
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
            if(state.getProperties().contains(Properties.DOUBLE_BLOCK_HALF)){ // doors and tall grass
                if(state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER){
                    pos = pos.down();
                    state = world.getBlockState(pos);
                }
                BlockState upperState = world.getBlockState(pos.up());
                FloatingBlockEntity upper = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY()+1, pos.getZ() + 0.5, upperState);
                FloatingBlockEntity lower = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
                FloatingBlockStructure structure = new FloatingBlockStructure(lower, upper, Vec3i.ZERO.up());
                structure.spawn(world);
            } else { // everything else
                FloatingBlockEntity entity = new FloatingBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
                entity.floatTime = 0;
                if (state.getBlock() == Blocks.TNT){
                    System.out.println("boomer says");
                    entity.setOnLand((landingPos, impact) -> {
                        System.out.println("boom " + impact);
                        if (impact >= 0.8) {
                            System.out.println("yea");
                            world.breakBlock(landingPos, false);
                            world.createExplosion(entity, landingPos.getX(), landingPos.getY(), landingPos.getZ(), (float) MathHelper.clamp(impact*5.5, 0, 10), Explosion.DestructionType.BREAK);
                        }
                    });
                }
                if (state.getBlock() == Blocks.LIGHTNING_ROD){
                    entity.setOnLand((landingPos, impact) -> {
                        if (world.isThundering() && impact >= 1.1){
                            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightning.setPosition(Vec3d.ofCenter(landingPos));
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

    default ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand){
        if(this.getTier() == AetherTiers.GRAVITITE){
            ((AetherEntityExtensions)entity).setFlipped();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    default ActionResult defaultItemUse(ItemUsageContext context) {
        return ActionResult.SUCCESS;
    }
}