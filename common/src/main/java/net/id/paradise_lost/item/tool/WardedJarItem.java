package net.id.paradise_lost.item.tool;

import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WardedJarItem extends Item {
    private final EntityType<?> containedEntityType;

    public WardedJarItem(Properties settings) {
        super(settings);
        this.containedEntityType = null;
    }

    public WardedJarItem(EntityType<? extends Mob> type, Properties settings) {
        super(settings);
        this.containedEntityType = type;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (this.containedEntityType != null) return InteractionResult.PASS;
        Level world = player.level();
        if (entity.getType().equals(EntityType.ALLAY)) {
            ItemEntity itemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), entity.getItemInHand(InteractionHand.MAIN_HAND));
            itemEntity.setDefaultPickUpDelay();
            entity.discard();
            world.addFreshEntity(itemEntity);
            fillJar(stack, player, hand, new ItemStack(ItemRegistry.WARDED_JAR_ALLAY.get()));
            return InteractionResult.SUCCESS;
        } else if (entity.getType().equals(EntityRegistry.QUINT.get())) {
            entity.discard();
            fillJar(stack, player, hand, new ItemStack(ItemRegistry.WARDED_JAR_QUINT.get()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static void fillJar(ItemStack stack, Player player, InteractionHand hand, ItemStack filled) {
        if (stack.getCount() == 1 && !player.hasInfiniteMaterials()) {
            player.setItemInHand(hand, filled);
        } else {
            stack.consume(1, player);
            if (!player.getInventory().add(filled)) player.drop(filled, false);
        }
    }

    public InteractionResult useOn(UseOnContext context) {
        if (this.containedEntityType == null) return InteractionResult.PASS;
        Level world = context.getLevel();
        if (world instanceof ServerLevel) {
            Player user = context.getPlayer();
            ItemStack itemStack = context.getItemInHand();
            BlockPos blockPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            } else {
                blockPos2 = blockPos.relative(direction);
            }
            if (containedEntityType.spawn((ServerLevel) world, itemStack, context.getPlayer(), blockPos2, MobSpawnType.MOB_SUMMONED, true, false) != null) {
                itemStack.consume(1, user);
                context.getPlayer().addItem(new ItemStack(ItemRegistry.WARDED_JAR.get()));
                world.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (this.containedEntityType == null) return InteractionResultHolder.pass(itemStack);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else if (!(world instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemStack);
            } else if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
                Entity entity = this.containedEntityType.spawn((ServerLevel) world, itemStack, user, blockPos, MobSpawnType.MOB_SUMMONED, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemStack);
                } else {
                    itemStack.consume(1, user);
                    user.awardStat(Stats.ITEM_USED.get(this));
                    world.gameEvent(user, GameEvent.ENTITY_PLACE, entity.position());
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, new ItemStack(ItemRegistry.WARDED_JAR.get()));
                }
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }
}
