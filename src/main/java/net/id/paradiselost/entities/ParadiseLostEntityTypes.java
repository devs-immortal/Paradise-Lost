package net.id.paradiselost.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.id.paradiselost.entities.block.FloatingBlockEntity;
import net.id.paradiselost.entities.block.SliderEntity;
import net.id.paradiselost.entities.hostile.EnvoyEntity;
import net.id.paradiselost.entities.hostile.SentinelEntity;
import net.id.paradiselost.entities.passive.ParadiseLostAnimalEntity;
import net.id.paradiselost.entities.passive.PopomEntity;
import net.id.paradiselost.entities.passive.QuintEntity;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.entities.projectile.ThrownNitraEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.Heightmap;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.minecraft.entity.SpawnGroup.CREATURE;
import static net.minecraft.entity.SpawnGroup.MISC;
import static net.minecraft.entity.SpawnGroup.MONSTER;

@SuppressWarnings({"unused", "SameParameterValue"})
public class ParadiseLostEntityTypes {
    // Block
    public static final EntityType<FloatingBlockEntity> FLOATING_BLOCK = add(
            "floating_block",
            of(FloatingBlockEntity::new, MISC, 0.98F, 0.98F, 20)
    );

    public static final EntityType<SliderEntity> SLIDER = add(
            "slider",
            of(SliderEntity::new, MISC, 0.98F, 0.98F, 20)
    );

    // Hostile
    public static final EntityType<EnvoyEntity> ENVOY = add(
            "envoy",
            of(EnvoyEntity::new, MONSTER, 0.6F, 1.95F, 10),
            attributes(EnvoyEntity::createEnvoyAttributes),
            spawnRestrictions(HostileEntity::canSpawnInDark)
    );

    public static final EntityType<SentinelEntity> SENTINEL = add(
            "sentinel",
            of(SentinelEntity::new, MONSTER, 0.6F, 2.25F, 16),
            attributes(SentinelEntity::createSentinelAttributes),
            spawnRestrictions(SentinelEntity::noSpawn)
    );

    // Boats
    public static final EntityType<ChestBoatEntity> AUREL_BOAT = add(
            "aurel_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.AUREL_BOATS::boat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> AUREL_CHEST_BOAT = add(
            "aurel_chest_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.AUREL_BOATS::chestBoat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> MOTHER_AUREL_BOAT = add(
            "mother_aurel_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.MOTHER_AUREL_BOATS::boat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> MOTHER_AUREL_CHEST_BOAT = add(
            "mother_aurel_chest_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.MOTHER_AUREL_BOATS::chestBoat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> MENTH_BOAT = add(
            "menth_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.MENTH_BOATS::boat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> MENTH_CHEST_BOAT = add(
            "menth_chest_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.MENTH_BOATS::chestBoat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> WISTERIA_BOAT = add(
            "wisteria_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.WISTERIA_BOATS::boat), MISC, 1.375F, 0.5625F, 10)
    );

    public static final EntityType<ChestBoatEntity> WISTERIA_CHEST_BOAT = add(
            "wisteria_chest_boat",
            of((type, world) -> new ChestBoatEntity(type, world, ParadiseLostItems.WISTERIA_BOATS::chestBoat), MISC, 1.375F, 0.5625F, 10)
    );

    // Passive
    public static final EntityType<MoaEntity> MOA = add(
            "moa",
            of(MoaEntity::new, CREATURE, 0.8F, 1.9F, 5),
            attributes(MoaEntity::createMoaAttributes),
            spawnRestrictions(ParadiseLostAnimalEntity::isValidNaturalParadiseLostSpawn)
    );

    public static final EntityType<PopomEntity> POPOM = add(
            "popom",
            of(PopomEntity::new, CREATURE, 1.1F, 1.0F, 5),
            attributes(PopomEntity::createPopomAttributes),
            spawnRestrictions(PopomEntity::canMobSpawn)
    );

    public static final EntityType<QuintEntity> QUINT = add(
            "quint",
            of(QuintEntity::new, MONSTER, 0.65F, 0.65F, 16),
            attributes(QuintEntity::createQuintAttributes),
            spawnRestrictions(QuintEntity::canMobSpawn)
    );

    // Projectile
    public static final EntityType<ThrownNitraEntity> THROWN_NITRA = add(
            "thrown_nitra",
            of(ThrownNitraEntity::new, MISC, 0.5F, 0.5F, 5)
    );

    private static Consumer<? super EntityType<? extends LivingEntity>> attributes(Supplier<DefaultAttributeContainer.Builder> builder) {
        return entityType -> FabricDefaultAttributeRegistry.register(entityType, builder.get());
    }

    private static <T extends MobEntity> Consumer<EntityType<T>> spawnRestrictions(SpawnLocation location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
        return entityType -> SpawnRestriction.register(entityType, location, heightmapType, predicate);
    }

    private static <T extends MobEntity> Consumer<EntityType<T>> spawnRestrictions(SpawnLocation location, SpawnRestriction.SpawnPredicate<T> predicate) {
        return spawnRestrictions(location, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends MobEntity> Consumer<EntityType<T>> spawnRestrictions(SpawnRestriction.SpawnPredicate<T> predicate) {
        return spawnRestrictions(SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    public static void init() {
    }

    @SafeVarargs
    private static <E extends Entity> EntityType<E> add(String id, EntityType.Builder<E> builder, Consumer<? super EntityType<E>>... additionalActions) {
        var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, locate(id));
        var ent = Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));

        for (var action : additionalActions) {
            action.accept(ent);
        }

        return ent;
    }

    public static <T extends Entity> EntityType.Builder<T> of(EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, float width, float height, int trackingRange) {
        return EntityType.Builder.create(factory, spawnGroup)
                .dimensions(width, height)
                .maxTrackingRange(trackingRange);
    }
}