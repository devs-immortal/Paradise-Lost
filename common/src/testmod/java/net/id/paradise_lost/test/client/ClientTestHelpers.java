package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.TestHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

import static net.id.paradise_lost.test.TestHelpers.check;
import static net.id.paradise_lost.test.TestHelpers.topOf;

public final class ClientTestHelpers {
    private static final Logger LOG = LoggerFactory.getLogger("ClientTest");

    private ClientTestHelpers() {
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

    public static void useOn(BlockPos pos) {
        client().gameMode.useItemOn(client().player, InteractionHand.MAIN_HAND, topOf(pos));
    }

    public static void interact(int entityId) {
        Entity entity = client().level.getEntity(entityId);
        check(entity != null, "client does not have entity " + entityId);
        client().gameMode.interact(client().player, entity, InteractionHand.MAIN_HAND);
    }

    public static void teleport(ServerPlayer player, ResourceKey<Level> world, double x, double y, double z, float pitch) {
        teleport(player, world, x, y, z, 0, pitch);
    }

    public static void teleport(ServerPlayer player, ResourceKey<Level> world, double x, double y, double z, float yaw, float pitch) {
        ServerLevel level = client().getSingleplayerServer().getLevel(world);
        check(level != null, "missing level " + world.location());
        TestHelpers.teleport(player, level, x, y, z, yaw, pitch);
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
