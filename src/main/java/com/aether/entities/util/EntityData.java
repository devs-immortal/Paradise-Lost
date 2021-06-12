package com.aether.entities.util;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

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