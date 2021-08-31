package net.id.aether.world;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public class AetherGameRules {
    public static final Identifier MAX_QUICKSOIL_SPEED_ID = new Identifier("the_aether", "max_quicksoil_speed");

    public static final GameRules.Key<DoubleRule> MAX_QUICKSOIL_SPEED = GameRuleRegistry.register("maxQuicksoilVelocity",
            GameRules.Category.MOBS,
            GameRuleFactory.createDoubleRule(
                    24,
                    0.01,
                    65535,
                    ((server, rule) -> server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(getMaxQuicksoilSpeedSyncPacket(rule.get()))))));

    public static Packet<?> getMaxQuicksoilSpeedSyncPacket(double value) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(value);
        return new CustomPayloadS2CPacket(MAX_QUICKSOIL_SPEED_ID, buf);
    }

    public static void init() {
        // N/A
    }
}
