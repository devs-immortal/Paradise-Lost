package net.id.paradiselost.world;

import net.minecraft.sound.SoundEvent;

public interface ExplosionExtensions {

    float getPower();

    void affectWorld(boolean particles, SoundEvent customSound);
}
