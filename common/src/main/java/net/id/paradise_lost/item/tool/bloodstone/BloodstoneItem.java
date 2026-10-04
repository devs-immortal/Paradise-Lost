package net.id.paradise_lost.item.tool.bloodstone;

import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import java.util.List;

public abstract class BloodstoneItem extends Item {
    public BloodstoneItem(Item.Properties settings) {
        super(settings);
    }

        protected abstract List<Component> getDefaultText();

    @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.addAll(getDefaultText());
        super.appendHoverText(stack, context, tooltip, type);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack unused, Player user, LivingEntity entity, InteractionHand hand) {
        var stack = user.getItemInHand(hand);
        BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromEntity(entity);
        if (capturedData.isMoa) {
            stack.set(ParadiseLostDataComponentTypes.MOA_GENES, capturedData.moaGeneComponent);
        } else {
            stack.remove(ParadiseLostDataComponentTypes.MOA_GENES);
        }
        stack.set(ParadiseLostDataComponentTypes.BLOODSTONE, capturedData.bloodstoneComponent);
        playPrickEffects(user.level(), entity.blockPosition());
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (user.isShiftKeyDown()) {
            ItemStack stack = user.getItemInHand(hand);
            BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromEntity(user);
            stack.set(ParadiseLostDataComponentTypes.BLOODSTONE, capturedData.bloodstoneComponent);
            playPrickEffects(user.level(), user.blockPosition());
            return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
        }
        return super.use(world, user, hand);
    }

    private void playPrickEffects(Level world, BlockPos pos) {
        world.playSound(null, pos, ParadiseLostSoundEvents.ITEM_BLOODSTONE_PRICK, SoundSource.PLAYERS, 0.5F, 0.5F);
    }
}
