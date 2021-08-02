package com.aether.items.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AmbrosiumBloodstoneItem extends BloodstoneItem {
    public AmbrosiumBloodstoneItem(Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> createTooltip(LivingEntity entity) {
        return ImmutableList.of(
                new LiteralText("HP: " + String.format("%.1f", entity.getHealth()) + "/" + String.format("%.1f", entity.getMaxHealth())).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                new LiteralText("DF: " + entity.getArmor()).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                new LiteralText("TF: " + MathHelper.floor(entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE)
        );
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(new TranslatableText("info.aether.bloodstone.ambrosium").formatted(Formatting.GOLD));
    }
}
