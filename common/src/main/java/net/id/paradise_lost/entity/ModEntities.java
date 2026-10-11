package net.id.paradise_lost.entity;

import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.BiConsumer;

public final class ModEntities {
    private ModEntities() {}

    public static void init() {
        SpawnPlacements.register(EntityType.BREEZE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
    }

    public static void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> registrar) {
        for (EntityRegistry.AttributeRegistration registration : EntityRegistry.attributeRegistrations()) {
            registrar.accept(registration.type().get(), registration.builder().get().build());
        }
    }
}
