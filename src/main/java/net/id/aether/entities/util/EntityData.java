package net.id.aether.entities.util;

import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class EntityData {
	public final int id;
	public final UUID uuid;
	public final double x;
	public final double y;
	public final double z;

	public EntityData(PacketByteBuf buf) {
		this.id = buf.readVarInt();
		this.uuid = buf.readUuid();
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}
}