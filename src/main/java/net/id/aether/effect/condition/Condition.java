package net.id.aether.effect.condition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.registry.AetherRegistries;
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
 *
 * Jack here, these are condition processors.
 * In general, these can all be explained by seeing what VenomCondition does with them,
 * but I'll provide an explanation here anyway.
 * process(), processPlayer(), and processClient() are ticked every tick.
 * All conditions, which extend this class, override these methods with their own effects.
 * e.g. the venom condition applies a poison status effect.
 *
 * The exempt variable is a tag with all entities exempt from the condition.
 * maxTemp is the maximum value for the temporary persistence
 * maxChron is the maximum value for the chronic persistence.
 * tempDecay and chronDecay are how fast these persistences decay.
 * scalingValue is what these maxes are scaled to, for some reason.
 * I've never used visThreshold, so it can't be all too important -
 * If you really want to know, ask Azzy.
 *
 * See the Persistence class for a description of temporary,
 * chronic, and constant persistences are.
 *
 * Hope this helps!
 * ~ Jack.
 */
public abstract class Condition {

    public final Tag<EntityType<?>> exempt;
    public final float maxTemp, maxChron;
    public final float tempDecay, chronDecay;
    public final float scalingValue;
    public final float visThreshold;

    public Condition(Tag<EntityType<?>> exempt, float maxTemp, float maxChron, float tempDecay, float chronDecay, float scalingValue, float visThreshold) {
        this.exempt = exempt;
        this.maxTemp = maxTemp;
        this.maxChron = maxChron;
        this.tempDecay = tempDecay;
        this.chronDecay = chronDecay;
        this.scalingValue = scalingValue;
        this.visThreshold = visThreshold;
    }

    public Identifier getId(){
        return AetherRegistries.CONDITION_REGISTRY.getId(this);
    }

    public boolean isExempt(LivingEntity entity) {
        return exempt.contains(entity.getType());
    }

    public abstract void process(World world, LivingEntity entity, Severity severity, float rawSeverity);

    public abstract void processPlayer(World world, PlayerEntity player, Severity severity, float rawSeverity);

    @Environment(EnvType.CLIENT)
    public abstract void processClient(ClientWorld world, LivingEntity entity, Severity severity, float rawSeverity);
}
