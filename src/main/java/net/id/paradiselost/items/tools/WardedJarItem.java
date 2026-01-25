package net.id.paradiselost.items.tools;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class WardedJarItem extends Item {
    private final EntityType<?> containedEntityType;

    public WardedJarItem(Settings settings) {
        super(settings);
        this.containedEntityType = null;
    }

    public WardedJarItem(EntityType<? extends MobEntity> type, Settings settings) {
        super(settings);
        this.containedEntityType = type;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (this.containedEntityType != null) return ActionResult.PASS;
        World world = player.getWorld();
        if (entity.getType().equals(EntityType.ALLAY)) {
            ItemEntity itemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), entity.getStackInHand(Hand.MAIN_HAND));
            itemEntity.setToDefaultPickupDelay();
            entity.discard();
            world.spawnEntity(itemEntity);
            stack.decrementUnlessCreative(1, player);
            var jarItem = new ItemStack(ParadiseLostItems.WARDED_JAR_ALLAY);
            player.giveItemStack(jarItem);
            return ActionResult.SUCCESS;
        } else if (entity.getType().equals(ParadiseLostEntityTypes.QUINT)) {
            entity.discard();
            stack.decrementUnlessCreative(1, player);
            var jarItem = new ItemStack(ParadiseLostItems.WARDED_JAR_QUINT);
            player.giveItemStack(jarItem);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        if (this.containedEntityType == null) return ActionResult.PASS;
        World world = context.getWorld();
        if (world instanceof ServerWorld) {
            PlayerEntity user = context.getPlayer();
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            } else {
                blockPos2 = blockPos.offset(direction);
            }
            if (containedEntityType.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), blockPos2, SpawnReason.MOB_SUMMONED, true, false) != null) {
                itemStack.decrementUnlessCreative(1, user);
                context.getPlayer().giveItemStack(new ItemStack(ParadiseLostItems.WARDED_JAR));
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }
        }
        return ActionResult.SUCCESS;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.containedEntityType == null) return TypedActionResult.pass(itemStack);
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else if (!(world instanceof ServerWorld)) {
            return TypedActionResult.success(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
                return TypedActionResult.pass(itemStack);
            } else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
                Entity entity = this.containedEntityType.spawnFromItemStack((ServerWorld) world, itemStack, user, blockPos, SpawnReason.MOB_SUMMONED, false, false);
                if (entity == null) {
                    return TypedActionResult.pass(itemStack);
                } else {
                    itemStack.decrementUnlessCreative(1, user);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
                    return new TypedActionResult<>(ActionResult.SUCCESS, new ItemStack(ParadiseLostItems.WARDED_JAR));
                }
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }
}
