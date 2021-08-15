package com.aether.entities;

import com.aether.entities.block.FloatingBlockEntity;
import com.aether.entities.hostile.*;
import com.aether.entities.passive.*;
import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonDartEntity;
import com.aether.entities.projectile.PoisonNeedleEntity;
import com.aether.registry.RegistryQueue;
import com.aether.registry.RegistryQueue.Action;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.*;

import java.util.function.Supplier;

import static com.aether.Aether.locate;
import static net.minecraft.entity.EntityDimensions.changing;
import static net.minecraft.entity.SpawnGroup.*;

@SuppressWarnings({"unused", "SameParameterValue"})
public class AetherEntityTypes {
    private static Action<? super EntityType<? extends LivingEntity>> attributes(Supplier<DefaultAttributeContainer.Builder> builder) {
        return (id, entityType) -> DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(entityType, builder.get().build());
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



    /*
    Begin entity types
     */

    public static final EntityType<AechorPlantEntity> AECHOR_PLANT = add("aechor_plant", of(AechorPlantEntity::new, MONSTER, changing(1f, 1f), 5),
            attributes(AechorPlantEntity::createAechorPlantAttributes), spawnRestrictions(AetherAnimalEntity::isValidNaturalAetherSpawn));
            //todo: why is this an animal? should extend hostile to allow hostile spawn restrictions, and inherit hostile behviour

    public static final EntityType<FlyingCowEntity> FLYING_COW = add("flying_cow", of(FlyingCowEntity::new, CREATURE, changing(0.9F, 1.3F), 5),
            attributes(FlyingCowEntity::createFlyingCowAttributes), spawnRestrictions(AetherAnimalEntity::isValidNaturalAetherSpawn));

    public static final EntityType<AerbunnyEntity> AERBUNNY = add("aerbunny", of(AerbunnyEntity::new, CREATURE, changing(0.55F, 0.55F), 5),
            attributes(AerbunnyEntity::createAerbunnyAttributes), spawnRestrictions(AetherAnimalEntity::isValidNaturalAetherSpawn));

    public static final EntityType<MoaEntity> MOA = add("moa", of(MoaEntity::new, CREATURE, changing(1.0F, 2.0F), 5),
            attributes(MoaEntity::createMoaAttributes), spawnRestrictions(AetherAnimalEntity::isValidNaturalAetherSpawn));

    public static final EntityType<PhygEntity> PHYG = add("phyg", of(PhygEntity::new, CREATURE, changing(0.9F, 1.3F), 5),
            attributes(PhygEntity::createPhygAttributes), spawnRestrictions(AetherAnimalEntity::isValidNaturalAetherSpawn));

    public static final EntityType<SheepuffEntity> SHEEPUFF = add("sheepuff", of(SheepuffEntity::new, CREATURE, changing(0.9F, 1.3F), 5),
            attributes(SheepuffEntity::createSheepuffAttributes), spawnRestrictions(AetherAnimalEntity::isValidNaturalAetherSpawn));

    public static final EntityType<CockatriceEntity> COCKATRICE = add("cockatrice", of(CockatriceEntity::new, MONSTER, changing(1.0F, 2.0F), 5),
            attributes(CockatriceEntity::createCockatriceAttributes), spawnRestrictions(HostileEntity::canSpawnInDark));

    public static final EntityType<ChestMimicEntity> CHEST_MIMIC = add("chest_mimic", of(ChestMimicEntity::new, MONSTER, changing(1.0F, 2.0F), 5),
            attributes(ChestMimicEntity::createChestMimicAttributes), spawnRestrictions(HostileEntity::canSpawnInDark));

    public static final EntityType<FloatingBlockEntity> FLOATING_BLOCK = add("floating_block",
            AetherEntityTypes.<FloatingBlockEntity>of(FloatingBlockEntity::new, MISC, changing(0.98F, 0.98F), 10).trackedUpdateRate(20));

    public static final EntityType<GoldenDartEntity> GOLDEN_DART = add("golden_dart", of(GoldenDartEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<EnchantedDartEntity> ENCHANTED_DART = add("enchanted_dart", of(EnchantedDartEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<PoisonDartEntity> POISON_DART = add("poison_dart", of(PoisonDartEntity::new, MISC, changing(0.5F, 0.5F), 5));
    public static final EntityType<PoisonNeedleEntity> POISON_NEEDLE = add("poison_needle", of(PoisonNeedleEntity::new, MISC, changing(0.5F, 0.5F), 5));

    public static final EntityType<AerwhaleEntity> AERWHALE = add("aerwhale", of(AerwhaleEntity::new, CREATURE, changing(3.0F, 1.2F), 5),
            attributes(AerwhaleEntity::createAerwhaleAttributes), spawnRestrictions(MobEntity::canMobSpawn));

    public static final EntityType<BlueSwetEntity> BLUE_SWET = add("blue_swet", of(BlueSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(BlueSwetEntity::createSwetAttributes), spawnRestrictions(SwetEntity::canSpawn));

    public static final EntityType<PurpleSwetEntity> PURPLE_SWET = add("purple_swet", of(PurpleSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(PurpleSwetEntity::createSwetAttributes), spawnRestrictions(SwetEntity::canSpawn));

    public static final EntityType<WhiteSwetEntity> WHITE_SWET = add("white_swet", of(WhiteSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(WhiteSwetEntity::createSwetAttributes), spawnRestrictions(SwetEntity::canSpawn));

    public static final EntityType<GoldenSwetEntity> GOLDEN_SWET = add("golden_swet", of(GoldenSwetEntity::new, MONSTER, changing(2.0F, 2.0F), 5),
            attributes(GoldenSwetEntity::createSwetAttributes), spawnRestrictions(SwetEntity::canSpawn));


    public static void init() {
        RegistryQueue.ENTITY_TYPE.register();
    }

    @SafeVarargs
    private static <V extends EntityType<?>> V add(String id, V type, Action<? super V>... additionalActions) {
        return RegistryQueue.ENTITY_TYPE.add(locate(id), type, additionalActions);
    }

    @SafeVarargs
    private static <E extends Entity> EntityType<E> add(String id, FabricEntityTypeBuilder<E> builder, Action<? super EntityType<E>>... additionalActions) {
        return add(id, builder.build(), additionalActions);
    }

    public static <T extends Entity> FabricEntityTypeBuilder<T> of(EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, EntityDimensions dimensions, int trackingRange) {
        return FabricEntityTypeBuilder.create(spawnGroup, factory).dimensions(dimensions).trackRangeChunks(trackingRange);
    }
}
