package net.id.paradiselost.entities;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.block.FloatingBlockEntity;
import net.id.paradiselost.entities.block.SliderEntity;
import net.id.paradiselost.entities.hostile.AechorPlantEntity;
import net.id.paradiselost.entities.hostile.CockatriceEntity;
import net.id.paradiselost.entities.hostile.swet.*;
import net.id.paradiselost.entities.misc.RookEntity;
import net.id.paradiselost.entities.passive.AerbunnyEntity;
import net.id.paradiselost.entities.passive.AerwhaleEntity;
import net.id.paradiselost.entities.passive.ParadiseLostAnimalEntity;
import net.id.paradiselost.entities.passive.ambyst.FindLogSensor;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.entities.projectile.*;
import net.id.paradiselost.mixin.brain.ActivityInvoker;
import net.id.paradiselost.mixin.brain.MemoryModuleTypeInvoker;
import net.id.paradiselost.mixin.brain.SensorTypeInvoker;
import net.id.paradiselost.registry.ParadiseLostRegistryQueues;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.util.Set;
import java.util.function.Supplier;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.minecraft.entity.EntityDimensions.changing;
import static net.minecraft.entity.EntityDimensions.fixed;
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
    public static final EntityType<BlueSwetEntity> BLUE_SWET = add("blue_swet", of(BlueSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(BlueSwetEntity::createSwetAttributes), spawnRestrictions(MobEntity::canMobSpawn));
    public static final EntityType<PurpleSwetEntity> PURPLE_SWET = add("purple_swet", of(PurpleSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(PurpleSwetEntity::createSwetAttributes), spawnRestrictions(MobEntity::canMobSpawn));
    public static final EntityType<WhiteSwetEntity> WHITE_SWET = add("white_swet", of(WhiteSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(WhiteSwetEntity::createSwetAttributes), spawnRestrictions(MobEntity::canMobSpawn));
    public static final EntityType<GoldenSwetEntity> GOLDEN_SWET = add("golden_swet", of(GoldenSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(GoldenSwetEntity::createSwetAttributes), spawnRestrictions(MobEntity::canMobSpawn));
    public static final EntityType<VermilionSwetEntity> VERMILION_SWET = add("vermilion_swet", of(VermilionSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(VermilionSwetEntity::createSwetAttributes), spawnRestrictions(VermilionSwetEntity::canSpawn));
    // TODO: why is this an animal? should extend hostile to allow hostile spawn restrictions, and inherit hostile behviour
    public static final EntityType<AechorPlantEntity> AECHOR_PLANT = add("aechor_plant", of(AechorPlantEntity::new, MONSTER, changing(1f, 1f), 5),
            attributes(AechorPlantEntity::createAechorPlantAttributes), spawnRestrictions(AechorPlantEntity::canSpawn));
    // public static final EntityType<ChestMimicEntity> CHEST_MIMIC = add("chest_mimic", of(ChestMimicEntity::new, MONSTER, changing(1.0F, 2.0F), 5),
    //        attributes(ChestMimicEntity::createChestMimicAttributes), spawnRestrictions(HostileEntity::canSpawnInDark));
    public static final EntityType<CockatriceEntity> COCKATRICE = add("cockatrice", of(CockatriceEntity::new, MONSTER, changing(1.0F, 2.0F), 5),
            attributes(CockatriceEntity::createCockatriceAttributes), spawnRestrictions(CockatriceEntity::canSpawn));
    // passive
    public static final EntityType<MoaEntity> MOA = add("moa", of(MoaEntity::new, CREATURE, changing(1.0F, 2.0F), 5),
            attributes(MoaEntity::createMoaAttributes), spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn));
    public static final EntityType<AerbunnyEntity> AERBUNNY = add("aerbunny", of(AerbunnyEntity::new, CREATURE, changing(0.55F, 0.55F), 5),
            attributes(AerbunnyEntity::createAerbunnyAttributes), spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn));
    public static final EntityType<AerwhaleEntity> AERWHALE = add("aerwhale", of(AerwhaleEntity::new, CREATURE, changing(3.0F, 1.2F), 5),
            attributes(AerwhaleEntity::createAerwhaleAttributes), spawnRestrictions(MobEntity::canMobSpawn));
    public static final EntityType<RookEntity> ROOK = add("rook", of(RookEntity::new, MISC, fixed(0.75F, 1.8F), 5),
            attributes(RookEntity::createRookAttributes), spawnRestrictions((type, world, spawnReason, pos, random) -> false));
    // public static final EntityType<AmbystEntity> AMBYST = add("ambyst", of(AmbystEntity::new, CREATURE, changing(0.6F, 0.42F), 5),
    //         attributes(AmbystEntity::createAmbystAttributes), spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn));

    // Projectile
    public static final EntityType<CockatriceSpitEntity> COCKATRICE_SPIT = add("cockatrice_spit", of(CockatriceSpitEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<GoldenDartEntity> GOLDEN_DART = add("golden_dart", of(GoldenDartEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<EnchantedDartEntity> ENCHANTED_DART = add("enchanted_dart", of(EnchantedDartEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<PoisonDartEntity> POISON_DART = add("poison_dart", of(PoisonDartEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<PoisonNeedleEntity> POISON_NEEDLE = add("poison_needle", of(PoisonNeedleEntity::new, MISC, changing(0.5F, 0.5F), 5));


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

    private static Action<? super EntityType<? extends LivingEntity>> attributes(Supplier<DefaultAttributeContainer.Builder> builder) {
        return (id, entityType) -> FabricDefaultAttributeRegistry.register(entityType, builder.get());
    }

    private static <T extends MobEntity> Action<EntityType<T>> spawnRestrictions(SpawnRestriction.Location location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
        return (id, entityType) -> SpawnRestriction.register(entityType, location, heightmapType, predicate);
    }

    private static <T extends MobEntity> Action<EntityType<T>> spawnRestrictions(SpawnRestriction.Location location, SpawnRestriction.SpawnPredicate<T> predicate) {
        return spawnRestrictions(location, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends MobEntity> Action<EntityType<T>> spawnRestrictions(SpawnRestriction.SpawnPredicate<T> predicate) {
        return spawnRestrictions(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    public static void init() {
        ParadiseLostRegistryQueues.ENTITY_TYPE.register();
    }

    @SafeVarargs
    private static <V extends EntityType<?>> V add(String id, V type, Action<? super V>... additionalActions) {
        return ParadiseLostRegistryQueues.ENTITY_TYPE.add(locate(id), type, additionalActions);
    }

    @SafeVarargs
    private static <E extends Entity> EntityType<E> add(String id, FabricEntityTypeBuilder<E> builder, Action<? super EntityType<E>>... additionalActions) {
        return add(id, builder.build(), additionalActions);
    }

    public static <T extends Entity> FabricEntityTypeBuilder<T> of(EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, EntityDimensions dimensions, int trackingRange) {
        return FabricEntityTypeBuilder.create(spawnGroup, factory).dimensions(dimensions).trackRangeChunks(trackingRange);
    }
}
