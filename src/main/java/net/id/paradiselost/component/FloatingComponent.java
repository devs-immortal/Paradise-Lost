package net.id.paradiselost.component;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FloatingComponent implements AutoSyncedComponent {

    private static final double FLOAT_SECONDS = 4.1;
    private boolean floating = false;
    private int floatTime = 0;

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        floating = tag.getBoolean("floating");
        floatTime = tag.getInt("floatTime");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("floating", floating);
        tag.putInt("floatTime", floatTime);
    }

    public boolean getFloating() {
        return floating;
    }

    public int getFloatTime() {
        return floatTime;
    }

    public void addFloating() {
        floating = true;
        floatTime += (int) (20 * FLOAT_SECONDS);
    }

    public void stopFloating() {
        floating = false;
        floatTime = 0;
    }

    public void tick() {
        floatTime--;
        if (floatTime == 0) {
            floating = false;
        }
    }

    public boolean isCartOnRail(AbstractMinecartEntity minecart) {
        int i = MathHelper.floor(minecart.getX());
        int j = MathHelper.floor(minecart.getY());
        int k = MathHelper.floor(minecart.getZ());
        BlockState blockState = minecart.getWorld().getBlockState(new BlockPos(i, j, k));
        return AbstractRailBlock.isRail(blockState);
    }

}
