package com.aether.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public abstract class AetherNonLivingEntity extends Entity {
	public AetherNonLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	public AetherNonLivingEntity(World worldIn) {
		this(null, worldIn);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
}
