package com.aether.component;

import com.aether.entities.passive.MoaEntity;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MoaGenetics implements AutoSyncedComponent {

    private MoaEntity moa;
    private float speed;
    private float glidingSpeed;
    private float jumpStrength;
    private int naturalRegenInterval;
    private boolean legendary;

    public MoaGenetics(MoaEntity moa) {
        this.moa = moa;
    }

    public static MoaGenetics initMoa(@NotNull MoaEntity moa) {
        MoaGenetics genetics = new MoaGenetics(moa);


        return genetics;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }
}
