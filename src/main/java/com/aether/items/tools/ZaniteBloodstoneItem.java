package com.aether.items.tools;

import com.aether.api.MoaAPI;
import com.aether.entities.passive.MoaEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Optional;

public class ZaniteBloodstoneItem extends BloodstoneItem {
    @Override
    protected List<Text> createTooltip(LivingEntity entity) {
        return entity instanceof MoaEntity moa ?
                ImmutableList.of(
                        new LiteralText("Race: " + I18n.translate(MoaAPI.formatForTranslation(moa.getGenes().getRace().id()), "")).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        new LiteralText("Hunger: " + String.format("%.1f", moa.getGenes().getHunger()) + "/" + 100.0).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        new LiteralText("Affinity: " + I18n.translate(MoaAPI.formatForTranslation(moa.getGenes().getAffinity()))).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        new LiteralText("Owner: " + Optional.ofNullable(moa.getOwner()).map(owner -> owner.getName().asString()).orElse("none")).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE)
                )
                :
                ImmutableList.of(
                        new LiteralText("???").formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE)
                );
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(new TranslatableText("info.aether.bloodstone.zanite").formatted(Formatting.GOLD));
    }
}
