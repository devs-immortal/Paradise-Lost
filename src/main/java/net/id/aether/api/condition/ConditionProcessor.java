package net.id.aether.api.condition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * These are supposed to be flywheel objects ya nut.
 * No you don't get any further documentation, go nag jack.
 * [ - insert gigachad image - ]
 * ~ Azzy
 */
public abstract class ConditionProcessor {

    public final Identifier id;
    public final Tag<EntityType<?>> exempt;
    public final float maxTemp, maxChron;
    public final float tempDecay, chronDecay;
    public final float scalingValue;
    public final float visThreshold;

    public ConditionProcessor(Identifier id, Tag<EntityType<?>> exempt, float maxTemp, float maxChron, float tempDecay, float chronDecay, float scalingValue, float visThreshold) {
        this.id = id;
        this.exempt = exempt;
        this.maxTemp = maxTemp;
        this.maxChron = maxChron;
        this.tempDecay = tempDecay;
        this.chronDecay = chronDecay;
        this.scalingValue = scalingValue;
        this.visThreshold = visThreshold;
    }

    public boolean isExempt(LivingEntity entity) {
        return exempt.contains(entity.getType());
    }

    public abstract void process(World world, LivingEntity entity, Severity severity, float rawSeverity);

    public abstract void processPlayer(World world, PlayerEntity player, Severity severity, float rawSeverity);

    @Environment(EnvType.CLIENT)
    public abstract void processClient(ClientWorld world, LivingEntity entity, Severity severity, float rawSeverity);
}
