package net.id.aether.items.tools.bloodstone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public abstract class BloodstoneItem extends Item {
    public BloodstoneItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    protected abstract List<Text> createTooltip(LivingEntity entity);

    @Environment(EnvType.CLIENT)
    protected abstract List<Text> getDefaultText();

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (world != null && nbt.contains("target") && nbt.getUuid("target") != null) {
            @SuppressWarnings("unchecked")
            var target = (Optional<LivingEntity>) (Object) StreamSupport
                    .stream(((ClientWorld) world).getEntities().spliterator(), true)
                    .filter(entity -> entity.isLiving() && entity.getUuid().equals(nbt.getUuid("target")))
                    .findFirst();
            tooltip.add(((MutableText) target.map(Entity::getName).orElse(new LiteralText("???"))).formatted(Formatting.GOLD));
            tooltip.addAll(target.map(((BloodstoneItem) stack.getItem())::createTooltip).orElse(getDefaultText()));
        } else {
            tooltip.addAll(getDefaultText());
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack unused, PlayerEntity user, LivingEntity entity, Hand hand) {
        var stack = user.getStackInHand(hand);
        if (!user.isSneaking()) {
            stack.getOrCreateNbt().putUuid("target", entity.getUuid());
            playPrickEffects(user.world, entity.getBlockPos());
            return ActionResult.success(user.world.isClient());
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            var stack = user.getStackInHand(hand);
            stack.getOrCreateNbt().putUuid("target", user.getUuid());
            playPrickEffects(world, user.getBlockPos());
            return TypedActionResult.success(stack);
        }
        return super.use(world, user, hand);
    }

    private void playPrickEffects(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.ENTITY_BEE_STING, SoundCategory.PLAYERS, 0.5F, 0.5F);
    }
}
