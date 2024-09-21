package net.id.paradiselost.items.tools.bloodstone;

import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BloodstoneCapturedData {
    boolean isMoa = false;
    public String Race = "???"; // translation key
    public String Hunger = "???";
    public String Affinity = "???"; // translation key
    public String Owner = "???";

    public String GROUND_SPEED = "???";
    public String GLIDING_SPEED = "???";
    public String GLIDING_DECAY = "???";
    public String JUMPING_STRENGTH = "???";
    public String DROP_MULTIPLIER = "???";
    public String MAX_HEALTH = "???";

    public UUID uuid;
    public Text name = Text.literal("Unknown Entity");
    public String HP = "???";
    public String DF = "???";
    public String TF = "???";

    public List<ConditionData> conditionDataList = new ArrayList<>();

    public BloodstoneCapturedData() {
    }

    public static final String NBT_TAG = "capturedEntityData";

    public NbtCompound toNBT() {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("isMoa", isMoa);
        if (isMoa) {
            nbt.putString("Race", Race);
            nbt.putString("Hunger", Hunger);
            nbt.putString("Affinity", Affinity);
            nbt.putString("Owner", Owner);

            nbt.putString("GROUND_SPEED", GROUND_SPEED);
            nbt.putString("GLIDING_SPEED", GLIDING_SPEED);
            nbt.putString("GLIDING_DECAY", GLIDING_DECAY);
            nbt.putString("JUMPING_STRENGTH", JUMPING_STRENGTH);
            nbt.putString("DROP_MULTIPLIER", DROP_MULTIPLIER);
            nbt.putString("MAX_HEALTH", MAX_HEALTH);
        }
        nbt.putUuid("uuid", uuid);
        nbt.putString("name", Text.Serialization.toJsonTree(name).getAsString());
        nbt.putString("HP", HP);
        nbt.putString("DF", DF);
        nbt.putString("TF", TF);

        if (conditionDataList.size() > 0) {
            NbtList condNBTList = new NbtList();
            conditionDataList.forEach(conditionData -> condNBTList.add(conditionData.toNBT()));
            nbt.put("condNBTList", condNBTList);
        }
        return nbt;
    }

    public static BloodstoneCapturedData fromNBT(NbtCompound nbt) {
        BloodstoneCapturedData bloodstoneCapturedData = new BloodstoneCapturedData();
        boolean isMoa = nbt.getBoolean("isMoa");
        if (isMoa) {
            bloodstoneCapturedData.Race = nbt.getString("Race");
            bloodstoneCapturedData.Hunger = nbt.getString("Hunger");
            bloodstoneCapturedData.Affinity = nbt.getString("Affinity");
            bloodstoneCapturedData.Owner = nbt.getString("Owner");

            bloodstoneCapturedData.GROUND_SPEED = nbt.getString("GROUND_SPEED");
            bloodstoneCapturedData.GLIDING_SPEED = nbt.getString("GLIDING_SPEED");
            bloodstoneCapturedData.GLIDING_DECAY = nbt.getString("GLIDING_DECAY");
            bloodstoneCapturedData.JUMPING_STRENGTH = nbt.getString("JUMPING_STRENGTH");
            bloodstoneCapturedData.DROP_MULTIPLIER = nbt.getString("DROP_MULTIPLIER");
            bloodstoneCapturedData.MAX_HEALTH = nbt.getString("MAX_HEALTH");
        }
        bloodstoneCapturedData.uuid = nbt.getUuid("uuid");
        bloodstoneCapturedData.name = Text.Serialization.fromJson(nbt.getString("name"));
        bloodstoneCapturedData.HP = nbt.getString("HP");
        bloodstoneCapturedData.DF = nbt.getString("DF");
        bloodstoneCapturedData.TF = nbt.getString("TF");

        if (nbt.contains("condNBTList")) {
            NbtList condNBTList = (NbtList) nbt.get("condNBTList");
            condNBTList.forEach(nbtElement -> bloodstoneCapturedData.conditionDataList.add(ConditionData.fromNBT((NbtCompound) nbtElement)));
        }
        return bloodstoneCapturedData;
    }

    public static BloodstoneCapturedData fromEntity(LivingEntity entity) {
        BloodstoneCapturedData bloodstoneCapturedData = new BloodstoneCapturedData();
        if (entity instanceof MoaEntity moa) {
            bloodstoneCapturedData.isMoa = true;
            //olvite
            bloodstoneCapturedData.Race = moa.getGenes().getRace().getTranslationKey();
            bloodstoneCapturedData.Hunger = String.format("%.1f", moa.getGenes().getHunger()) + "/" + 100.0;
            bloodstoneCapturedData.Affinity = moa.getGenes().getAffinity().getTranslationKey();
            bloodstoneCapturedData.Owner = Optional.ofNullable(moa.getOwner()).map(owner -> owner.getName().getString()).orElse("none");
            //gravitite
            bloodstoneCapturedData.GROUND_SPEED = MoaAttributes.GROUND_SPEED.getRatingTierTranslationKey(moa);
            bloodstoneCapturedData.GLIDING_SPEED = MoaAttributes.GLIDING_SPEED.getRatingTierTranslationKey(moa);
            bloodstoneCapturedData.GLIDING_DECAY = MoaAttributes.GLIDING_DECAY.getRatingTierTranslationKey(moa);
            bloodstoneCapturedData.JUMPING_STRENGTH = MoaAttributes.JUMPING_STRENGTH.getRatingTierTranslationKey(moa);
            bloodstoneCapturedData.DROP_MULTIPLIER = MoaAttributes.DROP_MULTIPLIER.getRatingTierTranslationKey(moa);
            bloodstoneCapturedData.MAX_HEALTH = MoaAttributes.MAX_HEALTH.getRatingTierTranslationKey(moa);
        }
        //Cherine
        bloodstoneCapturedData.uuid = entity.getUuid();
        bloodstoneCapturedData.name = entity.getName();
        bloodstoneCapturedData.HP = String.format("%.1f", entity.getHealth()) + "/" + String.format("%.1f", entity.getMaxHealth());
        bloodstoneCapturedData.DF = "" + entity.getArmor();
        bloodstoneCapturedData.TF = "" + MathHelper.floor(entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));

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
