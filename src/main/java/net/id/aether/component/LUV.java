package net.id.aether.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * What are you doing here?
 * Dawn will soon break...
 * Child of light, this is not for you to see.
 *
 *
 * @author why, that is not for you to know, is it?
 */
public class LUV implements AutoSyncedComponent, CommonTickingComponent, PlayerComponent<LUV> {

    private PlayerEntity player;
    private byte value;

    public LUV(PlayerEntity player) {
        value = (byte) player.getRandom().nextInt(127);
        this.player = player;
    }

    @Override
    public void tick() {
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        value = tag.getByte("LUV");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putByte("LUV", value);
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
        return true;
    }
}
