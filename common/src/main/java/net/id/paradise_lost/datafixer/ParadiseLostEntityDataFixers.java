package net.id.paradise_lost.datafixer;

import net.minecraft.nbt.CompoundTag;

public final class ParadiseLostEntityDataFixers {
    private ParadiseLostEntityDataFixers() {
    }

    public static void fixEntityTag(CompoundTag entityTag) {
        if (entityTag == null) {
            return;
        }
        CustomBoatDataFix.migrate(entityTag);
        // Moa CCA -> moaGenes is applied again in MoaEntity in case this path is skipped,
        // but doing it here keeps the modern key on the tag for anything that peeks early.
        MoaGenesDataFix.migrate(entityTag);
    }
}
