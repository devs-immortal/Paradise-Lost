package net.id.paradiselost.effect.condition;

import net.id.incubus_core.condition.api.Condition;
import net.id.incubus_core.condition.api.Severity;
import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.tag.ParadiseLostEntityTypeTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class VenomCondition extends Condition {

    public VenomCondition() {
        super(ParadiseLostEntityTypeTags.VENOM_IMMUNITY, 300, 300, 0.5F, 0.025F, 400, 0.05F);
    }

    @Override
    public void tick(World world, LivingEntity entity, Severity severity, float rawSeverity) {
        if (rawSeverity > visThreshold && world.getTime() % 20 == 0) {
            var poisonEffect = switch (severity) {
                case MILD -> new StatusEffectInstance(StatusEffects.POISON, 100, 1, true, false, true);
                case ACUTE -> new StatusEffectInstance(StatusEffects.POISON, 100, 2, true, false, true);
                case DIRE, EXTREME -> new StatusEffectInstance(StatusEffects.POISON, 200, 2, true, false, true);
                default -> new StatusEffectInstance(StatusEffects.POISON, 100, 0, true, false, true);
            };
            var witherEffect = switch (severity) {
                case DIRE, EXTREME -> new StatusEffectInstance(StatusEffects.WITHER, 100, 1, true, false, true);
                default -> null;
            };

            entity.addStatusEffect(poisonEffect);
            if (witherEffect != null) {
                entity.addStatusEffect(witherEffect);
            }
        }
    }

    @Override
    public void tickPlayer(World world, PlayerEntity player, Severity severity, float rawSeverity) {
        if (rawSeverity > visThreshold && world.getTime() % 20 == 0) {
            var poisonEffect = switch (severity) {
                case MILD -> new StatusEffectInstance(StatusEffects.POISON, 100, 1, true, false, true);
                case ACUTE -> new StatusEffectInstance(StatusEffects.POISON, 200, 1, true, false, true);
                case DIRE, EXTREME -> new StatusEffectInstance(StatusEffects.POISON, 200, 2, true, false, true);
                default -> new StatusEffectInstance(StatusEffects.POISON, 100, 0, true, false, true);
            };
            var witherEffect = switch (severity) {
                case DIRE -> new StatusEffectInstance(StatusEffects.WITHER, 100, 0, true, false, true);
                case EXTREME -> new StatusEffectInstance(StatusEffects.WITHER, 200, 2, true, false, true);
                default -> null;
            };

            player.addStatusEffect(poisonEffect);
            if (witherEffect != null) {
                player.addStatusEffect(witherEffect);
            }
        }
    }

    @Override
    public void clientTick(ClientWorld world, LivingEntity entity, Severity severity, float rawSeverity) {
        if (severity.isAsOrMoreSevere(Severity.MILD)) {
            var random = world.getRandom();
            var self = MinecraftClient.getInstance().player == entity;
            if (random.nextFloat() < ((severity.isAsOrMoreSevere(Severity.DIRE) ? 0.6 : 0.2) / (self ? 3 : 1))) {
                world.addImportantParticle(ParadiseLostParticles.VENOM_BUBBLE, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), (entity.getVelocity().x / 15) + ((random.nextDouble() * 0.005) - 0.0025), 0.025 + random.nextDouble() * 0.035, (entity.getVelocity().z / 15) + ((random.nextDouble() * 0.005) - 0.0025));
            }
        }
    }
}
