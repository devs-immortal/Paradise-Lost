package net.id.paradise_lost.datafixer;

import net.id.paradise_lost.PLConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.Map;

public final class CustomBoatDataFix {
    private static final Map<String, String> WOOD_BY_TYPE = Map.of(
            "paradise_lost_aurel", "aurel",
            "paradise_lost_mother_aurel", "mother_aurel",
            "paradise_lost_menth", "menth",
            "paradise_lost_wisteria", "wisteria"
    );

    private CustomBoatDataFix() {
    }

    public static boolean migrate(CompoundTag entityTag) {
        if (entityTag == null || !entityTag.contains("id", Tag.TAG_STRING)) {
            return false;
        }
        String rawId = entityTag.getString("id");
        ResourceLocation id = ResourceLocation.tryParse(rawId);
        if (id == null) {
            return false;
        }
        boolean chest = isChestBoat(id);
        if (!chest && !isBoat(id)) {
            return false;
        }
        if (!entityTag.contains("Type", Tag.TAG_STRING)) {
            return false;
        }
        String type = normalizeType(entityTag.getString("Type"));
        String wood = WOOD_BY_TYPE.get(type);
        if (wood == null) {
            return false;
        }
        ResourceLocation replacement = PLConstants.id(wood + (chest ? "_chest_boat" : "_boat"));
        entityTag.putString("id", replacement.toString());
        entityTag.remove("Type");
        PLConstants.LOGGER.debug("Migrated legacy boat Type {} -> {}", type, replacement);
        return true;
    }

    private static boolean isBoat(ResourceLocation id) {
        return id.equals(ResourceLocation.withDefaultNamespace("boat"));
    }

    private static boolean isChestBoat(ResourceLocation id) {
        return id.equals(ResourceLocation.withDefaultNamespace("chest_boat"));
    }

    private static String normalizeType(String type) {
        String normalized = type.trim().toLowerCase(Locale.ROOT);
        // Enum constant names from the 2.4 early-riser, just in case they were written raw.
        return switch (normalized) {
            case "paradise_lost_aurel", "paradiselost_aurel" -> "paradise_lost_aurel";
            case "paradise_lost_mother_aurel", "paradiselost_mother_aurel" -> "paradise_lost_mother_aurel";
            case "paradise_lost_menth", "paradiselost_menth" -> "paradise_lost_menth";
            case "paradise_lost_wisteria", "paradiselost_wisteria" -> "paradise_lost_wisteria";
            default -> normalized;
        };
    }
}
