package net.id.aether.items.tools.bloodstone;

import net.id.aether.api.MoaAPI;
import net.id.aether.entities.passive.moa.MoaAttributes;
import net.id.aether.entities.passive.moa.MoaEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;
import java.util.UUID;

public class BloodstoneCapturedData {
    boolean isMoa = false;
    public String Race = "???";
    public String Hunger = "???";
    public String Affinity = "???";
    public String Owner = "???";

    public String GROUND_SPEED = "???";
    public String GLIDING_SPEED = "???";
    public String GLIDING_DECAY = "???";
    public String JUMPING_STRENGTH = "???";
    public String DROP_MULTIPLIER = "???";
    public String MAX_HEALTH = "???";

    public UUID uuid;
    public Text name = new LiteralText("Unknown Entity");
    public String HP = "???";
    public String DF = "???";
    public String TF = "???";

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
        nbt.putString("name", Text.Serializer.toJson(name));
        nbt.putString("HP", HP);
        nbt.putString("DF", DF);
        nbt.putString("TF", TF);
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
        bloodstoneCapturedData.name = Text.Serializer.fromJson(nbt.getString("name"));
        bloodstoneCapturedData.HP = nbt.getString("HP");
        bloodstoneCapturedData.DF = nbt.getString("DF");
        bloodstoneCapturedData.TF = nbt.getString("TF");
        return bloodstoneCapturedData;
    }

    public static BloodstoneCapturedData fromEntity(LivingEntity entity) {
        BloodstoneCapturedData bloodstoneCapturedData = new BloodstoneCapturedData();
        if (entity instanceof MoaEntity moa) {
            bloodstoneCapturedData.isMoa = true;
            //zanite
            bloodstoneCapturedData.Race = I18n.translate(MoaAPI.formatForTranslation(moa.getGenes().getRace()), "");
            bloodstoneCapturedData.Hunger = String.format("%.1f", moa.getGenes().getHunger()) + "/" + 100.0;
            bloodstoneCapturedData.Affinity = I18n.translate(MoaAPI.formatForTranslation(moa.getGenes().getAffinity()));
            bloodstoneCapturedData.Owner = Optional.ofNullable(moa.getOwner()).map(owner -> owner.getName().asString()).orElse("none");
            //gravitite
            bloodstoneCapturedData.GROUND_SPEED = MoaAttributes.GROUND_SPEED.getRatingTier(moa);
            bloodstoneCapturedData.GLIDING_SPEED = MoaAttributes.GLIDING_SPEED.getRatingTier(moa);
            bloodstoneCapturedData.GLIDING_DECAY = MoaAttributes.GLIDING_DECAY.getRatingTier(moa);
            bloodstoneCapturedData.JUMPING_STRENGTH = MoaAttributes.JUMPING_STRENGTH.getRatingTier(moa);
            bloodstoneCapturedData.DROP_MULTIPLIER = MoaAttributes.DROP_MULTIPLIER.getRatingTier(moa);
            bloodstoneCapturedData.MAX_HEALTH = MoaAttributes.MAX_HEALTH.getRatingTier(moa);
        }
        //Ambrosium
        bloodstoneCapturedData.uuid = entity.getUuid();
        bloodstoneCapturedData.name = entity.getName();
        bloodstoneCapturedData.HP = String.format("%.1f", entity.getHealth()) + "/" + String.format("%.1f", entity.getMaxHealth());
        bloodstoneCapturedData.DF = "" + entity.getArmor();
        bloodstoneCapturedData.TF = "" + MathHelper.floor(entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));

        return bloodstoneCapturedData;
    }

    public Text getRatingWithColor(String rating) {
        MutableText text = new LiteralText(rating);
        return switch (rating) {
            case "F" -> text.formatted(Formatting.DARK_RED);
            case "D" -> text.formatted(Formatting.RED);
            case "C" -> text.formatted(Formatting.YELLOW);
            case "B" -> text.formatted(Formatting.GREEN);
            case "A" -> text.formatted(Formatting.AQUA);
            case "S" -> text.formatted(Formatting.LIGHT_PURPLE);
            case "S+" -> text.formatted(Formatting.GOLD);
            default -> text;
        };
    }
}