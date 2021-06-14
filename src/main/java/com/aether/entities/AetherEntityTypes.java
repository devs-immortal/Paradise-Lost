package com.aether.entities;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.entities.hostile.*;
import com.aether.entities.passive.*;
import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonDartEntity;
import com.aether.entities.projectile.PoisonNeedleEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;

import java.util.Random;

import static net.minecraft.entity.SpawnReason.SPAWNER;

public class AetherEntityTypes {
    public static final EntityType<AechorPlantEntity> AECHOR_PLANT;
    public static final EntityType<FlyingCowEntity> FLYING_COW;
    public static final EntityType<AerbunnyEntity> AERBUNNY;
    public static final EntityType<MoaEntity> MOA;
    public static final EntityType<PhygEntity> PHYG;
    public static final EntityType<SheepuffEntity> SHEEPUFF;
    public static final EntityType<CockatriceEntity> COCKATRICE;
    public static final EntityType<ChestMimicEntity> CHEST_MIMIC;
    public static final EntityType<FloatingBlockEntity> FLOATING_BLOCK;
    public static final EntityType<GoldenDartEntity> GOLDEN_DART;
    public static final EntityType<EnchantedDartEntity> ENCHANTED_DART;
    public static final EntityType<PoisonDartEntity> POISON_DART;
    public static final EntityType<PoisonNeedleEntity> POISON_NEEDLE;
//    public static EntityType<WhirlwindEntity> WHIRLWIND;
    public static final EntityType<AerwhaleEntity> AERWHALE;
//    public static EntityType<MiniCloudEntity> MINI_CLOUD;
//    public static EntityType<FireMinionEntity> FIRE_MINION;
//    public static EntityType<CrystalEntity> CRYSTAL;
//    public static EntityType<PhoenixArrowEntity> PHOENIX_ARROW;
    public static final EntityType<SwetEntity> BLUE_SWET;
    public static final EntityType<SwetEntity> PURPLE_SWET;
    public static final EntityType<SwetEntity> WHITE_SWET;
    public static final EntityType<SwetEntity> GOLDEN_SWET;

    static {
        AECHOR_PLANT = register("aechor_plant", SpawnGroup.MONSTER, EntityDimensions.changing(1.0F, 1.0F), (entityType, world) -> new AechorPlantEntity(world));
        FLYING_COW = register("flying_cow", SpawnGroup.CREATURE, EntityDimensions.changing(0.9F, 1.3F), (entityType, world) -> new FlyingCowEntity(world));
        AERBUNNY = register("aerbunny", SpawnGroup.CREATURE, EntityDimensions.changing(0.55F, 0.55F), (entityType, world) -> new AerbunnyEntity(world));
        MOA = register("moa", SpawnGroup.CREATURE, EntityDimensions.changing(1.0F, 2.0F), (entityType, world) -> new MoaEntity(world));
        PHYG = register("phyg", SpawnGroup.CREATURE, EntityDimensions.changing(0.9F, 1.3F), (entityType, world) -> new PhygEntity(world));
        SHEEPUFF = register("sheepuff", SpawnGroup.CREATURE, EntityDimensions.changing(0.9F, 1.3F), (entityType, world) -> new SheepuffEntity(world));
        COCKATRICE = register("cockatrice", SpawnGroup.MONSTER, EntityDimensions.changing(1.0F, 2.0F), (entityType, world) -> new CockatriceEntity(world));
        CHEST_MIMIC = register("chest_mimic", SpawnGroup.MONSTER, EntityDimensions.changing(1.0F, 2.0F), (entityType, world) -> new ChestMimicEntity(world));
        FLOATING_BLOCK = register("floating_block", 160, 20, true, EntityDimensions.changing(0.98F, 0.98F), FloatingBlockEntity::new);
        GOLDEN_DART = register("golden_dart", SpawnGroup.MISC, EntityDimensions.changing(0.5F, 0.5F), (entityType, world) -> new GoldenDartEntity(world));
        ENCHANTED_DART = register("enchanted_dart", SpawnGroup.MISC, EntityDimensions.changing(0.5F, 0.5F), (entityType, world) -> new EnchantedDartEntity(world));
        POISON_DART = register("poison_dart", SpawnGroup.MISC, EntityDimensions.changing(0.5F, 0.5F), (entityType, world) -> new PoisonDartEntity(world));
        POISON_NEEDLE = register("poison_needle", SpawnGroup.MISC, EntityDimensions.changing(0.5F, 0.5F), (entityType, world) -> new PoisonNeedleEntity(world));
        AERWHALE = register("aerwhale", SpawnGroup.CREATURE, EntityDimensions.changing(3.0F, 1.2F), (entityType, world) -> new AerwhaleEntity(world));
//        WHIRLWIND = register("whirlwind", ...);
//        MINI_CLOUD = register("mini_cloud", ...);
//        FIRE_MINION = register("fire_minion", ...);
//        CRYSTAL = register("crystal", ...);
//        PHOENIX_ARROW = register("phoenix_arrow", ...);
        BLUE_SWET = register("blue_swet", SpawnGroup.MONSTER, EntityDimensions.changing(2.0F, 2.0F), (entityType, world) -> new BlueSwetEntity(world));
        PURPLE_SWET = register("purple_swet", SpawnGroup.MONSTER, EntityDimensions.changing(2.0F, 2.0F), (entityType, world) -> new PurpleSwetEntity(world));
        WHITE_SWET = register("white_swet", SpawnGroup.MONSTER, EntityDimensions.changing(2.0F, 2.0F), (entityType, world) -> new WhiteSwetEntity(world));
        GOLDEN_SWET = register("golden_swet", SpawnGroup.MONSTER, EntityDimensions.changing(2.0F, 2.0F), (entityType, world) -> new GoldenSwetEntity(world));
    }

    public static void init() {
        // Register Entity Attribute Data and Spawn Restrictions - TODO
        FabricDefaultAttributeRegistry.register(MOA, MoaEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(FLYING_COW, FlyingCowEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(SHEEPUFF, SheepuffEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(AERBUNNY, AerbunnyEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(AECHOR_PLANT, AechorPlantEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(PHYG, PhygEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(COCKATRICE, CockatriceEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(AERWHALE, AerwhaleEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(CHEST_MIMIC, ChestMimicEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(BLUE_SWET, SwetEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(PURPLE_SWET, SwetEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(WHITE_SWET, SwetEntity.initAttributes());
        FabricDefaultAttributeRegistry.register(GOLDEN_SWET, SwetEntity.initAttributes());

        // Don't seem to spawn if there is a restriction, i'm not sure but maybe it's because of their size?
        //SpawnRestriction.register(AERWHALE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getAnimalData);
        SpawnRestriction.register(SHEEPUFF, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getAnimalData);
        SpawnRestriction.register(PHYG, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getAnimalData);
        SpawnRestriction.register(AERBUNNY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getAnimalData);
        SpawnRestriction.register(MOA, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getAnimalData);
        SpawnRestriction.register(FLYING_COW, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getAnimalData);
        SpawnRestriction.register(COCKATRICE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getHostileData);
        SpawnRestriction.register(AECHOR_PLANT, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getHostileData);
        SpawnRestriction.register(BLUE_SWET, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getHostileData);
        SpawnRestriction.register(PURPLE_SWET, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getHostileData);
        SpawnRestriction.register(WHITE_SWET, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getHostileData);
        SpawnRestriction.register(GOLDEN_SWET, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AetherEntityTypes::getHostileData);
    }

    public static DefaultAttributeContainer.Builder getDefaultAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 6);
    }

    public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, Aether.locate(name), FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).trackRangeBlocks(trackingDistance).trackedUpdateRate(updateIntervalTicks).forceTrackedVelocityUpdates(alwaysUpdateVelocity).dimensions(size).disableSaving().build());
    }

    public static <X extends Entity> EntityType<X> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, Aether.locate(name), FabricEntityTypeBuilder.create(category, factory).dimensions(size).build());
    }

    public static boolean getAnimalData(EntityType<? extends Entity> entityType, WorldAccess WorldAccess, SpawnReason SpawnReason, BlockPos blockPos, Random random) {
        return WorldAccess.getBlockState(blockPos.down()).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK && WorldAccess.getBaseLightLevel(blockPos, 0) > 8 && (SpawnReason == SPAWNER || WorldAccess.getBlockState(blockPos.down()).allowsSpawning(WorldAccess, blockPos, entityType));
    }

    public static boolean getHostileData(EntityType<? extends Entity> entityType_1, ServerWorldAccess WorldAccess_1, SpawnReason SpawnReason, BlockPos blockPos_1, Random random_1) {
        return WorldAccess_1.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(WorldAccess_1, blockPos_1, random_1) && (SpawnReason == SPAWNER || WorldAccess_1.getBlockState(blockPos_1.down()).allowsSpawning(WorldAccess_1, blockPos_1, entityType_1));
    }
}
