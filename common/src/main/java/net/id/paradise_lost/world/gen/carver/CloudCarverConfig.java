package net.id.paradise_lost.world.gen.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

public class CloudCarverConfig extends CarverConfiguration {
    public static final Codec<CloudCarverConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(CarverConfiguration.CODEC.forGetter((carverConfig) -> {
            return carverConfig;
        }), FloatProvider.CODEC.fieldOf("horizontal_radius_multiplier").forGetter((carverConfig) -> {
            return carverConfig.horizontalRadiusMultiplier;
        }), FloatProvider.CODEC.fieldOf("vertical_radius_multiplier").forGetter((carverConfig) -> {
            return carverConfig.verticalRadiusMultiplier;
        }), BlockStateProvider.CODEC.fieldOf("cloud_block").forGetter((cloudCarverConfig -> {
            return cloudCarverConfig.cloudState;
        })), FloatProvider.CODEC.fieldOf("yaw_multiplier").forGetter((carverConfig) -> {
            return carverConfig.yawMultiplier;
        }), FloatProvider.CODEC.fieldOf("yaw_pitch_ratio").forGetter((carverConfig) -> {
            return carverConfig.yawPitchRatio;
        }), IntProvider.CODEC.fieldOf("size_multiplier").forGetter((carverConfig) -> {
            return carverConfig.sizeMultiplier;
        }), FloatProvider.CODEC.fieldOf("max_yaw").forGetter((carverConfig) -> {
            return carverConfig.maxYaw;
        }), IntProvider.CODEC.fieldOf("engorged_chance").forGetter((carverConfig) -> {
            return carverConfig.engorgementChance;
        }), FloatProvider.CODEC.fieldOf("width_multiplier").forGetter((carverConfig) -> {
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
    public final BlockStateProvider cloudState;

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugConfig, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockStateProvider cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
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

    public CloudCarverConfig(CarverConfiguration config, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockStateProvider cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(config.probability, config.y, config.yScale, config.lavaLevel, config.debugSettings, config.replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }
}
