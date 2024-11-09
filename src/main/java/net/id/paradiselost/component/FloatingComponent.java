package net.id.paradiselost.component;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FloatingComponent implements AutoSyncedComponent {

    private static final int FLOAT_SECONDS = 4;
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

    public void startFloating() {
        floating = true;
        floatTime = 20 * FLOAT_SECONDS;
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

}
