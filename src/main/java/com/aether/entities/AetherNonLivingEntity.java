package com.aether.entities;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class AetherNonLivingEntity extends Entity {
	public AetherNonLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	public Identifier createSpawnPacket(PacketByteBuf buf) {
		buf.writeVarInt(this.getId());
		buf.writeUuid(this.getUuid());
		buf.writeDouble(this.getX());
		buf.writeDouble(this.getY());
		buf.writeDouble(this.getZ());

		return null;
	}

	@Override
	public final Packet<?> createSpawnPacket() {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		Identifier id = this.createSpawnPacket(buf);

		for (ServerPlayerEntity playerEntity : ((ServerWorld) this.world).getPlayers()) {
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerEntity, id, buf);
		}

		return new EntitySpawnS2CPacket(this);
	}
}
