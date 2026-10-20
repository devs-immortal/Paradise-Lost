package net.id.paradise_lost.test;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class TestHelpers {
    private TestHelpers() {
    }

    public static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void expect(List<String> wrong, boolean ok, String what) {
        if (!ok) wrong.add(what);
    }

    public static void checkAll(List<String> wrong) {
        check(wrong.isEmpty(), String.join("; ", wrong));
    }

    public static <T> void forEachOurs(Registry<T> registry, Consumer<T> action) {
        for (T entry : registry) {
            if (registry.getKey(entry).getNamespace().equals(ModConstants.MOD_ID)) action.accept(entry);
        }
    }

    public static BlockHitResult topOf(BlockPos pos) {
        return new BlockHitResult(Vec3.atCenterOf(pos).add(0, 0.5, 0), Direction.UP, pos, false);
    }

    public static void hold(ServerPlayer player, Item item, int count) {
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item, count));
    }

    public static void hold(ServerPlayer player, Item item) {
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
    }

    public static int spawnNoAi(ServerPlayer player, EntityType<? extends Mob> type, BlockPos at) {
        Mob mob = type.spawn(player.serverLevel(), at, MobSpawnType.COMMAND);
        mob.setNoAi(true);
        return mob.getId();
    }

    public static void setField(Object target, String name, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getField(Object target, String name) {
        return getField(target, target.getClass(), name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object target, Class<?> owner, String name) {
        try {
            Field field = owner.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static BlockPos ahead(ServerPlayer player, int distance, int side) {
        Direction facing = player.getDirection();
        return player.blockPosition().relative(facing, distance).relative(facing.getClockWise(), side);
    }

    public static void teleport(ServerPlayer player, ServerLevel world, double x, double y, double z, float yaw, float pitch) {
        player.teleportTo(world, x, y, z, Set.of(), yaw, pitch);
    }

    public static Map<Item, Integer> drops(ServerLevel world, BlockPos pos) {
        return drops(world, pos, 1.5);
    }

    public static Map<Item, Integer> drops(ServerLevel world, BlockPos pos, double radius) {
        Map<Item, Integer> drops = new LinkedHashMap<>();
        for (ItemStack stack : dropStacks(world, pos, radius)) {
            drops.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }
        return drops;
    }

    public static List<ItemStack> dropStacks(ServerLevel world, BlockPos pos, double radius) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemEntity item : world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(radius), e -> true)) {
            stacks.add(item.getItem().copy());
            item.discard();
        }
        return stacks;
    }

    public static String describe(Map<Item, Integer> items) {
        if (items.isEmpty()) return "nothing";
        return items.entrySet().stream()
                .map(e -> BuiltInRegistries.ITEM.getKey(e.getKey()) + " x" + e.getValue())
                .collect(Collectors.joining(", "));
    }
}
