package net.id.paradise_lost.datafixer;

import net.id.paradise_lost.ModConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public final class MoaGenesDataFix {
    public static final String MOA_GENES_KEY = "moaGenes";

    private MoaGenesDataFix() {
    }

    public static boolean migrate(CompoundTag entityTag) {
        if (entityTag == null || entityTag.contains(MOA_GENES_KEY, Tag.TAG_COMPOUND)) {
            return false;
        }
        CompoundTag legacy = LegacyCardinalComponents.get(entityTag, LegacyCardinalComponents.MOA_GENETICS);
        if (legacy == null || legacy.isEmpty()) {
            return false;
        }
        entityTag.put(MOA_GENES_KEY, legacy.copy());
        LegacyCardinalComponents.remove(entityTag, LegacyCardinalComponents.MOA_GENETICS);
        ModConstants.LOGGER.debug("Migrated CCA moa_genetics -> moaGenes");
        return true;
    }
}
