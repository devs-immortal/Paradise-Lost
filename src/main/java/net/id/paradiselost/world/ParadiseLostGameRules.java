package net.id.paradiselost.world;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public class ParadiseLostGameRules {
    public static final Identifier MAX_AUGMENTED_SPEED_ID = ParadiseLost.locate("max_augmented_speed");

    public static final GameRules.Key<DoubleRule> MAX_AUGMENTED_SPEED = GameRuleRegistry.register("maxAugmentedVelocity",
            GameRules.Category.MOBS,
            GameRuleFactory.createDoubleRule(
                    24,
                    0.01,
                    65535,
                    ((server, rule) -> server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(getMaxAugmentedSpeedSyncPacket(rule.get()))))));

    public static Packet<?> getMaxAugmentedSpeedSyncPacket(double value) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(value);
        return new CustomPayloadS2CPacket(MAX_AUGMENTED_SPEED_ID, buf);
    }

    public static void init() {
        // N/A
    }
}
