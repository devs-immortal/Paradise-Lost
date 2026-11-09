package net.id.paradise_lost.item.tool;

import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

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
        EntityType<?> type = entity.getType();
        if (type.equals(EntityType.ALLAY) || type.equals(EntityRegistry.QUINT.get())) {
            fillJar(stack, player, hand, entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static void fillJar(ItemStack stack, Player player, InteractionHand hand, LivingEntity entity) {
        ItemStack filled = new ItemStack(entity.getType().equals(EntityType.ALLAY)
                ? ItemRegistry.WARDED_JAR_ALLAY.get()
                : ItemRegistry.WARDED_JAR_QUINT.get());
        if (player.hasInfiniteMaterials()) {
            player.getInventory().add(filled);
        } else {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filled, false));
        }
        player.playSound(ParadiseLostSoundEvents.ITEM_WARDED_JAR_IN, 1.0F, 1.0F);
        entity.discard();
    }

    @Nullable
    private Entity release(ServerLevel level, ItemStack stack, @Nullable Player player, BlockPos pos, boolean alignPosition, boolean invertY) {
        return this.containedEntityType.spawn(level, stack, player, pos, MobSpawnType.MOB_SUMMONED, alignPosition, invertY);
    }

    private static ItemStack emptyJar(ItemStack held, Player player) {
        return player.hasInfiniteMaterials() ? held : new ItemStack(ItemRegistry.WARDED_JAR.get());
    }

    public InteractionResult useOn(UseOnContext context) {
        if (this.containedEntityType == null) return InteractionResult.PASS;
        Level world = context.getLevel();
        Player user = context.getPlayer();
        if (world instanceof ServerLevel serverLevel) {
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
            if (this.release(serverLevel, itemStack, user, blockPos2, true, false) != null && user != null) {
                user.setItemInHand(context.getHand(), emptyJar(itemStack, user));
                world.gameEvent(user, GameEvent.ENTITY_PLACE, blockPos);
            }
        }
        world.playSound(user, user.blockPosition(), ParadiseLostSoundEvents.ITEM_WARDED_JAR_OUT, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (this.containedEntityType == null) return InteractionResultHolder.pass(itemStack);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else if (world.isClientSide) {
            world.playSound(user, user.blockPosition(), ParadiseLostSoundEvents.ITEM_WARDED_JAR_OUT, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.success(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemStack);
            } else if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
                Entity entity = this.release((ServerLevel) world, itemStack, user, blockPos, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemStack);
                } else {
                    user.awardStat(Stats.ITEM_USED.get(this));
                    world.playSound(user, user.blockPosition(), ParadiseLostSoundEvents.ITEM_WARDED_JAR_OUT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    world.gameEvent(user, GameEvent.ENTITY_PLACE, entity.position());
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, emptyJar(itemStack, user));
                }
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }
}
