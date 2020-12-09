package com.aether.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;

public class AetherSurfaceBuilderConfig implements SurfaceConfig {
	public static final Codec<AetherSurfaceBuilderConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockState.CODEC.fieldOf("top_material").forGetter(config -> {
			return config.topMaterial;
		}), BlockState.CODEC.fieldOf("under_material").forGetter(config -> {
			return config.underMaterial;
		}), BlockState.CODEC.fieldOf("underwater_material").forGetter(config -> {
			return config.underwaterMaterial;
		}), PrimitiveCodec.BOOL.fieldOf("generate_quick_soil").forGetter(config -> {
			return config.generateQuickSoil;
		})).apply(instance, AetherSurfaceBuilderConfig::new);
	});

	private final BlockState topMaterial;
	private final BlockState underMaterial;
	private final BlockState underwaterMaterial;
	private final boolean generateQuickSoil;

	public AetherSurfaceBuilderConfig(BlockState topMaterial, BlockState underMaterial, BlockState underwaterMaterial, boolean generateQuickSoil) {
		this.topMaterial = topMaterial;
		this.underMaterial = underMaterial;
		this.underwaterMaterial = underwaterMaterial;
		this.generateQuickSoil = generateQuickSoil;
	}

	@Override
	public BlockState getTopMaterial() {
		return this.topMaterial;
	}

	@Override
	public BlockState getUnderMaterial() {
		return this.underMaterial;
	}

	public BlockState getUnderwaterMaterial() {
		return this.underwaterMaterial;
	}

	public boolean shouldGenerateQuicksoil() {
		return this.generateQuickSoil;
	}
}
