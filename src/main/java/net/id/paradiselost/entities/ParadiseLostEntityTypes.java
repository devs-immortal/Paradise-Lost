package net.id.paradiselost.entities;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.block.FloatingBlockEntity;
import net.id.paradiselost.entities.block.SliderEntity;
import net.id.paradiselost.entities.hostile.EnvoyEntity;
import net.id.paradiselost.entities.passive.ParadiseLostAnimalEntity;
import net.id.paradiselost.entities.passive.ambyst.FindLogSensor;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.entities.projectile.ThrownNitraEntity;
import net.id.paradiselost.mixin.brain.ActivityInvoker;
import net.id.paradiselost.mixin.brain.MemoryModuleTypeInvoker;
import net.id.paradiselost.mixin.brain.SensorTypeInvoker;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.minecraft.entity.EntityDimensions.changing;
import static net.minecraft.entity.SpawnGroup.*;

@SuppressWarnings({"unused", "SameParameterValue"})
public class ParadiseLostEntityTypes {
    /*
    Begin entity types
     */
    // Block
    public static final EntityType<FloatingBlockEntity> FLOATING_BLOCK = add("floating_block",
            ParadiseLostEntityTypes.<FloatingBlockEntity>of(FloatingBlockEntity::new, MISC, changing(0.98F, 0.98F), 10).trackedUpdateRate(20));
    public static final EntityType<SliderEntity> SLIDER = add("slider",
            ParadiseLostEntityTypes.<SliderEntity>of(SliderEntity::new, MISC, changing(0.98F, 0.98F), 10).trackedUpdateRate(20));

    // Hostile
    public static final EntityType<EnvoyEntity> ENVOY = add("envoy", of(EnvoyEntity::new, MONSTER, changing(0.6F, 1.95F), 10),
            attributes(EnvoyEntity::createEnvoyAttributes), spawnRestrictions(EnvoyEntity::canMobSpawn));

    // passive
    public static final EntityType<MoaEntity> MOA = add("moa", of(MoaEntity::new, CREATURE, changing(1.0F, 2.0F), 5),
            attributes(MoaEntity::createMoaAttributes), spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn));
    // public static final EntityType<AmbystEntity> AMBYST = add("ambyst", of(AmbystEntity::new, CREATURE, changing(0.6F, 0.42F), 5),
    //         attributes(AmbystEntity::createAmbystAttributes), spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn));

    // projectile
    public static final EntityType<ThrownNitraEntity> THROWN_NITRA = add("thrown_nitra", of(ThrownNitraEntity::new, MISC, changing(0.5F, 0.5F), 5));

    //Brain
    public static final Activity HIDEINLOG = ActivityInvoker.invokeRegister(ParadiseLost.locate("hideinlog").toString());

    public static final MemoryModuleType<Boolean> IS_RAINING_MEMORY = MemoryModuleTypeInvoker.invokeRegister(ParadiseLost.locate("is_raining").toString(), Codec.BOOL);
    public static final MemoryModuleType<BlockPos> LOG_MEMORY = MemoryModuleTypeInvoker.invokeRegister(ParadiseLost.locate("log").toString(), BlockPos.CODEC);
    public static final MemoryModuleType<BlockPos> LOG_OPENING_MEMORY = MemoryModuleTypeInvoker.invokeRegister(ParadiseLost.locate("log_opening").toString(), BlockPos.CODEC);

    public static final SensorType<FindLogSensor> FINDLOG_SENSOR = SensorTypeInvoker.invokeRegister(ParadiseLost.locate("find_log").toString(), () -> new FindLogSensor(6, 20));
    public static final SensorType<Sensor<AnimalEntity>> WEATHER_SENSOR = SensorTypeInvoker.invokeRegister(ParadiseLost.locate("weather").toString(), () -> new Sensor<>(100) {
        protected void sense(ServerWorld world, AnimalEntity entity) {
            entity.getBrain().remember(ParadiseLostEntityTypes.IS_RAINING_MEMORY, world.isRaining());
        }

        public Set<MemoryModuleType<?>> getOutputMemoryModules() {
            return ImmutableSet.of(ParadiseLostEntityTypes.IS_RAINING_MEMORY);
        }
    });

    private static Consumer<? super EntityType<? extends LivingEntity>> attributes(Supplier<DefaultAttributeContainer.Builder> builder) {
        return (entityType) -> FabricDefaultAttributeRegistry.register(entityType, builder.get());
    }

    private static <T extends MobEntity> Consumer<EntityType<T>> spawnRestrictions(SpawnRestriction.Location location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
        return (entityType) -> SpawnRestriction.register(entityType, location, heightmapType, predicate);
    }

    private static <T extends MobEntity> Consumer<EntityType<T>> spawnRestrictions(SpawnRestriction.Location location, SpawnRestriction.SpawnPredicate<T> predicate) {
        return spawnRestrictions(location, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends MobEntity> Consumer<EntityType<T>> spawnRestrictions(SpawnRestriction.SpawnPredicate<T> predicate) {
        return spawnRestrictions(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    public static void init() {}

    @SafeVarargs
    private static <V extends EntityType<?>> V add(String id, V type, Consumer<? super V>... additionalActions) {
        return Registry.register(Registries.ENTITY_TYPE, locate(id), type);
    }

    @SafeVarargs
    private static <E extends Entity> EntityType<E> add(String id, FabricEntityTypeBuilder<E> builder, Consumer<? super EntityType<E>>... additionalActions) {
        var ent = add(id, builder.build(), additionalActions);
        for (var action : additionalActions) {
            action.accept(ent);
        }
        return ent;
    }

    public static <T extends Entity> FabricEntityTypeBuilder<T> of(EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, EntityDimensions dimensions, int trackingRange) {
        return FabricEntityTypeBuilder.create(spawnGroup, factory).dimensions(dimensions).trackRangeChunks(trackingRange);
    }
}
