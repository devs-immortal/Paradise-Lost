package net.id.paradise_lost.item.armor;

import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.networking.packet.FloatyAnchorC2SPacket;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.id.paradise_lost.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class FloatyLeggingsItem extends ArmorItem {
    public static final int DURABILITY_INTERVAL_TICKS = 20;
    public static final int JUMP_TOGGLE_TICKS = 7;
    private static final double DESCEND_SPEED = -0.15D;
    private static final double HORIZONTAL_FRICTION = 0.6D * 0.91D;

    public FloatyLeggingsItem(Holder<ArmorMaterial> material, Properties settings) {
        super(material, Type.LEGGINGS, settings);
    }

    public static boolean isWearing(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof FloatyLeggingsItem;
    }

    public static boolean isFloatyEnabled(ItemStack stack) {
        return !isBroken(stack);
    }

    public static boolean isBroken(ItemStack stack) {
        return stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    public static int getDefensePoints(FloatyLeggingsItem item) {
        return item.getMaterial().value().getDefense(item.getType());
    }

    public static boolean applyWearDamage(ItemStack stack, int amount, LivingEntity entity, EquipmentSlot slot) {
        if (!(stack.getItem() instanceof FloatyLeggingsItem) || !stack.isDamageableItem()) {
            return false;
        }
        int cap = stack.getMaxDamage() - 1;
        int before = stack.getDamageValue();
        if (before >= cap) {
            return false;
        }
        stack.setDamageValue(Math.min(before + amount, cap));
        return stack.getDamageValue() != before;
    }

    public static boolean isSurvivalLike(Player player) {
        return !player.isCreative() && !player.isSpectator();
    }

    public static boolean canUseFloaty(Player player) {
        return isWearing(player)
                && !player.isPassenger()
                && !player.getAbilities().flying
                && !player.isFallFlying();
    }

    public static boolean canAnchor(Player player) {
        return canUseFloaty(player)
                && isSurvivalLike(player)
                && !player.onGround()
                && isFloatyEnabled(player.getItemBySlot(EquipmentSlot.LEGS));
    }

    public static void onGameModeChanged(ServerPlayer player, GameType from, GameType to) {
        if (from.isSurvival() == to.isSurvival()) {
            return;
        }
        if (!(player instanceof ParadiseLostEntityExtensions extensions)) {
            return;
        }
        if (from.isSurvival()) {
            Services.ATTACHMENTS.setAttachedValue(
                    player, CommonDataAttachments.FLOATY_ANCHORED_SAVED, extensions.isFloatyAnchored());
            if (extensions.isFloatyAnchored()) {
                extensions.setFloatyAnchored(false);
            }
        } else {
            boolean saved = Boolean.TRUE.equals(
                    Services.ATTACHMENTS.getOrCreateAttachedValue(player, CommonDataAttachments.FLOATY_ANCHORED_SAVED));
            if (saved && canAnchor(player)) {
                extensions.setFloatyAnchored(true);
            }
        }
    }

    public static void toggleAnchorFromClient(Player player) {
        if (!(player instanceof ParadiseLostEntityExtensions extensions) || !canAnchor(player)) {
            return;
        }
        boolean next = !extensions.isFloatyAnchored();
        extensions.setFloatyAnchored(next);
        if (next) {
            player.resetFallDistance();
        }
        PacketHandler.sendToServer(new FloatyAnchorC2SPacket(next));
    }

    public static void beginAnchoredHover(Player player) {
        player.setNoGravity(true);
        player.resetFallDistance();
    }

    public static void onAnchorStarted(Player player) {
        player.setNoGravity(true);
        player.setDeltaMovement(Vec3.ZERO);
        player.resetFallDistance();
        player.walkAnimation.update(0.0F, 0.4F);
    }

    public static boolean hasMovementInput(float strafe, float forward) {
        return Math.abs(strafe) > 1.0E-4F || Math.abs(forward) > 1.0E-4F;
    }

    public static void travelAnchored(Player player, float strafe, float forward) {
        player.setNoGravity(true);

        double y = player.isShiftKeyDown() ? DESCEND_SPEED : 0.0D;
        double xBefore = player.getX();
        double zBefore = player.getZ();

        Vec3 motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x, 0.0D, motion.z);
        if (hasMovementInput(strafe, forward)) {
            player.moveRelative(player.getSpeed(), new Vec3(strafe, 0.0D, forward));
        }

        motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x, y, motion.z);
        player.move(MoverType.SELF, player.getDeltaMovement());

        motion = player.getDeltaMovement();
        double hx = motion.x * HORIZONTAL_FRICTION;
        double hz = motion.z * HORIZONTAL_FRICTION;
        if (hx * hx + hz * hz < 1.0E-8D) {
            hx = 0.0D;
            hz = 0.0D;
        }
        player.setDeltaMovement(hx, y, hz);

        float dist = (float) Math.hypot(player.getX() - xBefore, player.getZ() - zBefore);
        player.walkAnimation.update(Math.min(dist * 4.0F, 1.0F), 0.4F);
        player.resetFallDistance();
    }

    public static void clearAnchoredHover(Player player, boolean holdingNoGravity) {
        if (holdingNoGravity) {
            player.setNoGravity(false);
        }
    }

    public static void hurtWhileAnchored(Player player) {
        if (player.level().isClientSide() || player.getAbilities().instabuild) {
            return;
        }
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof FloatyLeggingsItem) {
            legs.hurtAndBreak(1, player, EquipmentSlot.LEGS);
        }
    }
}
