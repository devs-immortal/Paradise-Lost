package net.id.paradiselost.items.tools.bloodstone;

import net.id.paradiselost.api.MoaAPI;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.entities.passive.moa.MoaRaces;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BloodstoneCapturedData {
    boolean isMoa = false;
    public ParadiseLostDataComponentTypes.MoaGeneComponent moaGeneComponent;
    public ParadiseLostDataComponentTypes.BloodstoneComponent bloodstoneComponent;

    public BloodstoneCapturedData() {
    }

    public static BloodstoneCapturedData fromComponents(ItemStack bs) {
        BloodstoneCapturedData bloodstoneCapturedData = new BloodstoneCapturedData();
        bloodstoneCapturedData.moaGeneComponent = bs.getOrDefault(ParadiseLostDataComponentTypes.MOA_GENES, null);
        bloodstoneCapturedData.bloodstoneComponent = bs.getOrDefault(ParadiseLostDataComponentTypes.BLOODSTONE, null);
        bloodstoneCapturedData.isMoa = bloodstoneCapturedData.moaGeneComponent != null;

        return bloodstoneCapturedData;
    }

    public static BloodstoneCapturedData fromEntity(LivingEntity entity) {
        BloodstoneCapturedData bloodstoneCapturedData = new BloodstoneCapturedData();
        String owner = "none";
        if (entity instanceof MoaEntity moa) {
            if (moa.getOwner() != null) {
                owner = moa.getOwner().getName().getString();
            }
            bloodstoneCapturedData.isMoa = true;
            bloodstoneCapturedData.moaGeneComponent = new ParadiseLostDataComponentTypes.MoaGeneComponent(
                    //olvite
                    moa.getGenes().getRace().getId(),
                    moa.getGenes().getAffinity().getTranslationKey(),
                    moa.isBaby(),
                    moa.getGenes().getHunger(),
                    moa.getOwner() == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : moa.getOwner().getUuid(),
                    //surtrum
                    new ParadiseLostDataComponentTypes.MoaAttributeComponent(
                            moa.getGenes().getAttribute(MoaAttributes.GROUND_SPEED),
                            moa.getGenes().getAttribute(MoaAttributes.GLIDING_SPEED),
                            moa.getGenes().getAttribute(MoaAttributes.GLIDING_DECAY),
                            moa.getGenes().getAttribute(MoaAttributes.JUMPING_STRENGTH),
                            moa.getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER),
                            moa.getGenes().getAttribute(MoaAttributes.MAX_HEALTH)
                    )
            );
        }
        //Cherine
        bloodstoneCapturedData.bloodstoneComponent = new ParadiseLostDataComponentTypes.BloodstoneComponent(
                entity.getUuid(),
                entity.getName(),
                String.format("%.1f", entity.getHealth()) + "/" + String.format("%.1f", entity.getMaxHealth()),
                "" + entity.getArmor(),
                "" + MathHelper.floor(entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)),
                owner
        );

        return bloodstoneCapturedData;
    }

    public Text getRatingWithColor(String rating) {
        MutableText text = Text.translatable(rating);
        return switch (rating) {
            case "moa.attribute.tier.1" -> text.formatted(Formatting.DARK_RED);
            case "moa.attribute.tier.2" -> text.formatted(Formatting.RED);
            case "moa.attribute.tier.3" -> text.formatted(Formatting.YELLOW);
            case "moa.attribute.tier.4" -> text.formatted(Formatting.GREEN);
            case "moa.attribute.tier.5" -> text.formatted(Formatting.AQUA);
            case "moa.attribute.tier.6" -> text.formatted(Formatting.LIGHT_PURPLE);
            case "moa.attribute.tier.7" -> text.formatted(Formatting.GOLD);
            default -> text;
        };
    }

    public static record ConditionData(String id, float severity) {
        public static ConditionData fromNBT(NbtCompound nbt) {
            return new ConditionData(nbt.getString("id"), nbt.getFloat("severity"));
        }

        public NbtCompound toNBT() {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", id);
            nbt.putFloat("severity", severity);
            return nbt;
        }
    }
}
