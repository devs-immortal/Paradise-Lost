package net.id.paradise_lost.item.tool.bloodstone;

import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
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

                    moa.getGenes().getRaceId(),
                    moa.getGenes().getAffinity().getTranslationKey(),
                    moa.isBaby(),
                    moa.getGenes().getHunger(),
                    moa.getOwner() == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : moa.getOwner().getUUID(),

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

        bloodstoneCapturedData.bloodstoneComponent = new ParadiseLostDataComponentTypes.BloodstoneComponent(
                entity.getUUID(),
                entity.getName(),
                String.format("%.1f", entity.getHealth()) + "/" + String.format("%.1f", entity.getMaxHealth()),
                "" + entity.getArmorValue(),
                "" + Mth.floor(entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS)),
                owner
        );

        return bloodstoneCapturedData;
    }

    public Component getRatingWithColor(String rating) {
        MutableComponent text = Component.translatable(rating);
        return switch (rating) {
            case "moa.attribute.tier.1" -> text.withStyle(ChatFormatting.DARK_RED);
            case "moa.attribute.tier.2" -> text.withStyle(ChatFormatting.RED);
            case "moa.attribute.tier.3" -> text.withStyle(ChatFormatting.YELLOW);
            case "moa.attribute.tier.4" -> text.withStyle(ChatFormatting.GREEN);
            case "moa.attribute.tier.5" -> text.withStyle(ChatFormatting.AQUA);
            case "moa.attribute.tier.6" -> text.withStyle(ChatFormatting.LIGHT_PURPLE);
            case "moa.attribute.tier.7" -> text.withStyle(ChatFormatting.GOLD);
            default -> text;
        };
    }

    public static record ConditionData(String id, float severity) {
        public static ConditionData fromNBT(CompoundTag nbt) {
            return new ConditionData(nbt.getString("id"), nbt.getFloat("severity"));
        }

        public CompoundTag toNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("id", id);
            nbt.putFloat("severity", severity);
            return nbt;
        }
    }
}
