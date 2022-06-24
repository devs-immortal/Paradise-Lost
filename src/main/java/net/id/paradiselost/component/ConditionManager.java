package net.id.paradiselost.component;

import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.api.ConditionAPI;
import net.id.paradiselost.effect.condition.Condition;
import net.id.paradiselost.effect.condition.ConditionModifier;
import net.id.paradiselost.effect.condition.Persistence;
import net.id.paradiselost.effect.condition.Severity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConditionManager implements AutoSyncedComponent, CommonTickingComponent, PlayerComponent<ConditionManager> {

    private final LivingEntity target;
    private final List<ConditionTracker> conditionTrackers = new ArrayList<>();

    public ConditionManager(LivingEntity target) {
        this.target = target;
        var conditions = ConditionAPI.getValidConditions(target.getType());
        conditions.forEach(condition -> conditionTrackers.add(new ConditionTracker(condition)));
    }

    @Override
    public void tick() {
        conditionTrackers.forEach(tracker -> {
            var condition = tracker.getCondition();

            float rawSeverity = getScaledSeverity(condition);
            var severity = Severity.getSeverity(rawSeverity);

            if(target instanceof PlayerEntity player) {
                condition.tickPlayer(player.world, player, severity, rawSeverity);
            }
            else {
                condition.tick(target.world, target, severity, rawSeverity);
            }

            tracker.remove(Persistence.TEMPORARY, getScaledDecay(Persistence.TEMPORARY, condition));
            tracker.remove(Persistence.CHRONIC, getScaledDecay(Persistence.CHRONIC, condition));
        });
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        conditionTrackers.forEach(tracker -> {
            var condition = tracker.getCondition();

            float rawSeverity = getScaledSeverity(condition);
            var severity = Severity.getSeverity(rawSeverity);

            condition.clientTick((ClientWorld) target.world, target, severity, rawSeverity);
        });
    }

    public boolean set(Condition condition, Persistence persistence, float value) {
        return Optional.ofNullable(this.getConditionTracker(condition)).map(tracker -> {
            switch (persistence) {
                case TEMPORARY -> tracker.tempVal = value;
                case CHRONIC -> tracker.chronVal = value;
                case CONSTANT -> throw new IllegalArgumentException("Constant condition values may not be directly edited");
            }
            ConditionAPI.trySync(this.target);
            return true;
        }).orElse(false);
    }

    public void add(Condition condition, Persistence persistence, float amount) {
        Optional.ofNullable(this.getConditionTracker(condition)).ifPresent(tracker -> tracker.add(persistence, amount));
        ConditionAPI.trySync(this.target);
    }

    public void remove(Condition condition, Persistence persistence, float amount) {
        Optional.ofNullable(this.getConditionTracker(condition)).ifPresent(tracker -> tracker.remove(persistence, amount));
        ConditionAPI.trySync(this.target);
    }

    public boolean removeAll(){
        return conditionTrackers.stream().allMatch((tracker) ->
                set(tracker.parent, Persistence.TEMPORARY, 0)
                && set(tracker.parent, Persistence.CHRONIC, 0));
    }

    public void removeScaled(Condition condition, float amount) {
        Optional.ofNullable(this.getConditionTracker(condition)).ifPresent(tracker -> {
            float partial = tracker.getPartialCondition();
            float tempPart = tracker.tempVal / partial;
            float chronPart = tracker.chronVal / partial;
            tracker.remove(Persistence.TEMPORARY, amount * tempPart);
            tracker.remove(Persistence.CHRONIC, amount * chronPart);
        });
    }

    public boolean isImmuneTo(Condition condition) {
        return conditionTrackers.stream().noneMatch(tracker -> tracker.getCondition() == condition);
    }

    private ConditionTracker getConditionTracker(Condition condition){
        for (var tracker : conditionTrackers) {
            if (tracker.getCondition() == condition){
                return tracker;
            }
        }
        return null;
    }

    public boolean tryApply(Condition condition, Persistence persistence, float amount) {
        var tracker = this.getConditionTracker(condition);
        if(tracker != null && persistence != Persistence.CONSTANT && !isImmuneTo(condition)) {
            tracker.add(persistence, amount);
            return true;
        }
        return false;
    }

    public float getScaledDecay(Persistence persistence, @NotNull Condition condition) {
        return switch (persistence) {
            case TEMPORARY -> condition.tempDecay;
            case CHRONIC -> condition.chronDecay;
            default -> 0;
        } * getDecayMultiplier(condition);
    }

    public float getDecayMultiplier(@NotNull Condition condition) {
        var modifiers = getActiveModifiers();
        if(!modifiers.isEmpty()) {
            return (float) modifiers.stream()
                    .mapToDouble(mod -> mod.getDecayMultiplier(condition))
                    .average().orElse(1);
        }
        return 1;
    }

    public float getScaledSeverity(@NotNull Condition condition) {
        return MathHelper.clamp((getRawCondition(condition) / getScalingValueForCondition(condition)) * getSeverityMultiplier(condition), 0, 1);
    }

    public float getSeverityMultiplier(@NotNull Condition condition) {
        return (float) getActiveModifiers().stream().mapToDouble(mod -> mod.getSeverityMultiplier(condition)).average().orElse(1);
    }

    public float getScalingValueForCondition(@NotNull Condition condition) {
        var modifiers = getActiveModifiers();
        float scalingValue = condition.scalingValue;
        scalingValue *= modifiers.stream().mapToDouble(mod -> mod.getScalingMultiplier(condition)).average().orElse(1);
        scalingValue += modifiers.stream().mapToDouble(mod -> mod.getScalingOffset(condition)).sum();
        return scalingValue;
    }

    public float getRawCondition(@NotNull Condition condition) {
        return Optional.ofNullable(this.getConditionTracker(condition)).map(tracker -> {
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
                if(stack.getItem() instanceof ConditionModifier) {
                    stacks.add(stack);
                }
            });

            if(player.getMainHandStack().getItem() instanceof ConditionModifier) {
                stacks.add(player.getMainHandStack());
            }

            if(player.getOffHandStack().getItem() instanceof ConditionModifier) {
                stacks.add(player.getOffHandStack());
            }

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
        conditionTrackers.forEach(tracker -> {
            var condition = tracker.getCondition();
            if(tag.contains(condition.getId().toString())) {
                tracker.fromNbt((NbtCompound) tag.get(condition.getId().toString()));
            }
        });
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        conditionTrackers.forEach(tracker -> {
            var nbt = new NbtCompound();
            tracker.writeToNbt(nbt);
            tag.put(tracker.getCondition().getId().toString(), nbt);
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
        return false;
    }

    private static class ConditionTracker {

        private final Condition parent;

        private float tempVal;
        private float chronVal;

        public ConditionTracker(Condition parent) {
            this.parent = parent;
        }

        public Condition getCondition(){
            return parent;
        }

        public void add(Persistence persistence, float amount) {
            switch (persistence) {
                case TEMPORARY -> tempVal = Math.min(parent.maxTemp, tempVal + amount);
                case CHRONIC -> chronVal = Math.min(parent.maxChron, chronVal + amount);
            }
        }

        public void remove(Persistence persistence, float amount) {
            switch (persistence) {
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
