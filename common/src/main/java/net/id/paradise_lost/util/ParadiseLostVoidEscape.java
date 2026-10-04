package net.id.paradise_lost.util;

import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.id.paradise_lost.world.ParadiseLostGameRules.PARADISE_VOID_KILLS;

public final class ParadiseLostVoidEscape {
    private static final Set<UUID> ESCAPING = ConcurrentHashMap.newKeySet();

    private ParadiseLostVoidEscape() {
    }

    public static boolean isOutOfWorldDamage(DamageSource source) {
        return source.is(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
    }

    public static boolean tryEscape(Entity entity) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return false;
        }
        if (level.dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return false;
        }
        if (level.getGameRules().getBoolean(PARADISE_VOID_KILLS)) {
            return false;
        }

        Entity root = entity.getRootVehicle();
        if (!ESCAPING.add(root.getUUID())) {
            return true;
        }

        try {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            if (overworld == null) {
                return false;
            }

            WorldBorder border = overworld.getWorldBorder();
            double xMin = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
            double zMin = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
            double xMax = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
            double zMax = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
            double scale = DimensionType.getTeleportationScale(level.dimensionType(), overworld.dimensionType());
            Vec3 dest = new Vec3(
                    Mth.clamp(root.getX() * scale, xMin, xMax),
                    overworld.getMaxY() + 129,
                    Mth.clamp(root.getZ() * scale, zMin, zMax)
            );

            List<PassengerLink> links = capturePassengerTree(root);
            for (Entity rider : root.getSelfAndPassengers().toList()) {
                if (rider instanceof ParadiseLostEntityExtensions extensions) {
                    extensions.setParadiseLostFallen(true);
                }
                if (rider instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 2, false, false, true));
                    living.resetFallDistance();
                }
            }

            Entity teleported = root.teleport(new TeleportTransition(
                    overworld,
                    dest,
                    root.getDeltaMovement(),
                    root.getYRot(),
                    root.getXRot(),
                    TeleportTransition.DO_NOTHING
            ));
            if (teleported == null) {
                return false;
            }

            remountTree(links, teleported, overworld);
            return true;
        } finally {
            ESCAPING.remove(root.getUUID());
        }
    }

    private static List<PassengerLink> capturePassengerTree(Entity root) {
        List<PassengerLink> links = new ArrayList<>();
        capturePassengerTree(root, links);
        return links;
    }

    private static void capturePassengerTree(Entity vehicle, List<PassengerLink> links) {
        for (Entity passenger : List.copyOf(vehicle.getPassengers())) {
            links.add(new PassengerLink(passenger.getUUID(), vehicle.getUUID()));
            capturePassengerTree(passenger, links);
        }
    }

    private static void remountTree(List<PassengerLink> links, Entity root, ServerLevel level) {
        Map<UUID, Entity> byId = new HashMap<>();
        root.getSelfAndPassengers().forEach(entity -> byId.put(entity.getUUID(), entity));

        for (PassengerLink link : links) {
            Entity passenger = byId.get(link.passengerId());
            Entity vehicle = byId.get(link.vehicleId());
            if (passenger == null) {
                passenger = level.getEntity(link.passengerId());
            }
            if (vehicle == null) {
                vehicle = level.getEntity(link.vehicleId());
            }
            if (passenger != null && vehicle != null && passenger.getVehicle() != vehicle) {
                passenger.teleportTo(vehicle.getX(), vehicle.getY(), vehicle.getZ());
                passenger.startRiding(vehicle, true);
            }
        }
    }

    private record PassengerLink(UUID passengerId, UUID vehicleId) {
    }
}
