package net.id.paradise_lost.item.tool;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;

public class AurelMilkBucketItem extends MilkBucketItem {
    public AurelMilkBucketItem(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (user instanceof ServerPlayer serverPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!world.isClientSide) {
            user.removeAllEffects();
        }

        if (user instanceof Player playerEntity) {
            return ItemUtils.createFilledResult(stack, playerEntity, new ItemStack(ItemRegistry.AUREL_BUCKET.get()), false);
        } else {
            stack.consume(1, user);
            return stack;
        }
    }
}
