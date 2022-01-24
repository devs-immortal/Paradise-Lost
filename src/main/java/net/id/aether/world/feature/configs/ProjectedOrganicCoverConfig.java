package net.id.aether.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record ProjectedOrganicCoverConfig(BlockStateProvider cover, IntProvider branchLength, IntProvider projection, IntProvider tries, double heuristic) implements FeatureConfig {
    public static final Codec<ProjectedOrganicCoverConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("cover").forGetter(ProjectedOrganicCoverConfig::cover),
            IntProvider.VALUE_CODEC.fieldOf("branchLength").forGetter(ProjectedOrganicCoverConfig::branchLength),
            IntProvider.VALUE_CODEC.fieldOf("projection").forGetter(ProjectedOrganicCoverConfig::projection),
            IntProvider.VALUE_CODEC.fieldOf("tries").forGetter(ProjectedOrganicCoverConfig::tries),
            Codec.DOUBLE.fieldOf("heuristic").forGetter(ProjectedOrganicCoverConfig::heuristic)
    ).apply(instance, ProjectedOrganicCoverConfig::new));
}
