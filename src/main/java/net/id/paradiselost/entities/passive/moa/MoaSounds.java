package net.id.paradiselost.entities.passive.moa;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

import static net.id.paradiselost.util.ParadiseLostSoundEvents.*;

public class MoaSounds {
    private final MoaEntity moa;
    private int moaSoundCallCooldown = 200;
    private float soundChance = 0;
    private float songChance = 0;

    public MoaSounds(MoaEntity moa) {
        this.moa = moa;
    }

    public void tick() {
        if (moa.getWorld().isClient) {
            return;
        }

        if (moaSoundCallCooldown > 0) {
            moaSoundCallCooldown--;
        } else {
            attemptMoaSound();
        }
    }

    public void setSoundCallCooldown(int cooldown) {
        this.moaSoundCallCooldown = cooldown;
    }

    public void setSongChance(float chance) {
        this.songChance = chance;
    }

    public void playAmbientSound(float volume, float pitch) {
        playSound(ENTITY_MOA_AMBIENT, volume, pitch);
    }

    public void playAmbientSound(boolean isBaby) {
        final float volume = isBaby ? 0.35f : 0.4f;
        final float pitch = isBaby ? getRandomFloat(1f, 1.1f) : getRandomFloat(0.85f, 0.92f);
        playAmbientSound(volume, pitch);
    }

    public void playSingSound() {
        playSound(ENTITY_MOA_AMBIENT_SING, 0.3f, getRandomFloat(0.98f, 1.02f));
    }

    public void playJumpingSound() {
        playSound(ENTITY_MOA_GLIDING, 0.25F, getRandomFloat(0.64f, 0.69f));
    }

    public void playFlapSound() {
        playSound(ENTITY_MOA_GLIDING, 0.9F, getRandomFloat(0.9f, 0.97f));
    }

    public void playStepSound() {
        playSound(ENTITY_MOA_STEP, 0.12F, 1F);
    }

    public void playEatSound(float volume, float pitch) {
        playSound(ENTITY_MOA_EAT, volume, pitch);
    }

    public void playLayingEggSound() {
        playSound(ENTITY_MOA_LAY_EGG, 0.8F, 1.5f);
    }

    public void playHurtSound() {
        playSound(ENTITY_MOA_HURT, 0.2F, getRandomFloat(0.78f, 0.82f));
    }

    public void playDeathSound(float volume, float pitch) {
        playSound(ENTITY_MOA_DEATH, volume, pitch);
    }

    private void attemptMoaSound() {
        final Random random = moa.getRandom();
        if (random.nextFloat() < 0.2f + soundChance) {
            if (random.nextFloat() > 0.05f + songChance || moa.isBaby()) {
                // Small chirp
                moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                songChance += getRandomFloat(0.04f, 0.1f);

                playAmbientSound(moa.isBaby());
            } else {
                // Play little song sometimes so it doesn't get annoying
                moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                songChance = 0;
                playSingSound();
            }
        } else {
            if (moa.isSaddled()) {
                moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                soundChance += getRandomFloat(0.02f, 0.06f);
            } else {
                moaSoundCallCooldown = (int) getRandomFloat(30, 90);
                soundChance += getRandomFloat(0.02f, 0.09f);
            }
        }
    }

    private void playSound(SoundEvent soundEvent, float volume, float pitch) {
        if (!moa.isSilent()) {
            moa.getWorld().playSound(null, moa.getX(), moa.getY(), moa.getZ(), soundEvent, SoundCategory.NEUTRAL, volume, pitch);
        }
    }

    private float getRandomFloat(float min, float max) {
        return moa.getRandomFloat(min, max);
    }
}
