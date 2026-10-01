package net.id.paradise_lost.component;

import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class FloatingComponent {
    private static final double FLOAT_SECONDS = 4.1;

    private final AbstractMinecart cart;

    FloatingComponent(AbstractMinecart cart) {
        this.cart = cart;
    }

    public void readFromNbt(CompoundTag tag) {

        int time = tag.getInt("floatTime");
        setFloatTime(tag.getBoolean("floating") ? Math.max(time, 1) : time);
    }

    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("floating", getFloating());
        tag.putInt("floatTime", getFloatTime());
    }

    public void setFloatTime(int time) {
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOATING, time);
    }

    public boolean getFloating() {
        return getFloatTime() > 0;
    }

    public int getFloatTime() {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOATING);
    }

    public void addFloating() {
        setFloatTime(getFloatTime() + (int) (20 * FLOAT_SECONDS));
    }

    public void stopFloating() {
        setFloatTime(0);
    }

    public void tick() {
        int time = getFloatTime() - 1;
        setFloatTime(Math.max(time, 0));
    }

    public boolean isCartOnRail(AbstractMinecart minecart) {
        int i = Mth.floor(minecart.getX());
        int j = Mth.floor(minecart.getY());
        int k = Mth.floor(minecart.getZ());
        BlockState blockState = minecart.level().getBlockState(new BlockPos(i, j, k));
        return BaseRailBlock.isRail(blockState);
    }
}
