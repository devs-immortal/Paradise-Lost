package com.aether.entities.util;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class EntityData {
	public final int id;
	public final UUID uuid;
	public final double x;
	public final double y;
	public final double z;

	public EntityData(FriendlyByteBuf buf) {
		this.id = buf.readVarInt();
		this.uuid = buf.readUUID();
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}
}