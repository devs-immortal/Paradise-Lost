package com.aether.util;

import com.aether.Aether;
import com.aether.entities.block.FloatingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

public class NetworkingHell {
	public static final Identifier SPAWN_FLOATING_BLOCK_ENTITY = packet("entity", "spawn", "floating_block");

	public static void init() {

	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ClientSidePacketRegistry.INSTANCE.register(SPAWN_FLOATING_BLOCK_ENTITY, FloatingBlockEntity::spawn);
	}

	private static Identifier packet(String... path) {
		return Aether.locate(String.join(".", path));
	}
}