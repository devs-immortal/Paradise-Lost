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

public class AbstentineBloodstoneItem extends BloodstoneItem {
    @Override
    protected List<Text> createTooltip(LivingEntity entity) {
        return ImmutableList.of(
                new LiteralText("Dear oh dear, what was it? The hunt, the blood, or the horrible dream?").formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE)
        );
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(new TranslatableText("info.aether.bloodstone.abstentine").formatted(Formatting.GOLD));
    }
}
