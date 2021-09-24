package net.id.aether.component;

import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.api.ConditionAPI;
import net.id.aether.effect.condition.ConditionModifier;
import net.id.aether.effect.condition.ConditionProcessor;
import net.id.aether.effect.condition.Persistance;
import net.id.aether.effect.condition.Severity;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConditionManager implements AutoSyncedComponent, CommonTickingComponent, PlayerComponent<ConditionManager> {

    private final LivingEntity target;
    private final List<Identifier> processors;
    private final HashMap<Identifier, Tracker> conditionTrackers = new HashMap<>();

    public ConditionManager(LivingEntity target) {
        this.target = target;
        processors = ConditionAPI.getValidProcessors(target.getType());
        processors.forEach(id -> {
            conditionTrackers.put(id, new Tracker(AetherRegistries.CONDITION_REGISTRY.get(id)));
        });
    }

    @Override
    public void tick() {
        processors.forEach(id -> {
            var condition = ConditionAPI.getOrThrow(id);

            var tracker = conditionTrackers.get(id);
            float rawSeverity = getScaledSeverity(condition);
            var severity = Severity.getSeverity(rawSeverity);

            if(target instanceof PlayerEntity player) {
                condition.processPlayer(player.world, player, severity, rawSeverity);
            }
            else {
                condition.process(target.world, target, severity, rawSeverity);
            }

            tracker.remove(Persistance.TEMPORARY, getScaledDecay(Persistance.TEMPORARY, condition));
            tracker.remove(Persistance.CHRONIC, getScaledDecay(Persistance.CHRONIC, condition));
        });
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        processors.forEach(id -> {
            var condition = ConditionAPI.getOrThrow(id);

            float rawSeverity = getScaledSeverity(condition);
            var severity = Severity.getSeverity(rawSeverity);

            condition.processClient((ClientWorld) target.world, target, severity, rawSeverity);
        });
    }

    public boolean set(Identifier processorId, Persistance persistance, float value) {
        return Optional.ofNullable(conditionTrackers.get(processorId)).map(tracker -> {
            switch (persistance) {
                case TEMPORARY -> tracker.tempVal = value;
                case CHRONIC -> tracker.chronVal = value;
                case CONSTANT -> throw new IllegalArgumentException("Constant condition values may not be directly edited");
            }
            return true;
        }).orElse(false);
    }

    public void add(Identifier processorId, Persistance persistance, float amount) {
        Optional.ofNullable(conditionTrackers.get(processorId)).ifPresent(tracker -> tracker.add(persistance, amount));
    }

    public void remove(Identifier processorId, Persistance persistance, float amount) {
        Optional.ofNullable(conditionTrackers.get(processorId)).ifPresent(tracker -> tracker.remove(persistance, amount));
    }

    public boolean removeAll(){
        return conditionTrackers.values().stream().allMatch((tracker) ->
                set(tracker.parent.getId(), Persistance.TEMPORARY, 0)
                && set(tracker.parent.getId(), Persistance.CHRONIC, 0));
    }

    public void removeScaled(Identifier processorId, float amount) {
        Optional.ofNullable(conditionTrackers.get(processorId)).ifPresent(tracker -> {
            float partial = tracker.getPartialCondition();
            float tempPart = tracker.tempVal / partial;
            float chronPart = tracker.chronVal / partial;
            tracker.remove(Persistance.TEMPORARY, amount * tempPart);
            tracker.remove(Persistance.CHRONIC, amount * chronPart);
        });
    }

    public boolean isImmuneTo(Identifier processorId) {
        return !processors.contains(processorId);
    }

    public boolean isImmuneTo(ConditionProcessor processor) {
        return !processors.contains(processor.getId());
    }

    public boolean tryApply(Persistance persistance, Identifier processorId, float amount) {
        if(persistance != Persistance.CONSTANT && !isImmuneTo(processorId)) {
            var tracker = conditionTrackers.get(processorId);
            tracker.add(persistance, amount);
            return true;
        }
        return false;
    }

    public float getScaledDecay(Persistance persistance, @NotNull ConditionProcessor processor) {
        return switch (persistance) {
            case TEMPORARY -> processor.tempDecay;
            case CHRONIC -> processor.chronDecay;
            default -> 0;
        } * getDecayMultiplier(processor);
    }

    public float getDecayMultiplier(@NotNull ConditionProcessor processor) {
        var modifiers = getActiveModifiers();
        if(!modifiers.isEmpty()) {
            return (float) modifiers.stream()
                    .mapToDouble(mod -> mod.getDecayMultiplier(processor))
                    .average().orElse(1);
        }
        return 1;
    }

    public float getScaledSeverity(@NotNull ConditionProcessor condition) {
        return MathHelper.clamp((getRawCondition(condition) / getScalingValueForCondition(condition)) * getSeverityMultiplier(condition), 0, 1);
    }

    public float getSeverityMultiplier(@NotNull ConditionProcessor condition) {
        return (float) getActiveModifiers().stream().mapToDouble(mod -> mod.getSeverityMultiplier(condition)).average().orElse(1);
    }

    public float getScalingValueForCondition(@NotNull ConditionProcessor condition) {
        var modifiers = getActiveModifiers();
        float scalingValue = condition.scalingValue;
        scalingValue *= modifiers.stream().mapToDouble(mod -> mod.getScalingMultiplier(condition)).average().orElse(1);
        scalingValue += modifiers.stream().mapToDouble(mod -> mod.getScalingOffset(condition)).sum();
        return scalingValue;
    }

    public float getRawCondition(@NotNull ConditionProcessor condition) {
        return Optional.ofNullable(conditionTrackers.get(condition.getId())).map(tracker -> {
            float partial = tracker.getPartialCondition();
            partial += getActiveModifiers().stream().mapToDouble(mod -> mod.getConstantCondition(condition)).sum();
            return partial;
        }).orElseThrow(() -> new IllegalStateException("HOW in the FUCK do you get an invalid condition here: " + condition.getId()));
    }

    public List<ConditionModifier> getActiveModifiers() {
        List<ConditionModifier> modifiers = new ArrayList<>();
        if(target instanceof PlayerEntity player) {
            var stacks =
                    TrinketsApi.TRINKET_COMPONENT.get(player)
                            .getEquipped(stack -> stack.getItem() instanceof ConditionModifier)
                            .stream().map(Pair::getRight).collect(Collectors.toList());

            player.getArmorItems().forEach(stack -> {
                if(stack.getItem() instanceof ConditionModifier)
                    stacks.add(stack);
            });

            if(player.getMainHandStack().getItem() instanceof ConditionModifier)
                stacks.add(player.getMainHandStack());

            if(player.getOffHandStack().getItem() instanceof ConditionModifier)
                stacks.add(player.getOffHandStack());

            stacks.forEach(stack -> modifiers.add((ConditionModifier) stack.getItem()));
        }

        target.getActiveStatusEffects().forEach((statusEffect, statusEffectInstance) -> {
            if(statusEffect instanceof ConditionModifier modifier) {
                modifiers.add(modifier);
            }
        });

        return modifiers;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        processors.forEach(id -> {
            var tracker = conditionTrackers.get(id);
            if(tag.contains(id.toString())) {
                tracker.fromNbt((NbtCompound) tag.get(id.toString()));
            }
        });
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        processors.forEach(id -> {
            var tracker = conditionTrackers.get(id);
            var nbt = new NbtCompound();
            tracker.writeToNbt(nbt);
            tag.put(id.toString(), nbt);
        });
    }

    @Override
    public void copyFrom(ConditionManager other) {
        PlayerComponent.super.copyFrom(other);
    }

    @Override
    public void copyForRespawn(ConditionManager original, boolean lossless, boolean keepInventory, boolean sameCharacter) {
        if(sameCharacter) {
            PlayerComponent.super.copyForRespawn(original, lossless, keepInventory, sameCharacter);
        }
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
        return true;
    }

    private static class Tracker {

        private final ConditionProcessor parent;

        private float tempVal;
        private float chronVal;

        public Tracker(ConditionProcessor parent) {
            this.parent = parent;
        }

        public void add(Persistance persistance, float amount) {
            switch (persistance) {
                case TEMPORARY -> tempVal = Math.min(parent.maxTemp, tempVal + amount);
                case CHRONIC -> chronVal = Math.min(parent.maxChron, chronVal + amount);
            }
        }

        public void remove(Persistance persistance, float amount) {
            switch (persistance) {
                case TEMPORARY -> tempVal = Math.max(0, tempVal - amount);
                case CHRONIC -> chronVal = Math.max(0, chronVal - amount);
            }
        }

        public float getPartialCondition() {
            return tempVal + chronVal;
        }

        public void fromNbt(NbtCompound nbt) {
            tempVal = nbt.getFloat("temporary");
            chronVal = nbt.getFloat("chronic");
        }

        public void writeToNbt(NbtCompound nbt) {
            nbt.putFloat("temporary", tempVal);
            nbt.putFloat("chronic", chronVal);
        }
    }
}
