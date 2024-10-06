package net.id.paradiselost.items.tools.bloodstone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public abstract class BloodstoneItem extends Item {
    public BloodstoneItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    protected abstract List<Text> getDefaultText();

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, net.minecraft.item.Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.addAll(getDefaultText());
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ActionResult useOnEntity(ItemStack unused, PlayerEntity user, LivingEntity entity, Hand hand) {
        var stack = user.getStackInHand(hand);
        BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromEntity(entity);
        if (capturedData.isMoa) {
            stack.set(ParadiseLostDataComponentTypes.MOA_GENES, capturedData.moaGeneComponent);
        }
        stack.set(ParadiseLostDataComponentTypes.BLOODSTONE, capturedData.bloodstoneComponent);
        playPrickEffects(user.getWorld(), entity.getBlockPos());
        return ActionResult.success(user.getWorld().isClient());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            ItemStack stack = user.getStackInHand(hand);
            BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromEntity(user);
            stack.set(ParadiseLostDataComponentTypes.BLOODSTONE, capturedData.bloodstoneComponent);
            playPrickEffects(user.getWorld(), user.getBlockPos());
            return TypedActionResult.success(stack);
        }
        return super.use(world, user, hand);
    }

    private void playPrickEffects(World world, BlockPos pos) {
        world.playSound(null, pos, ParadiseLostSoundEvents.ITEM_BLOODSTONE_PRICK, SoundCategory.PLAYERS, 0.5F, 0.5F);
    }
}
