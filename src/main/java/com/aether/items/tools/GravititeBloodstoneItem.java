package com.aether.items.tools;

import com.aether.api.MoaAttributes;
import com.aether.entities.passive.MoaEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class GravititeBloodstoneItem extends BloodstoneItem {
    public GravititeBloodstoneItem(Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> createTooltip(LivingEntity entity) {
        return entity instanceof MoaEntity moa ?
                ImmutableList.of(
                        moa.getAttribute(MoaAttributes.GROUND_SPEED).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        moa.getAttribute(MoaAttributes.GLIDING_SPEED).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        moa.getAttribute(MoaAttributes.GLIDING_DECAY).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        moa.getAttribute(MoaAttributes.JUMPING_STRENGTH).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        moa.getAttribute(MoaAttributes.DROP_MULTIPLIER).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE),
                        moa.getAttribute(MoaAttributes.MAX_HEALTH).formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE)
                )
                :
                ImmutableList.of(
                        new LiteralText("???").formatted(Formatting.ITALIC, Formatting.LIGHT_PURPLE)
                );
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(new TranslatableText("info.aether.bloodstone.gravitite").formatted(Formatting.GOLD));
    }
}
