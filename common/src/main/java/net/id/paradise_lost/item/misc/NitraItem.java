package net.id.paradise_lost.item.misc;

import net.id.paradise_lost.entity.projectile.ThrownNitraEntity;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NitraItem extends Item {
    public NitraItem(Item.Properties settings) {
        super(settings);
    }

    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), ParadiseLostSoundEvents.ENTITY_NITRA_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        user.getCooldowns().addCooldown(itemStack, 10);
        if (!world.isClientSide) {
            ThrownNitraEntity nitraEntity = new ThrownNitraEntity(world, user, itemStack);
            nitraEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(nitraEntity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return world.isClientSide
                ? InteractionResult.SUCCESS.heldItemTransformedTo(itemStack)
                : InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(itemStack);
    }
}
