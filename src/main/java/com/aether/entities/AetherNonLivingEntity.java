package com.aether.entities;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class AetherNonLivingEntity extends Entity {
	public AetherNonLivingEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	public ResourceLocation createSpawnPacket(FriendlyByteBuf buf) {
		buf.writeVarInt(this.getId());
		buf.writeUUID(this.getUUID());
		buf.writeDouble(this.getX());
		buf.writeDouble(this.getY());
		buf.writeDouble(this.getZ());

		return null;
	}

	@Override
	public final Packet<?> getAddEntityPacket() {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		ResourceLocation id = this.createSpawnPacket(buf);

		for (ServerPlayer playerEntity : ((ServerLevel) this.level).players()) {
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerEntity, id, buf);
		}

		return new ClientboundAddEntityPacket(this);
	}
}
