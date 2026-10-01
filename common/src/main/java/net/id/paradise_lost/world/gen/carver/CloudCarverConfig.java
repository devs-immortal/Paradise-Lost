package net.id.paradise_lost.world.gen.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
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
            return carverConfig.yawMultiplier;
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

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugConfig, HolderSet.Named<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockStateProvider cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
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

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugConfig, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockStateProvider cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(probability, y, yScale, lavaLevel, debugConfig, BuiltInRegistries.BLOCK.getOrCreateTag(ParadiseLostBlockTags.CLOUD_CARVER_REPLACEABLES), horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }

    public CloudCarverConfig(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockStateProvider cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(probability, y, yScale, lavaLevel, CarverDebugSettings.DEFAULT, horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }

    public CloudCarverConfig(CarverConfiguration config, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, BlockStateProvider cloudState, FloatProvider yawMultiplier, FloatProvider yawPitchRatio, IntProvider sizeMultiplier, FloatProvider maxYaw, IntProvider engorgementChance, FloatProvider widthMultiplier) {
        this(config.probability, config.y, config.yScale, config.lavaLevel, config.debugSettings, horizontalRadiusMultiplier, verticalRadiusMultiplier, cloudState, yawMultiplier, yawPitchRatio, sizeMultiplier, maxYaw, engorgementChance, widthMultiplier);
    }
}
