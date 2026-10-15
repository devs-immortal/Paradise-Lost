package net.id.paradise_lost.gametest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TestHelpers {
    private static final Logger LOG = LoggerFactory.getLogger("ClientTest");

    private TestHelpers() {
    }

    public static Minecraft client() {
        return Minecraft.getInstance();
    }

    public static void onServer(Consumer<IntegratedServer> action) {
        IntegratedServer server = client().getSingleplayerServer();
        check(server != null, "integrated server is gone (game paused or shutting down)");
        server.submit(() -> action.accept(server)).join();
    }

    public static <T> T fromServer(Function<IntegratedServer, T> action) {
        IntegratedServer server = client().getSingleplayerServer();
        check(server != null, "integrated server is gone (game paused or shutting down)");
        return server.submit(() -> action.apply(server)).join();
    }

    public static ServerPlayer player(IntegratedServer server) {
        return server.getPlayerList().getPlayers().getFirst();
    }

    public static ServerLevel world(IntegratedServer server) {
        return player(server).serverLevel();
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

    public static BlockHitResult topOf(BlockPos pos) {
        return new BlockHitResult(Vec3.atCenterOf(pos).add(0, 0.5, 0), Direction.UP, pos, false);
    }

    public static void useOn(BlockPos pos) {
        client().gameMode.useItemOn(client().player, InteractionHand.MAIN_HAND, topOf(pos));
    }

    public static void interact(int entityId) {
        Entity entity = client().level.getEntity(entityId);
        check(entity != null, "client does not have entity " + entityId);
        client().gameMode.interact(client().player, entity, InteractionHand.MAIN_HAND);
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

    public static void teleport(ServerPlayer player, ResourceKey<Level> world, double x, double y, double z, float pitch) {
        teleport(player, world, x, y, z, 0, pitch);
    }

    public static void teleport(ServerPlayer player, ResourceKey<Level> world, double x, double y, double z, float yaw, float pitch) {
        ServerLevel level = client().getSingleplayerServer().getLevel(world);
        check(level != null, "missing level " + world.location());
        teleport(player, level, x, y, z, yaw, pitch);
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

    public static void screenshot(String name) {
        screenshot(name, false);
    }

    public static void screenshot(String name, boolean hud) {
        Minecraft client = client();
        client.options.hideGui = !hud;
        String numbered = name;
        if (ClientTestRunner.currentTestNumber > 0) {
            String base = name.endsWith(".png") ? name.substring(0, name.length() - 4) : name;
            String ext = name.endsWith(".png") ? ".png" : "";
            numbered = String.format("%02d_%s%s", ClientTestRunner.currentTestNumber, base, ext);
        }
        String fileName = numbered;
        client.execute(() -> {
            Screenshot.grab(client.gameDirectory, fileName, client.getMainRenderTarget(), message -> LOG.info(message.getString()));
            client.options.hideGui = false;
        });
    }
}
