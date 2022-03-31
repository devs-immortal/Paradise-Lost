package net.id.aether.items.tools.bloodstone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.util.AetherSoundEvents;
import net.minecraft.client.item.TooltipContext;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BloodstoneItem extends Item {
    public BloodstoneItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    protected abstract List<Text> getDefaultText();

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.addAll(getDefaultText());
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack unused, PlayerEntity user, LivingEntity entity, Hand hand) {
        var stack = user.getStackInHand(hand);
        BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromEntity(entity);
        stack.getOrCreateNbt().put(BloodstoneCapturedData.NBT_TAG, capturedData.toNBT());
        playPrickEffects(user.world, entity.getBlockPos());
        return ActionResult.success(user.world.isClient());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            ItemStack stack = user.getStackInHand(hand);
            BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromEntity(user);
            stack.getOrCreateNbt().put(BloodstoneCapturedData.NBT_TAG, capturedData.toNBT());
            playPrickEffects(user.world, user.getBlockPos());
            return TypedActionResult.success(stack);
        }
        return super.use(world, user, hand);
    }

    private void playPrickEffects(World world, BlockPos pos) {
        world.playSound(null, pos, AetherSoundEvents.ITEM_BLOODSTONE_PRICK, SoundCategory.PLAYERS, 0.5F, 0.5F);
    }
}
