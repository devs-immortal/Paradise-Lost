package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.entity.block.SliderEntity;
import net.id.paradise_lost.entity.hostile.EnvoyEntity;
import net.id.paradise_lost.entity.hostile.SentinelEntity;
import net.id.paradise_lost.entity.passive.ParadiseLostAnimalEntity;
import net.id.paradise_lost.entity.passive.PopomEntity;
import net.id.paradise_lost.entity.passive.QuintEntity;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.id.paradise_lost.entity.projectile.ThrownNitraEntity;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.entity.EntityDimensions.scalable;
import static net.minecraft.world.entity.MobCategory.CREATURE;
import static net.minecraft.world.entity.MobCategory.MISC;
import static net.minecraft.world.entity.MobCategory.MONSTER;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class EntityRegistry {
    public static final RegistrationProvider<EntityType<?>> ENTITY_TYPES =
            RegistrationProvider.get(Registries.ENTITY_TYPE, ModConstants.MODID);

    private static final List<AttributeRegistration> ATTRIBUTE_REGISTRATIONS = new ArrayList<>();
    private static final List<Runnable> SPAWN_REGISTRATIONS = new ArrayList<>();

    public static final RegistryObject<EntityType<?>, EntityType<FloatingBlockEntity>> FLOATING_BLOCK = add("floating_block",
            EntityType.Builder.<FloatingBlockEntity>of((type, level) -> new FloatingBlockEntity(type, level), MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(20));
    public static final RegistryObject<EntityType<?>, EntityType<SliderEntity>> SLIDER = add("slider",
            EntityType.Builder.<SliderEntity>of((type, level) -> new SliderEntity(type, level), MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(20));

    public static final RegistryObject<EntityType<?>, EntityType<EnvoyEntity>> ENVOY = add("envoy",
            of(EnvoyEntity::new, MONSTER, scalable(0.6F, 1.95F), 10),
            attributes(EnvoyEntity::createEnvoyAttributes),
            spawnRestrictions(Monster::checkMonsterSpawnRules));
    public static final RegistryObject<EntityType<?>, EntityType<SentinelEntity>> SENTINEL = add("sentinel",
            of(SentinelEntity::new, MONSTER, scalable(0.6F, 2.25F), 16),
            attributes(SentinelEntity::createSentinelAttributes),
            spawnRestrictions(SentinelEntity::noSpawn));
    public static final RegistryObject<EntityType<?>, EntityType<MoaEntity>> MOA = add("moa",
            of(MoaEntity::new, CREATURE, scalable(0.8F, 1.9F), 5),
            attributes(MoaEntity::createMoaAttributes),
            spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn));
    public static final RegistryObject<EntityType<?>, EntityType<PopomEntity>> POPOM = add("popom",
            of(PopomEntity::new, CREATURE, scalable(1.1F, 1.0F), 5),
            attributes(PopomEntity::createPopomAttributes),
            spawnRestrictions(PopomEntity::checkMobSpawnRules));
    public static final RegistryObject<EntityType<?>, EntityType<QuintEntity>> QUINT = add("quint",
            of(QuintEntity::new, MONSTER, scalable(0.65F, 0.65F), 16),
            attributes(QuintEntity::createQuintAttributes),
            spawnRestrictions(QuintEntity::checkMobSpawnRules));
    public static final RegistryObject<EntityType<?>, EntityType<ThrownNitraEntity>> THROWN_NITRA = add("thrown_nitra",
            of(ThrownNitraEntity::new, MISC, scalable(0.5F, 0.5F), 5));
    public static final RegistryObject<EntityType<?>, EntityType<LevitaArrow>> LEVITA_ARROW = add("levita_arrow",
            EntityType.Builder.<LevitaArrow>of(LevitaArrow::new, MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    public static final RegistryObject<EntityType<?>, EntityType<Boat>> AUREL_BOAT =
            add("aurel_boat", boat(() -> ItemRegistry.AUREL_BOATS.boat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<ChestBoat>> AUREL_CHEST_BOAT =
            add("aurel_chest_boat", chestBoat(() -> ItemRegistry.AUREL_BOATS.chestBoat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<Boat>> MOTHER_AUREL_BOAT =
            add("mother_aurel_boat", boat(() -> ItemRegistry.MOTHER_AUREL_BOATS.boat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<ChestBoat>> MOTHER_AUREL_CHEST_BOAT =
            add("mother_aurel_chest_boat", chestBoat(() -> ItemRegistry.MOTHER_AUREL_BOATS.chestBoat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<Boat>> MENTH_BOAT =
            add("menth_boat", boat(() -> ItemRegistry.MENTH_BOATS.boat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<ChestBoat>> MENTH_CHEST_BOAT =
            add("menth_chest_boat", chestBoat(() -> ItemRegistry.MENTH_BOATS.chestBoat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<Boat>> WISTERIA_BOAT =
            add("wisteria_boat", boat(() -> ItemRegistry.WISTERIA_BOATS.boat().get()));
    public static final RegistryObject<EntityType<?>, EntityType<ChestBoat>> WISTERIA_CHEST_BOAT =
            add("wisteria_chest_boat", chestBoat(() -> ItemRegistry.WISTERIA_BOATS.chestBoat().get()));

    private static EntityType.Builder<Boat> boat(Supplier<Item> item) {
        return EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, item), MISC)
                .noLootTable()
                .sized(1.375F, 0.5625F)
                .eyeHeight(0.5625F)
                .clientTrackingRange(10);
    }

    private static EntityType.Builder<ChestBoat> chestBoat(Supplier<Item> item) {
        return EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, item), MISC)
                .noLootTable()
                .sized(1.375F, 0.5625F)
                .eyeHeight(0.5625F)
                .clientTrackingRange(10);
    }

    private EntityRegistry() {}

    public static void init() {

    }

    public static void registerSpawnPlacements() {
        SPAWN_REGISTRATIONS.forEach(Runnable::run);
    }

    public static List<AttributeRegistration> attributeRegistrations() {
        return Collections.unmodifiableList(ATTRIBUTE_REGISTRATIONS);
    }

    @SafeVarargs
    private static <E extends Entity> RegistryObject<EntityType<?>, EntityType<E>> add(
            String id,
            EntityType.Builder<E> builder,
            Consumer<? super RegistryObject<EntityType<?>, EntityType<E>>>... additionalActions) {
        RegistryObject<EntityType<?>, EntityType<E>> type = ENTITY_TYPES.register(id, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, ModConstants.id(id))));
        for (var action : additionalActions) {
            action.accept(type);
        }
        return type;
    }

    private static <E extends LivingEntity> Consumer<RegistryObject<EntityType<?>, EntityType<E>>> attributes(
            Supplier<AttributeSupplier.Builder> builder) {
        return type -> ATTRIBUTE_REGISTRATIONS.add(new AttributeRegistration(type, builder));
    }

    private static <T extends Mob> Consumer<RegistryObject<EntityType<?>, EntityType<T>>> spawnRestrictions(
            SpawnPlacementType location, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        return type -> SPAWN_REGISTRATIONS.add(() -> SpawnPlacements.register(type.get(), location, heightmapType, predicate));
    }

    private static <T extends Mob> Consumer<RegistryObject<EntityType<?>, EntityType<T>>> spawnRestrictions(
            SpawnPlacementType location, SpawnPlacements.SpawnPredicate<T> predicate) {
        return spawnRestrictions(location, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Mob> Consumer<RegistryObject<EntityType<?>, EntityType<T>>> spawnRestrictions(
            SpawnPlacements.SpawnPredicate<T> predicate) {
        return spawnRestrictions(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Entity> EntityType.Builder<T> of(
            EntityType.EntityFactory<T> factory, MobCategory category, EntityDimensions dimensions, int trackingRange) {
        return EntityType.Builder.of(factory, category)
                .sized(dimensions.width(), dimensions.height())
                .clientTrackingRange(trackingRange);
    }

    public record AttributeRegistration(
            Supplier<? extends EntityType<? extends LivingEntity>> type,
            Supplier<AttributeSupplier.Builder> builder
    ) {}
}
