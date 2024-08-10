package net.id.paradiselost.world.gen.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.heightprovider.HeightProvider;

public class CloudCarverConfig extends CarverConfig {
    public static final Codec<CloudCarverConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(CarverConfig.CONFIG_CODEC.forGetter((carverConfig) -> {
            return carverConfig;
        }), FloatProvider.VALUE_CODEC.fieldOf("horizontal_radius_multiplier").forGetter((carverConfig) -> {
            return carverConfig.horizontalRadiusMultiplier;
        }), FloatProvider.VALUE_CODEC.fieldOf("vertical_radius_multiplier").forGetter((carverConfig) -> {
            return carverConfig.verticalRadiusMultiplier;
        }), BlockState.CODEC.fieldOf("cloud_block").forGetter((cloudCarverConfig -> {
            return cloudCarverConfig.cloudState;
        })), FloatProvider.VALUE_CODEC.fieldOf("yaw_multiplier").forGetter((carverConfig) -> {
            return carverConfig.yawMultiplier;
        }), FloatProvider.VALUE_CODEC.fieldOf("yaw_pitch_ratio").forGetter((carverConfig) -> {
            return carverConfig.yawMultiplier;
        }), IntProvider.VALUE_CODEC.fieldOf("size_multiplier").forGetter((carverConfig) -> {
            return carverConfig.sizeMultiplier;
        }), FloatProvider.VALUE_CODEC.fieldOf("max_yaw").forGetter((carverConfig) -> {
            return carverConfig.maxYaw;
        }), IntProvider.VALUE_CODEC.fieldOf("engorged_chance").forGetter((carverConfig) -> {
            return carverConfig.engorgementChance;
        }), FloatProvider.VALUE_CODEC.fieldOf("width_multiplier").forGetter((carverConfig) -> {
            return carverConfig.widthMultiplier;
        })).apply(instance, CloudCarverConfig::new);
    });

    public final FloatProvider horizontalRadiusMultiplier;
    public final FloatProvider verticalRadiusMultiplier;
    public final FloatProvider yawMultiplier;
    public final FloatProvider yawPitchRatio;
    public final FloatProvider widthMultiplier;
    public final IntProvider sizeMultiplier;
    public final FloatProvider maxYaw;
    public final IntProvider engorgementChance;
    public final BlockState cloudState;

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, YOffset lavaLevel, CarverDebugConfig debugConfig, RegistryEntryList.Named<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockState cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        super(probability, y, yScale, lavaLevel, debugConfig, replaceable);
        this.horizontalRadiusMultiplier = horizontalRadiusMultiplier;
        this.verticalRadiusMultiplier = verticalRadiusMultiplier;
        this.yawMultiplier = yawMultiplier;
        this.yawPitchRatio = yawPitchRatio;
        this.widthMultiplier = widthMultiplier;
        this.sizeMultiplier = sizeMultiplier;
        this.maxYaw = maxYaw;
        this.engorgementChance = engorgementChance;
        this.cloudState = cloudState;
    }

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, YOffset lavaLevel, CarverDebugConfig debugConfig, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockState cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(probability, y, yScale, lavaLevel, debugConfig, Registries.BLOCK.getOrCreateEntryList(ParadiseLostBlockTags.CLOUD_CARVER_REPLACEABLES), horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, YOffset lavaLevel, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockState cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(probability, y, yScale, lavaLevel, CarverDebugConfig.DEFAULT, horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }

    public CloudCarverConfig(CarverConfig config, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockState cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(config.probability, config.y, config.yScale, config.lavaLevel, config.debugConfig, horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }
}
